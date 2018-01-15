package Controller.NeuralNetwork;

import Model.ImageData;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MultiLayerPerceptron {
    private float[] inputNeurons;
    private float[] hiddenNeurons;
    private float[] outputNeurons;
    private float[] weights;

    //a buffer for the weights
    private float[] tempWeights;
    //used to keep the previous weights before modification, for momentum
    private float[] prWeights;

    private int inputN;
    private int outputN;
    private int hiddenN;
    private int hiddenL;

    MultiLayerPerceptron(){
        outputN = Configuration.outputN;
        inputN = Configuration.inputN;
        this.hiddenL = Configuration.hiddenLayers;
        this.hiddenN = Configuration.hiddenNeurons;

        //total number of weights required (input neurons + hidden neurons + output neurons)
        int weightsSize = inputN*hiddenN+(hiddenN*hiddenN*(hiddenL-1))+hiddenN*outputN;

        weights = new float[weightsSize];

        //initialising neurons in network
        inputNeurons = new float[inputN];
        hiddenNeurons = new float[hiddenN*hiddenL];
        outputNeurons = new float[outputN];

        //randomise weights for inputs to 1st hidden layer
        Random rn = new Random();
        rn.setSeed(System.currentTimeMillis());
        for(int i = 0; i < weightsSize; i++) {
            weights[i] = (rn.nextFloat() - 0.5f);
        }
    }


    /**
     * Saves the neural network to a file
     * @param filename Name of file to be saved
     */
    public void save(String filename) {
        try {
            RandomAccessFile aFile = new RandomAccessFile(filename, "rw");
            FileChannel outChannel = aFile.getChannel();

            //one float 4 bytes
            ByteBuffer buf = ByteBuffer.allocate(4 * (hiddenNeurons.length + weights.length));
            buf.clear();
            buf.asFloatBuffer().put(hiddenNeurons);
            buf.asFloatBuffer().put(weights);

            while (buf.hasRemaining()) {
                outChannel.write(buf);
            }

            outChannel.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Reads the weights and hidden neuron values to a file
     * @param filename Name of file to be read
     */
    public void load(String filename){
        try {
            RandomAccessFile rFile = new RandomAccessFile(filename, "rw");
            FileChannel inChannel = rFile.getChannel();
            ByteBuffer buf_in = ByteBuffer.allocate(4 * (hiddenNeurons.length + weights.length));
            buf_in.clear();

            inChannel.read(buf_in);

            buf_in.rewind();
            buf_in.asFloatBuffer().get(hiddenNeurons);
            buf_in.asFloatBuffer().get(weights);

            inChannel.close();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Uses the neural network to guess an image, then compares it to the actual value returned
     * @param imageToCompare sample image to test
     * @return boolean - matches image data sent?
     */
    public boolean GuessImage(ImageData imageToCompare){
        //first populate the input neurons
        populateInput(imageToCompare);

        int answer = imageToCompare.getNumber();

        //then calculate the network
        calculateNetwork();

        float winner = -1;
        int guess = 0;

        //find the best fitting output
        for(int i = 0; i < outputN; i++)
        {
            if(outputNeurons[i] > winner)
            {
                winner = outputNeurons[i];
                guess = i;
            }
        }
//        System.out.println("The neural network thinks that the correct answer " + answer
//                + " represents a " + guess);

        return guess == answer;
    }

    //assigns values to the input neurons
    private void populateInput(ImageData imageData) {
        int[] data = imageData.getNumOfBlackPixels();

        for(int i = 0; i < inputN; i++) {
            //if the specific pixel is on
            //set the corresponding neuron
            //values for each pixel range from 0 to 16, neural network takes anything above 8 to be "on" and below 8 to be off
            if(data[i] >= 8) {
                inputNeurons[i] = 1.0f;
            } else {
                inputNeurons[i] = 0.0f;
            }
        }
    }

    public boolean train(List<ImageData> imageDataArr){
        return train(
                Configuration.teachingStep,
                Configuration.leastMeanSquareError,
                Configuration.momentum,
                imageDataArr,
                Configuration.maxEpochs
        );
    }

    //trains the network according to our parameters
    private boolean train(float teachingStep, float lmse, float momentum, List<ImageData> imageDataArr, int maxEpochs) {
        float mse = 999.0f;
        int epochs = 1;
        float error = 0.0f;
        //the delta of the output layer
        float[] odelta = new float[outputN];
        //the delta of each hidden layer
        float[] hdelta = new float[hiddenN*hiddenL];


        tempWeights = Arrays.copyOf(weights, weights.length);

        //used to keep the previous weights before modification, for momentum
        prWeights = Arrays.copyOf(weights, weights.length);

        int target = 0;
        while(Math.abs(mse-lmse) > 0.0001f && epochs < maxEpochs) {
            //for each epoch reset the mean square error
            mse = 0.0f;

            //for each image
            for(ImageData imageData : imageDataArr) {

                //populate the input neurons
                populateInput(imageData);
                target = imageData.getNumber();

                //calculate the network
                calculateNetwork();

                //Now we have calculated the network for this iteration
                //back-propagate following the back-propagation algorithm
                for(int i = 0; i < outputN; i++) {
                    //let's get the delta of the output layer
                    //and the accumulated error
                    if(i != target) {
                        odelta[i] = (0.0f - outputNeurons[i])*dersigmoid(outputNeurons[i]);
                        error += (0.0f - outputNeurons[i])*(0.0f-outputNeurons[i]);
                    } else {
                        odelta[i] = (1.0f - outputNeurons[i])*dersigmoid(outputNeurons[i]);
                        error += (1.0f - outputNeurons[i])*(1.0f-outputNeurons[i]);
                    }
                }

                //we start propagating backwards now, to get the error of each neuron
                //in every layer let's get the delta of the last hidden layer first
                for(int i = 0; i < hiddenN; i++) {
                    hdelta[(hiddenL-1)*hiddenN+i] = 0.0f; //zero the values from the previous iteration

                    //add to the delta for each connection with an output neuron
                    for(int j = 0; j < outputN; j++)
                    {
                        hdelta[(hiddenL-1)*hiddenN+i] += odelta[j] * hiddenToOutput(i,j) ;
                    }

                    //The derivative here is only because of the
                    //delta rule weight adjustment about to follow
                    hdelta[(hiddenL-1)*hiddenN+i] *= dersigmoid(hiddenAt(hiddenL,i));
                }

                //now for each additional hidden layer, provided they exist
                for(int i = hiddenL-1; i > 0; i--) {

                    //add to each neuron's hidden delta
                    for(int j = 0; j < hiddenN; j++) { //from

                        hdelta[(i-1)*hiddenN+j] = 0.0f;//zero the values from the previous iteration
                        for(int k = 0; k < hiddenN; k++) { //to

                            //the previous hidden layers delta multiplied by the weights
                            //for each neuron
                            hdelta[(i-1)*hiddenN+j] += hdelta[i*hiddenN+k] * hiddenToHidden(i+1,j,k);
                        }

                        //The derivative here is only because of the
                        //delta rule weight adjustment about to follow
                        hdelta[(i-1)*hiddenN+j] *= dersigmoid(hiddenAt(i,j));
                    }
                }

                //Weights modification
                tempWeights = Arrays.copyOf(weights, weights.length);//keep the previous weights somewhere, we will need them

                //hidden to Input weights
                for(int i = 0; i < inputN; i ++) {
                    for(int j = 0; j < hiddenN; j ++) {
                        weights[inputN*j+i] +=
                                (momentum * (inputToHidden(i,j) - _prev_inputToHidden(i,j))) +
                                        (teachingStep * hdelta[j] * inputNeurons[i]);
                    }
                }

                //hidden to hidden weights, provided more than 1 layer exists
                for(int i = 2; i <= hiddenL; i++) {
                    for(int j = 0; j < hiddenN; j ++) { //from
                        for(int k =0; k < hiddenN; k ++) { //to
                            weights[inputN*hiddenN+((i-2)*hiddenN*hiddenN)+hiddenN*j+k] +=
                                    (momentum * (hiddenToHidden(i,j,k) - _prev_hiddenToHidden(i,j,k))) +
                                            (teachingStep * hdelta[(i-1)*hiddenN+k] * hiddenAt(i-1,j));
                        }
                    }
                }

                //last hidden layer to output weights
                for(int i = 0; i < outputN; i++) {
                    for(int j = 0; j < hiddenN; j ++) {
                        weights[(inputN*hiddenN + (hiddenL-1)*hiddenN*hiddenN + j*outputN+i)] +=
                                (momentum * (hiddenToOutput(j,i) - _prev_hiddenToOutput(j,i))) +
                                        (teachingStep * odelta[i] * hiddenAt(hiddenL,j));
                    }
                }

                prWeights = Arrays.copyOf(tempWeights, tempWeights.length);

                //add to the total mse for this epoch
                mse += error / (outputN+1f);
                //zero out the error for the next iteration
                error = 0.0f;
            }

            if(epochs % 1000 == 0) {
                System.out.println(epochs + " - " + mse);
            }
            epochs++;
        }
        return true;
    }

    //calculates the whole network, from input to output
    private void calculateNetwork() {
        //propagate towards the hidden layer
        for(int hiddenIndex = 0; hiddenIndex < hiddenN; hiddenIndex++)
        {
            hiddenNeurons[hiddenIndex] = 0.0f; // Layer one neuron.

            for(int input = 0 ; input < inputN; input++)
            {
                //set value of first layer hidden neurons to input neurons + weights
                hiddenNeurons[hiddenIndex] += inputNeurons[input] * inputToHidden(input,hiddenIndex);
            }

            //pass it through the activation function
            hiddenNeurons[hiddenIndex] = sigmoid(hiddenNeurons[hiddenIndex]);
        }

        //more than one hidden layers
        for(int i = 2; i <= hiddenL; i++) {
            //for each one of these extra layers calculate their values
            for(int j = 0; j < hiddenN; j++) { //to
                hiddenNeurons[(i-1)*hiddenN + j] = 0.0f;

                for(int k = 0; k <hiddenN; k++) { //from
                    hiddenNeurons[(i-1)*hiddenN + j] += hiddenNeurons[(i-2)*hiddenN + k] * hiddenToHidden(i,k,j);
                }

                //and finally pass it through the activation function
                hiddenNeurons[(i-1)*hiddenN + j] = sigmoid(hiddenNeurons[(i-1)*hiddenN + j]);
            }
        }

        //and now hidden to output
        for(int i = 0; i < outputN; i++) {
            outputNeurons[i] = 0.0f;

            for(int j = 0; j < hiddenN; j++)
            {
                outputNeurons[i] += hiddenNeurons[(hiddenL-1)*hiddenN + j] * hiddenToOutput(j,i);
            }

            //and finally pass it through the activation function
            outputNeurons[i] = sigmoid( outputNeurons[i] );
        }

    }

    //HELPER FUNCTIONS
    private float inputToHidden(int inp, int hid) {
        return weights[inputN*hid+inp];
    }
    private float hiddenToHidden(int toLayer, int fromHid, int toHid) {
        return weights[inputN*hiddenN+ ((toLayer-2)*hiddenN*hiddenN)+hiddenN*fromHid+toHid];
    }
    private float hiddenToOutput(int hid, int out)  {
        return weights[(inputN*hiddenN + (hiddenL-1)*hiddenN*hiddenN + hid*outputN+out)];
    }

    /*Helper macros just as above, but for the previous Weights*/
    private float _prev_inputToHidden(int inp, int hid) {
        return prWeights[inputN*hid+inp];
    }
    private float _prev_hiddenToHidden(int toLayer, int fromHid, int toHid) {
        return prWeights[inputN*hiddenN+ ((toLayer-2)*hiddenN*hiddenN)+hiddenN*fromHid+toHid];
    }
    private float _prev_hiddenToOutput(int hid, int out) {
        return prWeights[inputN*hiddenN + (hiddenL-1)*hiddenN*hiddenN + hid*outputN+out];
    }


    /*helper macro to locate the appropriate hidden neuron*/
    private float hiddenAt(int layer,int hid) {
        return hiddenNeurons[(layer-1)*hiddenN + hid];
    }

    /*math helper functions*/
    private float sigmoid(float value) {
        return (float)(1f/(1f+Math.exp(-value)));
    }
    private float dersigmoid(float value) {
        return (value*(1f-value));
    }

}


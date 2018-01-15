package Controller.NeuralNetwork;

//ANN

public class Configuration {
    final static int hiddenNeurons = 24;
    final static int hiddenLayers = 3;
    final static float teachingStep = 0.04f;
    final static float leastMeanSquareError = 0.01f;
    //multiplied by teachingStep so as to get a value closer to what we want
    final static float momentum = 0.4f;
    final static int maxEpochs = 40;

    //do not change
    final static int outputN = 10; //total possible outputs (0 to 9)
    final static int inputN = 64; //total inputs into the network (64 data points for each image)


}

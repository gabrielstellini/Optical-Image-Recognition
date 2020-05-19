# Overview

The application uses 3 algorithms:

•	Nearest neighbour/Euclidean distance (NN)

•	K-Nearest neighbour/Euclidean with some modification (K-NN)

•	Multi-layer perceptron neural network (MLP)

![Results](https://raw.githubusercontent.com/gabrielstellini/Optical-Image-Recognition/master/Results.PNG)


## Nearest neighbour

The nearest neighbour solution makes use of Euclidean distance to find the closest matching image of the number. It was the fastest algorithm and the most accurate.

The code implemented makes use of multi-threading to test the algorithm. For each value in the test data set, a thread was created which checks all values in the training data-set. Thus testing the accuracy can be done in seconds.



The Nearest neighbour algorithm also uses the least amount of system resources, as it only needs to store the number with the least distance.

## K-Nearest neighbour

The K-NN is a modification of the NN described above. It finds the 3 closest values (as opposed to finding only the closest value in NN) to the number it is guessing. The above tests were performed using a value of 3 for K. Thus it gets the 3 values with the least distance, and returns the most common number in the 3 values found.

In the main method, the value of K can be easily modified by changing the parameter. It should be noted that the higher the value of K, the less accurate the results.

_getKNNAccuracyMultiThreaded_(3, imagesData1, imagesData2);

## Multi-layer perceptron network

The neural network is built to accept the 64 inputs for each file, pass them through each of the hidden neurons and produce an output which can be 10 possibilities (0 to 9). The MLP uses a sigmoid function to calculate the network (and fill up the values as needed) and a derivative of the sigmoid function to train the network. The network uses backpropagation to train the network.

The inputs from the sample data set vary from 0 to 16. These were converted to 1s and 0s; where 1 is any value equal or bigger than 8, and 0 is any value smaller than 8. These inputs were then fed to the respective input neuron. This made it easier to train the neural network.


After some time running, the training produced the following results:

![Epoch results](https://raw.githubusercontent.com/gabrielstellini/Optical-Image-Recognition/master/Results2.PNG)

Thus it was decided that epochs should be limited to 5000 as there was no significant improvement in the mean square error.

The accuracy obtained in the result table was achieved by training the neural network on the training data set then filling the network input neurons with the test data set. The output of the neural network was then compared to the actual value for each image.

Other configuration details for the MLP can be found in the Configuration class under the `Controller.NeuralNetwork` package

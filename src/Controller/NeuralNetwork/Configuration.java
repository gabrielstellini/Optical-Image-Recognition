package Controller.NeuralNetwork;

public class Configuration {
    final static int hiddenNeurons = 24;
    final static int hiddenLayers = 3;
    final static float teachingStep = 0.04f;
    final static float leastMeanSquareError = 0.01f; //training will stop when error is lower than this
    final static float momentum = 0.4f; //multiplied by teachingStep so as to get a value closer to what we want
    final static int maxEpochs = 5000; //training will stop when epochs is higher than this

    final static int outputN = 10; //total possible outputs (0 to 9)
    final static int inputN = 64; //total inputs into the network (64 data points for each image)
}

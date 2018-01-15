package Controller.NeuralNetwork;

import Model.Algorithm;
import Model.ImageData;

import java.util.List;

public class MLPAlgorithm extends Algorithm {
    List<ImageData> list;
    ImageData imageToCompare;
    MultiLayerPerceptron mlp;

    public MLPAlgorithm(){
        this.mlp = new MultiLayerPerceptron();
    }

    public void train(){
        mlp.train(list);
    }

    public void save(){
        mlp.save("NNSaveFiles");
    }

    public void load(){
        mlp.load("NNSaveFiles");
    }

    public void run(){
        mlp.train(list);
    }

    @Override
    public boolean isCorrect() {
        return mlp.GuessImage(imageToCompare);
    }

    public boolean isCorrect(ImageData imageData) {
        return mlp.GuessImage(imageData);
    }

    @Override
    public void setList(List<ImageData> imageData) {
        this.list = imageData;
    }

    @Override
    public void setImageToCompare(ImageData imageToCompare) {
        this.imageToCompare = imageToCompare;
    }
}

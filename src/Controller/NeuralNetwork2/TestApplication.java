package Controller.NeuralNetwork2;

import Model.Algorithm;
import Model.ImageData;

import java.util.*;

public class TestApplication extends Algorithm{
    private List<ImageData> list;
    private ImageData imageToCompare;
    MultiLayerPerceptron mlp;

    public void run() {

//        Learn example
        mlp = new MultiLayerPerceptron(24, 3);
        if(!mlp.trainNetwork(0.04f, 0.01f, 0.4f, list, 40)) {
            System.out.println("There was an error while training ... Quitting\n\r");
            System.exit(0);
        }

        //save MLP example
//        mlp.save("test.data");

//        Load MLP example
//        MultiLayerPerceptron mlp2 = new MultiLayerPerceptron(24, 3);
//        mlp2.load("test.data");

//        for(String imageName : ImageNames) {
//            int answer = imageMap.get(imageName);
//            ImageReader ir = new ImageReader();
//            correct += mlp2.recallNetwork(ir.readImage(imageName), answer);
//        }
    }

    @Override
    public boolean isCorrect() {
        return mlp.recallNetwork(imageToCompare);
    }

    public boolean isCorrect(ImageData imageData) {
        return mlp.recallNetwork(imageData);
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

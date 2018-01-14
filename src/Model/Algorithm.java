package Model;

import Model.ImageData;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class Algorithm extends Thread{

    public static int getRandomIndex(LinkedList<ImageData> data) {
        return new Random().nextInt(data.size());
    }


    public abstract void setList(List<ImageData> imageData);
    public abstract void setImageToCompare(ImageData imageToCompare);
    public abstract boolean isCorrect();
}

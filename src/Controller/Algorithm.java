package Controller;

import Model.ImageData;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class Algorithm extends Thread{

    //non thread safe method
    abstract public void run(List<ImageData> list, ImageData imageToCompare);

    public static int getRandomIndex(LinkedList<ImageData> data) {
        return new Random().nextInt(data.size());
    }


    public abstract int getBestIndex();
    public abstract double getBestDistance();
    public abstract int getNumber();
}

package Controller;

import Model.ImageData;

import java.util.LinkedList;
import java.util.Random;

public abstract class Algorithm{

    abstract void run();

    public static int getRandomIndex(LinkedList<ImageData> data) {
        int rnd = new Random().nextInt(data.size());
        return rnd;
    }
}

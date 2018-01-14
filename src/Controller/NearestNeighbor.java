package Controller;

import Model.ImageData;

import java.util.List;

public class NearestNeighbor extends Algorithm {

    private int bestIndex = -1;
    private double bestDistance = -1;
    private int number = -1;
    private List<ImageData> list;
    private ImageData imageToCompare;

    public NearestNeighbor(){

    }

    public NearestNeighbor(List<ImageData> list, ImageData imageToCompare){
        this.list = list;
        this.imageToCompare = imageToCompare;
    }

    //NOT THREAD SAFE
    public void run(List<ImageData> list, ImageData imageToCompare){
        this.list = list;
        this.imageToCompare = imageToCompare;
        run();
    }

    @Override
    public void run() {
        //set an initial record
        bestIndex = 0;
        bestDistance = getDistance(list.get(0), imageToCompare);
        number = list.get(getBestIndex()).getNumber();


        double currentDistance;

        for (int i = 1; i < list.size(); i++){
            currentDistance = getDistance(list.get(i), imageToCompare);
            if(currentDistance < getBestDistance()){
                bestIndex = i;
                bestDistance = currentDistance;
                number = list.get(getBestIndex()).getNumber();
            }
        }
    }

    public static double getDistance(ImageData imageData1, ImageData imageData2){
        int[] image1BlackPixels = imageData1.getNumOfBlackPixels();
        int[] image2BlackPixels = imageData2.getNumOfBlackPixels();

        int distanceSquared = 0;

        int deltaPixels;
        for (int i = 0; i < image1BlackPixels.length; i++){
            deltaPixels = image1BlackPixels[i] - image2BlackPixels[i];
            distanceSquared += Math.pow(deltaPixels, 2);
        }

        return Math.sqrt(distanceSquared);
    }

    public int getBestIndex() {
        return bestIndex;
    }

    public double getBestDistance() {
        return bestDistance;
    }

    public int getNumber() {
        return number;
    }

    public boolean isCorrect(){
        return number == imageToCompare.getNumber();
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

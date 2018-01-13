package Model;

public class ImageData {
    private int[] numOfBlackPixels = new int[64];
    private int number = -1;

    public ImageData(int[] numOfBlackPixels, int number) {
        this.setNumOfBlackPixels(numOfBlackPixels);
        this.setNumber(number);
    }

    public int[] getNumOfBlackPixels() {
        return numOfBlackPixels;
    }

    public void setNumOfBlackPixels(int[] numOfBlackPixels) {
        this.numOfBlackPixels = numOfBlackPixels;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

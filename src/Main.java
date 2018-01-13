import Controller.*;
import Model.ImageData;

import java.util.LinkedList;

public class Main {
    public static void main(String[] args){
        ReadData fileData = new ReadData("src/Data/cw2DataSet1.csv");
        LinkedList<ImageData> imagesData = fileData.getData();
        printData(imagesData);

    }

    public static void printData(LinkedList<ImageData> imagesData){

        for (ImageData imageData: imagesData) {

            System.out.print("Number is: " + imageData.getNumber() + "! ");

            for (int i: imageData.getNumOfBlackPixels()){
                System.out.print(i + ",");
            }

            System.out.println();


        }
    }
}

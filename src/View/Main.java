package View;

import Controller.*;
import Controller.NeuralNetwork.MLPAlgorithm;
import Model.ImageData;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        ReadData fileData = new ReadData("src/Data/cw2DataSet1.csv");
        LinkedList<ImageData> imagesData1 = fileData.getData();

        ReadData fileData2 = new ReadData("src/Data/cw2DataSet2.csv");
        LinkedList<ImageData> imagesData2 = fileData2.getData();


//        printResults(nearestNeighbor);

        double accuracy;

//        accuracy = getNNAccuracyMultiThreaded(imagesData1, imagesData2);
//        System.out.println("Nearest neighbor accuracy from data set 1 to data set 2 is: " + accuracy);
//        accuracy = getNNAccuracyMultiThreaded(imagesData2, imagesData1);
//        System.out.println("Nearest neighbor accuracy from data set 2 to data set 1 is: " + accuracy);
//
//        accuracy = getKNNAccuracyMultiThreaded(3, imagesData1, imagesData2);
//        System.out.println("K-Nearest neighbor accuracy from data set 1 to data set 2 is: " + accuracy);
//        accuracy = getKNNAccuracyMultiThreaded(3, imagesData2, imagesData1);
//        System.out.println("K-Nearest neighbor accuracy from data set 2 to data set 1 is: " + accuracy);

        accuracy = getNeuralNetworkAccuracy(imagesData1, imagesData2);
        System.out.println("Neural network accuracy from data set 1 to data set 2 is: " + accuracy);
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

    public static double getNeuralNetworkAccuracy(LinkedList<ImageData> imagesData1, LinkedList<ImageData> imagesData2){
        MLPAlgorithm mlpAlgorithm = new MLPAlgorithm();
        mlpAlgorithm.setList(imagesData1);
        mlpAlgorithm.setImageToCompare(imagesData2.get(0));
        mlpAlgorithm.run();

        int totalCorrect = 0;

        for (ImageData imageData : imagesData2){
            boolean isCorrect = mlpAlgorithm.isCorrect(imageData);
            if (isCorrect){
                totalCorrect++;
            }
        }

        return 100.0 * (double) totalCorrect / imagesData1.size();
    }

    public static double getNNAccuracyMultiThreaded(LinkedList<ImageData> imagesData1, LinkedList<ImageData> imagesData2){
        List<NearestNeighbor> threadPool = new LinkedList<>();
        int totalCorrect = 0;

        for(ImageData currentImage: imagesData2) {
            NearestNeighbor nn = new NearestNeighbor(imagesData1, currentImage);
            threadPool.add(nn);
        }

        for (NearestNeighbor thread: threadPool) {
            thread.start();
        }

        for(NearestNeighbor thread: threadPool){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (NearestNeighbor thread: threadPool){
            if(thread.isCorrect()){
                totalCorrect++;
            }
        }
        return 100.0 * (double) totalCorrect / imagesData1.size();
    }

    public static double getKNNAccuracyMultiThreaded(int K, LinkedList<ImageData> imagesData1, LinkedList<ImageData> imagesData2){
        List<KNearestNeighbor> threadPool = new LinkedList<>();
        int totalCorrect = 0;

        for(ImageData currentImage: imagesData2) {
            KNearestNeighbor knn = new KNearestNeighbor(K, imagesData1, currentImage);
            threadPool.add(knn);
            knn.start();
        }

        for(KNearestNeighbor thread: threadPool){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (KNearestNeighbor thread: threadPool){
            if(thread.isCorrect()){
                totalCorrect++;
            }
        }
        return 100.0 * (double) totalCorrect / imagesData1.size();
    }
//
//    private static double getAlgorithmAccuracy(Algorithm algorithm, LinkedList<ImageData> imagesData1, LinkedList<ImageData> imagesData2) {
//        int totalCorrect = 0;
//        int totalIncorrect = 0;
//
//
//        for(ImageData currentImage: imagesData2) {
//            algorithm.setList(imagesData1);
//            algorithm.setImageToCompare(currentImage);
//            algorithm.run();
//
//            if(algorithm.getNumber() == currentImage.getNumber()){
//                totalCorrect++;
//            }
//            else {
//                totalIncorrect++;
//            }
//        }
//
//        return 100.0 * (double) totalCorrect / imagesData1.size();
//    }

    private static void printResults(NearestNeighbor algorithm){
        System.out.println("Most similar number was: " + algorithm.getNumber());
        System.out.println("Most similar index was: " + algorithm.getBestIndex());
        System.out.println("Difference in pixel data was: " + algorithm.getBestDistance());
    }
}

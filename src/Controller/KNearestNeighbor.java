package Controller;

import Model.ImageData;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;

public class KNearestNeighbor extends Algorithm {
    private int K = 0;
    private List<ImageData> list;
    private ImageData imageToCompare;
    //in the order of closest to least close
    private int[] matchingNumbers;

    public KNearestNeighbor(int K){
        this.K = K;
    }

    public KNearestNeighbor(int K, List<ImageData> list, ImageData imageToCompare){
        this.K = K;
        this.list = list;
        this.imageToCompare = imageToCompare;
    }

    //NOT THREAD SAFE
    public void run(int K, List<ImageData> list, ImageData imageToCompare){
        this.K = K;
        this.list = list;
        this.imageToCompare = imageToCompare;
        run();
    }

    @Override
    public void run() {
        matchingNumbers = new int[K];

        if(K > list.size()){
            System.out.println("K is bigger than the list size!");
            return;
        }

        double[] distances = new double[list.size()];

        //calculate all distances
        for (int i = 0; i < list.size(); i++){
            distances[i] = NearestNeighbor.getDistance(list.get(i), imageToCompare);
        }

        matchingNumbers = KSmallest(distances, K);
    }


    //used to sort distances and get the K of each
    public static int[] bottomK(final double[] input, final int K) {
        return IntStream.range(0, input.length)
                .boxed()
                .sorted(comparing(i -> input[i]))
                .mapToInt(i -> i)
                .limit(K)
                .toArray();
    }

    private int[] KSmallest(final double[] distances, final int K){
        int[] indexes = bottomK(distances, K);
        int[] matchingNumbers = new int[K];

        for (int i = 0; i < matchingNumbers.length; i++) {
                matchingNumbers[i] = list.get(indexes[i]).getNumber();
        }

        return matchingNumbers;
    }

    //returns most popular number in an array, O(n log n)
    public int findPopular(int[] a) {

        if (a == null || a.length == 0)
            return 0;

        Arrays.sort(a);

        int previous = a[0];
        int popular = a[0];
        int count = 1;
        int maxCount = 1;

        for (int i = 1; i < a.length; i++) {
            if (a[i] == previous)
                count++;
            else {
                if (count > maxCount) {
                    popular = a[i-1];
                    maxCount = count;
                }
                previous = a[i];
                count = 1;
            }
        }

        return count > maxCount ? a[a.length-1] : popular;
    }

    @Override
    public boolean isCorrect() {
        int guessedNumber = findPopular(matchingNumbers.clone());
        return guessedNumber == imageToCompare.getNumber();
    }

    @Override
    public void setList(List<ImageData> imageData) {
        this.list = imageData;
    }

    @Override
    public void setImageToCompare(ImageData imageToCompare) {
        this.imageToCompare = imageToCompare;
    }

    public void setK(int K){
        this.K = K;
    }


}

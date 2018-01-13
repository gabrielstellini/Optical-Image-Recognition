package Controller;

import Model.ImageData;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReadData {
    private File file;
    private LinkedList<ImageData> data = new LinkedList<>();

    public ReadData(String file){
        this.file = new File(file);
        extractNumbers();
    }

    public ReadData(File file){
        this.file = file;
        extractNumbers();
    }

//    public void extractNumbers(File csvFile)
//    {
//
//        Scanner scanner = null;
//
//        try {
//            scanner = new Scanner(csvFile);
//            ArrayList<Integer> intDataArr = new ArrayList<>();
//
//            //Gets all integers in a file
//            while (scanner.hasNext()) {
//                if (scanner.hasNextInt()) {
//                    intDataArr.add(scanner.nextInt());
//                } else {
//                    scanner.next();
//                }
//            }
//
//            for(int j = 0; j < intDataArr.size()/65; j++ ){
//                List pointList = intDataArr.subList(65*j, 65*(j+1));
//                int[] pointArr = convertIntegers(pointList);
//                ImageData imageData = new ImageData(pointArr, intDataArr.get(65));
//            }
//        } catch (IOException e) {
//            if (scanner != null) {
//                scanner.close();
//            }
//            e.printStackTrace();
//        }
//    }

    private void extractNumbers(){
        String line = "";
        String cvsSplitBy = ",";
        LinkedList<Integer> intDataArr = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] lineNumbersStr = line.split(cvsSplitBy);
                Arrays.stream(lineNumbersStr)
                        .mapToInt(Integer::parseInt)
                        .forEachOrdered(intDataArr::add);
            }

            for(int j = 0; j < intDataArr.size()/65; j++ ){
                List pointList = intDataArr.subList(65*j, (65*(j+1) - 1));
                int[] pointArr = convertIntegers(pointList);
                ImageData imageData = new ImageData(pointArr, intDataArr.get((65*(j+1) - 1)));
                data.add(imageData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Data is read");
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public LinkedList<ImageData> getData() {
        return data;
    }

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
}

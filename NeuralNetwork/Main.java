public class Main {

//    public static void main(String[] args){
//        int startIndex = 0;
//        int finishIndex = 60;
//        int l = 1000;
//        for (int i=0; i<20; i++){
//            System.out.println(startIndex+" "+finishIndex);
//            if (finishIndex+60 <= l){
//                startIndex = finishIndex;
//                finishIndex += 60;
//            } else {
//                startIndex = finishIndex;
//                finishIndex = l;
//            }
//            System.out.println(startIndex+" "+finishIndex);
//            System.out.println();
//        }
//
//    }
    
//    //Regression problem
//    public static void main(String[] args){
//        int size = 10;
//        int depth = 2;
//        int len = 1000;
//        double[][] X_train = new double[len][size];
//        double[] x_real = new double[len];
//        double[] y_real = new double[len];
//        double min = -2*Math.PI;
//        double max = 2*Math.PI;
//        double inc = Math.abs(max-min)/len;
//        int j = 0;
//        double val;
//        for (int i=0; i<len; i++){
//            double[] temp = new double[size];
//            double num = min + i*inc;
//            val = Math.exp(Math.sin(num)*num)*Math.sin(num);
//            Arrays.fill(temp, val);
//            X_train[j] = temp;
//            y_real[j] = val;
//            x_real[j] = i;
//            j++;
//        }
//
//        RegressionNeuralNetwork NN = new RegressionNeuralNetwork(depth, size, X_train, y_real);
//        NN.fit(1e-5);
////        System.out.println();
////        NN.print();
////        System.out.println("Gradients: ");
////        NN.printGradients();
//
//        double[] y_pred = NN.predictions();
//        double[][] x = new double[2][x_real.length];
//        double[][] y = new double[2][y_real.length];
//        x[0] = x_real;
//        x[1] = x_real;
//        y[0] = y_real;
//        y[1] = y_pred;
//
//        JFrame frame = new JFrame("Simple Graph Plotter");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(600, 600);
//        frame.add(new GraphPlotter.GraphPanel(x, y));
//        frame.setVisible(true);
//
////        System.out.println("\n\n");
////        System.out.println(Arrays.toString(NN.predictions()));
////        System.out.println(Arrays.toString(y_real));
////        System.out.println(NN.cost());
//
//
//    }
//


//    //Classification
//private static void printMnistMatrix(final MnistMatrix matrix) {
//    System.out.println("label: " + matrix.getLabel());
//    for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
//        for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
//            System.out.print(matrix.getValue(r, c) + " ");
//        }
//        System.out.println();
//    }
//}

//    public static int[] compressArray(int[][] multiDimArray) {
//        int totalElements = 0;
//        for (int i = 0; i < multiDimArray.length; i++) {
//            totalElements += multiDimArray[i].length;
//        }
//
//        int[] oneDimArray = new int[totalElements];
//        int index = 0;
//
//        for (int i = 0; i < multiDimArray.length; i++) {
//            for (int j = 0; j < multiDimArray[i].length; j++) {
//                oneDimArray[index] = multiDimArray[i][j];
//                index++;
//            }
//        }
//
//        return oneDimArray;
//    }

//    public static double[] intArrayToDoubleArray(int[] intArray) {
//        double[] doubleArray = new double[intArray.length];
//
//        for (int i = 0; i < intArray.length; i++) {
//            doubleArray[i] = (double) intArray[i]; // Type cast int to double
//        }
//
//        return doubleArray;
//    }

//    public static double[][] intArrayToDoubleArray(int[][] intArray) {
//        double[][] doubleArray = new double[intArray.length][intArray[0].length];
//
//        for (int i = 0; i < intArray.length; i++) {
//            for (int j=0; j<intArray[i].length; j++){
//                doubleArray[i][j] = (double) intArray[i][j];
//            }
//        }
//
//        return doubleArray;
//    }
//    public static void main(String[] args) throws IOException {
//        MnistMatrix[] mnistMatrix = new MnistDataReader().readData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte");
//        mnistMatrix = new MnistDataReader().readData("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte");
//
//        int[][] x_data = new int[10000][784];
//        int[][] y_data = new int[10000][10];
//        for (int i=0; i<mnistMatrix.length; i++){
//            for (int j=0; j<mnistMatrix[i].getData().length; j++){
//                x_data[i] = compressArray(mnistMatrix[i].getData());
//                y_data[i][mnistMatrix[i].getLabel()] = 1;
//            }
//        }
//
//
//        double[][] X_train = intArrayToDoubleArray(x_data);
//        double[][] y_train = intArrayToDoubleArray(y_data);
//        int[][] size ={
//            {x_data[0].length, 10},
//            {10, 10},
//        };
//        int depth = 2;
//        ClassificationNeuralNetwork NN = new ClassificationNeuralNetwork(depth, size, X_train, y_train);
//        //NN.print();
//        int correct = 0;
//        for (int i = 0; i < X_train.length; i++) {
//            int predVal = predictedValue(NN.feedForward(X_train[i]));
//            int realVal = predictedValue(y_train[i]);
//            if (predVal == realVal) {
//                correct += 1;
//            }
//          //  System.out.println("Predicted: " + predVal + " Real: " + realVal);
//        }
//        double ratio = (double)correct/ (double)X_train.length;;
//        System.out.println("Ratio: " + ratio);
//        System.out.println();
//
//        NN.fit(1e-6);
////        System.out.println();
////        NN.print();
////        System.out.println();
////        NN.printGradients();
//
//        System.out.println("After training:");
//        correct = 0;
//        for (int i = 0; i < X_train.length; i++) {
//            int predVal = predictedValue(NN.feedForward(X_train[i]));
//            int realVal = predictedValue(y_train[i]);
//            if (predVal == realVal) {
//                correct += 1;
//            }
//            //  System.out.println("Predicted: " + predVal + " Real: " + realVal);
//        }
//        ratio = (double)correct/ (double)X_train.length;;
//        System.out.println("Ratio: " + ratio);
//        System.out.println();
//
//        //System.out.println(Arrays.toString(size[0])+" "+Arrays.toString(size[1]));
//
//    }

    public static int predictedValue(double[] predictions){
        int val = 11;
        double min = Double.MIN_VALUE;
        for (int i=0; i<predictions.length; i++){
            if (predictions[i]>min){
                val = i;
                min = predictions[i];
            }
        }
        return val;
    }

    public static void main(String[] args) {

        double[][] x_data = new double[150][3];
        double[][] y_data = new double[150][3];

        for (int i = 0; i < x_data.length; i++) {
            if (i < x_data.length/3) {
                x_data[i][0] = 0;
                x_data[i][1] = 0;
                y_data[i][0] = 1;
            } else if (i < 2*(x_data.length/3)) {
                x_data[i][0] = 0;
                x_data[i][1] = 1;
                y_data[i][1] = 1;
            } else {
                x_data[i][0] = 1;
                x_data[i][1] = 1;
                y_data[i][2] = 1;
            }
        }

        ClassificationNeuralNetwork CNN = new ClassificationNeuralNetwork(2, 3, x_data, y_data);

        int correct = 0;
        for (int i = 0; i < x_data.length; i++) {
            int predVal = predictedValue(CNN.feedForward(x_data[i]));
            int realVal = predictedValue(y_data[i]);
            if (predVal == realVal) {
                correct += 1;
            }
          //  System.out.println("Predicted: " + predVal + " Real: " + realVal);
        }
        double ratio = (double)correct/ (double)x_data.length;;
        System.out.println("Ratio: " + ratio);
        System.out.println();

        //CNN.print();
        CNN.fit(1e-2);
        //CNN.print();

        System.out.println("After training:");

        correct = 0;
        for (int i = 0; i < x_data.length; i++) {
            int predVal = predictedValue(CNN.feedForward(x_data[i]));
            int realVal = predictedValue(y_data[i]);
            if (predVal == realVal) {
                correct++;
            }
           // System.out.println("Predicted: " + predVal + " Real: " + realVal);
        }
        ratio = (double)correct/ (double)x_data.length;
        System.out.println("Ratio: " + ratio);
        System.out.println();
    }
}

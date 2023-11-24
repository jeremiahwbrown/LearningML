import javax.swing.JFrame;

import java.util.Arrays;

public class Main {

    //Regression problem
    public static void main(String[] args){
        int size = 8;
        int depth = 2;
        int len = 500;
        double[][] X_train = new double[len][size];
        double[] x_real = new double[len];
        double[] y_real = new double[len];
        double min = -2;
        double max = 2;
        double inc = Math.abs(max-min)/len;
        int j = 0;
        double val;
        for (int i=0; i<len; i++){
            double[] temp = new double[size];
            double num = min + i*inc;
            //val = Math.exp(Math.sin(num)*num)*Math.sin(num);
            val = Math.pow(num, 3.0);
            Arrays.fill(temp, val);
            X_train[j] = temp;
            y_real[j] = val;
            x_real[j] = i;
            j++;
        }

        RegressionNeuralNetwork NN = new RegressionNeuralNetwork(depth, size, X_train, y_real);
        NN.fit(5e-4);
//        System.out.println();
//        NN.print();
//        System.out.println("Gradients: ");
//        NN.printGradients();

        double[] y_pred = NN.predictions();
        double[][] x = new double[2][x_real.length];
        double[][] y = new double[2][y_real.length];
        x[0] = x_real;
        x[1] = x_real;
        y[0] = y_real;
        y[1] = y_pred;

        JFrame frame = new JFrame("Simple Graph Plotter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(new GraphPlotter.GraphPanel(x, y));
        frame.setVisible(true);

//        System.out.println("\n\n");
//        System.out.println(Arrays.toString(NN.predictions()));
//        System.out.println(Arrays.toString(y_real));
//        System.out.println(NN.cost());


    }


//
////    //Classification
//    public static int predictedValue(double[] predictions){
//        int val = 11;
//        double min = Double.MIN_VALUE;
//        for (int i=0; i<predictions.length; i++){
//            if (predictions[i]>min){
//                val = i;
//                min = predictions[i];
//            }
//        }
//        return val;
//    }
//
//    public static void main(String[] args) {
//
//        double[][] x_data = new double[150][3];
//        double[][] y_data = new double[150][3];
//
//        for (int i = 0; i < x_data.length; i++) {
//            if (i < x_data.length/3) {
//                x_data[i][0] = 0;
//                x_data[i][1] = 0;
//                y_data[i][0] = 1;
//            } else if (i < 2*(x_data.length/3)) {
//                x_data[i][0] = 0;
//                x_data[i][1] = 1;
//                y_data[i][1] = 1;
//            } else {
//                x_data[i][0] = 1;
//                x_data[i][1] = 1;
//                y_data[i][2] = 1;
//            }
//        }
//
//        ClassificationNeuralNetwork CNN = new ClassificationNeuralNetwork(2, 3, x_data, y_data);
//
//        int correct = 0;
//        for (int i = 0; i < x_data.length; i++) {
//            int predVal = predictedValue(CNN.feedForward(x_data[i]));
//            int realVal = predictedValue(y_data[i]);
//            if (predVal == realVal) {
//                correct += 1;
//            }
//          //  System.out.println("Predicted: " + predVal + " Real: " + realVal);
//        }
//        double ratio = (double)correct/ (double)x_data.length;;
//        System.out.println("Ratio: " + ratio);
//        System.out.println();
//
//        //CNN.print();
//        CNN.fit(1e-2);
//        //CNN.print();
//
//        System.out.println("After training:");
//
//        correct = 0;
//        for (int i = 0; i < x_data.length; i++) {
//            int predVal = predictedValue(CNN.feedForward(x_data[i]));
//            int realVal = predictedValue(y_data[i]);
//            if (predVal == realVal) {
//                correct++;
//            }
//           // System.out.println("Predicted: " + predVal + " Real: " + realVal);
//        }
//        ratio = (double)correct/ (double)x_data.length;
//        System.out.println("Ratio: " + ratio);
//        System.out.println();
//    }
}

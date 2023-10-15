import javax.swing.JFrame;

import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        int size = 10;
        int depth = 2;
        int len = 1000;
        double[][] X_train = new double[len][size];
        double[] x_real = new double[len];
        double[] y_real = new double[len];
        double min = -2*Math.PI;
        double max = 2*Math.PI;
        double inc = Math.abs(max-min)/len;
        int j = 0;
        double val;
        for (int i=0; i<len; i++){
            double[] temp = new double[size];
            double num = min + i*inc;
            val = Math.exp(Math.sin(num)*num)*Math.sin(num);
            Arrays.fill(temp, val);
            X_train[j] = temp;
            y_real[j] = val;
            x_real[j] = i;
            j++;
        }


        RegressionNeuralNetwork NN = new RegressionNeuralNetwork(depth, size, X_train, y_real);
        NN.fit(1e-5);
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
}

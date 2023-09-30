import javax.swing.*;
import java.util.Arrays;

class Main{

    public static void main(String[] args){
        int size = 16;
        int depth = 2;
        int len = 100;
        double[][] X_train = new double[len][size];
        double[] x_real = new double[len];
        double[] y_real = new double[len];
        double min = -Math.PI;
        double max = Math.PI;
        double inc = Math.abs(max-min)/len;
        int j = 0;
        for (double i=min; i<=max; i+=inc){
            double[] temp = new double[size];
            double val = Math.sin(i);//Math.pow(i, 3.0);
            Arrays.fill(temp, val);
            X_train[j] = temp;
            y_real[j] = val;
            x_real[j] = i;
            j++;
        }


        NeuralNetwork NN = new NeuralNetwork(size, depth, X_train, y_real);
        NN.fit();
        NN.printDifference();
        NN.printNet();

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
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RegressionNeuralNetwork {
    NeuralNetwork network;
    double gradient;
    double[][] x_data;
    double[] y_data;
    double g;

    ArrayList<Integer> indices;

    public RegressionNeuralNetwork(int depth, int[][] size, double[][] x, double[] y) {
        this.x_data = x;
        this.y_data = y;
        this.gradient = 0.0d;
        this.network = new NeuralNetwork(depth, size);
        //int tempSize = this.network.Net[this.network.Net.length-1].getOutputSize();
        this.g = 0.9;
        this.indices = new ArrayList<>();
        for (int i = 0; i < this.x_data.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
    }

    public RegressionNeuralNetwork(int depth, int width, double[][] x, double[] y) {
        this.x_data = x;
        this.y_data = y;
        this.network = new NeuralNetwork(depth, width);
        this.indices = new ArrayList<>();
        for (int i = 0; i < this.x_data.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        //int tempSize = this.network.Net[this.network.Net.length-1].getOutputSize();
    }

    //public void setMomenumt(double x){this.g=x;}

    public double feedForward(double[] input) {
        double[] out = this.network.feedForward(input);
        double result = CustomMath.sum(out);
        return result;
    }

    public void gradient(double loss) {
        this.gradient = loss;
        double[] grads = new double[this.y_data.length];
        Arrays.fill(grads, loss);
        NeuralNetwork.TempObject temp = new NeuralNetwork.TempObject(grads);
        this.network.gradient(temp, true);
    }

    public void zeroGradient() {
        this.gradient = 0.0d;
        this.network.zeroGradient();
    }

    public void backPropigate(double a) {
        this.network.backPropigate(a, 1.0, (this.g / this.x_data.length));
        //this.network.backPropigate(a, (this.g/this.x_data.length), (this.g/this.x_data.length));
    }

    public double epoch(double a, int start, int finish) {
        this.zeroGradient();
        double cost = 0.0;
        for (int i = start; i < finish; i++) {
            double loss = this.feedForward(this.x_data[this.indices.get(i)])-this.y_data[this.indices.get(i)];
            //double loss = this.feedForward(this.x_data[i]) - this.y_data[i];
            this.gradient(loss);
//            System.out.println("Loss: "+loss);
//            System.out.println("Math.pow: "+Math.pow(loss, 2.0));
//            System.out.println("Cost: "+cost);
            cost += Math.pow(loss, 3.0);
        }
        this.backPropigate(a);
        System.out.println(start+" "+finish);
        return cost / (finish - start);
    }

    public double cost() {
        double cost = 0.0;
        for (int i = 0; i < this.x_data.length; i++) {
            double loss = this.feedForward(x_data[i]) - this.y_data[i];
            cost += Math.pow(loss, 2.0);
        }
        return cost / this.x_data.length;
    }

    public double[] predictions() {
        double[] result = new double[y_data.length];
        for (int i = 0; i < y_data.length; i++) {
            result[i] = this.feedForward(x_data[i]);
        }
        return result;
    }

    void print() {
        this.network.print();
    }

    void printGradients() {
        this.network.printGradients();
    }

    public void fit(double a) {
        double cost = 0.0;
        int increment = 0;
        double initialCost = this.cost();
        int inc = 32;
        int startIndex = 0;
        int finishIndex = startIndex + inc;
        int max_iterations = 100;//60 * (this.x_data.length/inc + 1);
        System.out.println("Initial cost=" + cost);

        whole:
        for (int i = 0; i < 3; i++) {
            while (increment <= max_iterations) {
                cost = this.epoch(a, startIndex, finishIndex);
                if (Double.isNaN(cost) || Double.isInfinite(cost)) {
                    break whole;
                }

                increment += 1;
                if (finishIndex >= this.x_data.length) {
                    startIndex = 0;
                    finishIndex = inc;
                } else if (finishIndex + 2 * inc >= this.x_data.length) {
                    startIndex = finishIndex;
                    finishIndex = this.x_data.length;
                } else {
                    startIndex = finishIndex;
                    finishIndex += inc;
                }
            }
            System.out.println("Increments=" + increment + " cost=" + cost + " a=" + a + "\n");
            if (initialCost - cost < 0){
                a /= 10;
            }
        }
        System.out.println("Finished: cost=" + cost + " a=" + a+" |change|="+Math.abs(initialCost-cost));
    }
}

import java.util.ArrayList;
import java.util.Arrays;

public class ClassificationNeuralNetwork {

    class SigmoidLayer{
        double[] expActivations;
        double[] gradients;
        int size;

        SigmoidLayer(int size){
            this.size = size;
            this.expActivations = new double[size];
            this.gradients = new double[size];
        }

        public void calculateSoftmax(double[] prevActivations){
            double sum = 0.0;
            for (int i=0; i<prevActivations.length; i++){
                sum+=Math.exp(prevActivations[i]);
            }
            for (int i=0; i<this.size; i++){
                this.expActivations[i] = Math.exp(prevActivations[i])/sum;
            }
        }
        void calcGradient(double[] realY){
            for (int i=0; i<this.expActivations.length; i++){
                this.gradients[i] = this.expActivations[i] - realY[i];
            }
        }

        void zeroGradient(){
            this.gradients = new double[this.size];
        }


    }

    NeuralNetwork network;
    SigmoidLayer lastLayer;
    double[][] x_data;
    double[][] y_data;
    double g;

    ArrayList<Integer> indices;

    ClassificationNeuralNetwork(int depth, int[][] size, double[][] x, double[][] y){
        this.network = new NeuralNetwork(depth, size);
        this.lastLayer = new SigmoidLayer(this.network.Net[this.network.Net.length-1].getOutputSize());
        this.x_data = x;
        this.y_data = y;
        this.g = 0.0d;
        this.indices = new ArrayList<>();
        for (int i = 0; i < this.x_data.length; i++) {
            indices.add(i);
        }
    }

    ClassificationNeuralNetwork(int depth, int width, double[][] x, double[][] y){
        this.network = new NeuralNetwork(depth, width);
        this.lastLayer = new SigmoidLayer(width);
        this.x_data = x;
        this.y_data = y;
        this.g = 0.0d;
        this.indices = new ArrayList<>();
        for (int i = 0; i < this.x_data.length; i++) {
            indices.add(i);
        }
    }

    public double[] feedForward(double[] input){
        double[] out = this.network.feedForward(input);
        this.lastLayer.calculateSoftmax(input);
        return this.lastLayer.expActivations;
    }

    public void gradient(double[] y){
        this.lastLayer.calcGradient(y);
        NeuralNetwork.TempObject temp = new NeuralNetwork.TempObject(this.lastLayer.gradients);
        this.network.gradient(temp, true);
    }

    public void zeroGradient(){
        this.lastLayer.zeroGradient();
        this.network.zeroGradient();
    }

    public void backPropigate(double a){
        this.network.backPropigate(a, 1.0, 0.0);
    }

//    public double cost(){
//        double loss = 0.0;
//        for (int i=0; i<this.y_data.length; i++){
//            double[] pred = this.feedForward(this.x_data[i]);
//            for(int j=0; j<pred.length; j++){
//                loss += -1.0d*this.y_data[i]*Math.log(pred[j]);
//            }
//        }
//        return loss/this.y_data.length;
//    }


    public double loss(double[] pred, double[] y_real){
        double l = 0.0;
        for (int i=0; i<pred.length; i++){
            l += -1.0d*y_real[i]*Math.log(pred[i]);
        }
        return l;
    }

    public double epoch(double a, int start, int finish) {
        this.zeroGradient();
        double cost = 0.0;
        for (int i = start; i < finish; i++) {
            double[] pred = this.feedForward(this.x_data[this.indices.get(i)]);
            //double loss = this.feedForward(this.x_data[i]) - this.y_data[i];
            this.gradient(this.y_data[this.indices.get(i)]);
//            System.out.println("Loss: "+loss);
//            System.out.println("Math.pow: "+Math.pow(loss, 2.0));
//            System.out.println("Cost: "+cost);
            cost += loss(pred, y_data[this.indices.get(i)]);
        }
        this.backPropigate(a);
       //System.out.println(start+" "+finish);
        return cost / (finish - start);
    }

    public void fit(double a) {
        double initialCost = 0.0;
        double cost = 0.0;
        int increment = 0;
        int inc = 120;
        int startIndex = 0;
        int finishIndex = startIndex + inc;
        int max_iterations = 60 * (this.x_data.length/inc + 1);

        whole:
        for (int i = 0; i < 3; i++) {
            while (increment <= max_iterations) {
                cost = this.epoch(a, startIndex, finishIndex);
                if (Double.isNaN(cost) || Double.isInfinite(cost)) {
                    break whole;
                }
                if (i==0 && increment==0){
                    initialCost = cost;
                    System.out.println("Initial cost=" + cost);
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
            System.out.println("Increments=" + increment + " cost=" + cost + " a=" + a +
                " change="+ (initialCost-cost)+"\n");
            if (initialCost - cost < 0){
                a /= 10;
            }
        }
        System.out.println("Finished: cost=" + cost + " a=" + a+" change="+ (initialCost-cost));
    }

    public void print(){
        this.network.print();
        System.out.println(Arrays.toString(this.lastLayer.expActivations));
    }

    public void printGradients(){
        this.network.printGradients();
        System.out.println(Arrays.toString(this.lastLayer.gradients));
    }

}

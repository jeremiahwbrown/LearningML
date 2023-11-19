import java.util.Arrays;
import java.util.Random;

class NeuralNetwork {

    class Neuron{

        int size;
        private double[] weights;
        private double bias;
        private double[] multipliers;
        private double activation;
        private double output;
        private double[] gradients;
        private double[] previousGrad;
        private double neuronGradient;

        Neuron(int size, Random rand){
            this.size = size;
            this.weights = new double[size];
            this.multipliers = new double[size];
            this.activation = 0.0;
            this.output = 0.0;
            this.gradients = new double[size];
            this.neuronGradient = 1.0;
            this.previousGrad = new double[size];

            double min = -Math.pow(2.0/size, .5);
            double max = Math.pow(2.0/size, .5);
            for (int i=0; i<this.size; i++){
                this.weights[i] = min + (max - min)*rand.nextDouble();
                this.multipliers[i] = 1.0d;
                this.gradients[i] = 1.0d;
            }
            this.bias = min + (max - min)*rand.nextDouble();

        }

        private double dot(double[] input){
            double result = 0.0;
            for (int i=0; i<input.length; i++){
                result += input[i]*this.weights[i];
            }
            return result;
        }

        double feedForward(double[] input){
            double dot_p = this.dot(input);
            this.multipliers = input;
            this.output = dot_p+this.bias;
            double actv = CustomMath.relu(this.output);
            this.activation = actv;
            return actv;
        }

        double[] getWeights(){return this.weights;}
        double[] getGradients(){return this.gradients;}
    }

    static class TempObject{
        double[][] W;
        double[] G;
        TempObject(Layer layer){
            double[][] weights = new double[layer.outputSize][layer.inputSize];
            double[] gradients = new double[layer.outputSize];
            for (int i=0; i<layer.outputSize; i++){
                for (int j=0; j<layer.inputSize; j++){
                    weights[i][j] = layer.layer[i].weights[j];
                }
                gradients[i] = layer.layer[i].neuronGradient;
            }
            this.W = weights;
            this.G = gradients;
        }

        TempObject(double[] grad){
            this.W = null;
            this.G = Arrays.copyOf(grad, grad.length);
        }

        TempObject(TempObject temp){
            this.W = Arrays.copyOf(temp.W, temp.W.length);
            this.G = Arrays.copyOf(temp.G, temp.G.length);
        }

    }

    class Layer{

        Neuron[] layer;
        private final int inputSize;
        private final int outputSize;

        Layer(int inputSize, int outputSize, Random rand){
            this.inputSize = inputSize;
            this.outputSize = outputSize;
            this.layer = new Neuron[outputSize];
            for (int i=0; i<this.outputSize; i++){
                this.layer[i] = new Neuron(this.inputSize, rand);
            }
        }

        public int getInputSize() {return inputSize;}
        public int getOutputSize() {return outputSize;}

        double[] feedForward(double[] input){
            double[] activations = new double[this.outputSize];
            for (int i=0; i<this.outputSize; i++){
                activations[i] = this.layer[i].feedForward(input);
            }
            return activations;
        }

        TempObject gradient(TempObject prevLayer, boolean isLastLayer){
            if (isLastLayer){
                for(int j=0; j<this.outputSize; j++){
                    this.layer[j].neuronGradient = prevLayer.G[j]*CustomMath.relu_derivative(this.layer[j].output);
                }

            } else {
                double gradientSum = 0.0;
                for (int j=0; j<this.outputSize; j++){
                    for (int i=0; i<prevLayer.W.length; i++){
                        gradientSum += prevLayer.W[j][i]*prevLayer.G[j];
                    }
                    this.layer[j].neuronGradient = gradientSum*CustomMath.relu_derivative(this.layer[j].output);
                    gradientSum = 0.0;
                }
            }

            for (int i=0; i<this.outputSize; i++){
                for (int j=0; j<this.inputSize; j++){
                    this.layer[i].gradients[j] += this.layer[i].multipliers[j]*this.layer[i].neuronGradient;
                }
            }
            TempObject result = new TempObject(this);
            return result;
        }

        void zeroGradient(){
            for (int i=0; i<this.outputSize; i++){
                for (int j=0; j<this.inputSize; j++){
                    this.layer[i].previousGrad[j] = this.layer[i].gradients[j];
                    this.layer[i].gradients[j] = 0.0d;
                }
                this.layer[i].neuronGradient = 0.0d;
            }
        }

        void backPropigate(double a, double adj, double g){
            for (int i=0; i<outputSize; i++){
                for (int j=0; j<inputSize; j++){
                    this.layer[i].weights[j] -= (a*adj*this.layer[i].gradients[j] + g*this.layer[i].previousGrad[j]);
                }
                this.layer[i].bias -=a*adj*this.layer[i].neuronGradient;
            }
        }

        void print(){
            for (int i=0; i<this.outputSize; i++){
                System.out.println(Arrays.toString(this.layer[i].weights)+" "+this.layer[i].bias);
                }
                System.out.println();
            }

        void printGradients(){
            for (int i=0; i<this.outputSize; i++){
                System.out.println(Arrays.toString(this.layer[i].gradients));
                System.out.println();
            }
        }
    }


    Layer[] Net;
    NeuralNetwork(int depth, int[][] size){
        Random rand = new Random(371242371);
        this.Net = new Layer[depth];
        for(int i=0; i<size.length; i++){
            this.Net[i] = new Layer(size[i][0], size[i][1], rand);
        }
    }

    NeuralNetwork(int depth, int width){
        Random rand = new Random(371242371);
        this.Net = new Layer[depth];
        for (int i=0; i<depth; i++){
            this.Net[i] = new Layer(width, width, rand);
        }
    }

    double[] feedForward(double[] input){
        double[] activations = input;
        double[] temp;
        for (int i=0; i<this.Net.length; i++){
            temp = this.Net[i].feedForward(activations);
            activations = temp;
        }
        return activations;
    }

    void gradient(TempObject output, boolean isLast){
        TempObject result = output;
        TempObject temp;
        for (int i=this.Net.length-1; i>=0; i--){
            temp = this.Net[i].gradient(result, isLast);
            result = temp;
            isLast = false;
        }
    }

    void zeroGradient(){
        for (int i=0; i<this.Net.length; i++){
            this.Net[i].zeroGradient();
        }
    }

    void backPropigate(double a, double adj, double g){
        for (int i=0; i<this.Net.length; i++){
            this.Net[i].backPropigate(a, adj, g);
        }
    }

    void printGradients(){
        for (int i=0; i<this.Net.length; i++){
            this.Net[i].printGradients();
            System.out.println("\n");
        }
    }

    void print(){
        for (int i=0; i<this.Net.length; i++){
            this.Net[i].print();
            System.out.println("\n");
        }
    }
}

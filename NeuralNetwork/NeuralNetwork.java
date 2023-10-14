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
        private double neuronGradient;

        Neuron(int size){
            this.size = size;
            this.weights = new double[size];
            this.bias = 0.0;
            this.multipliers = new double[size];
            this.activation = 0.0;
            this.output = 0.0;
            this.gradients = new double[size];
            this.neuronGradient = 0.0;

            Random rand = new Random();
            for (int i=0; i<this.size; i++){
                this.weights[i] = rand.nextDouble();
            }
            this.bias = rand.nextDouble();

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
            double[][] W = new double[layer.outputSize][layer.inputSize];
            double[] G = new double[layer.outputSize];
            for (int i=0; i<layer.outputSize; i++){
                for (int j=0; j<layer.inputSize; j++){
                    W[i][j] = layer.layer[i].weights[j];
                }
                G[i] = layer.layer[i].neuronGradient;
            }
        }

        TempObject(int outputSize, int intputSize, double grad){
            double[][] W = new double[outputSize][intputSize];
            double[] G = new double[outputSize];
            for (int i=0; i<outputSize; i++){
                for (int j=0; j<intputSize; j++){
                    W[i][j] = 1.0d;
                }
                G[i] = grad;
            }

        }

    }

    class Layer{

        Neuron[] layer;
        private int inputSize;
        private int outputSize;

        Layer(int inputSize, int outputSize){
            this.inputSize = inputSize;
            this.outputSize = outputSize;
            this.layer = new Neuron[outputSize];
            for (int i=0; i<this.outputSize; i++){
                this.layer[i] = new Neuron(this.inputSize);
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

        TempObject gradient(TempObject prevLayer){
            double gradientSum = 0.0;
            for (int i=0; i<this.outputSize; i++){
                for (int j=0; j<this.inputSize; j++){
                    gradientSum += CustomMath.relu_derivative(this.layer[i].output)*prevLayer.W[i][j]*prevLayer.G[i];
                }
                this.layer[i].neuronGradient += gradientSum;
                gradientSum = 0.0;
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
                    this.layer[i].gradients[j] = 0.0d;
                }
                this.layer[i].neuronGradient = 0.0d;
            }
        }

        void backPropigate(double a, double loss){
            for (int i=0; i<outputSize; i++){
                for (int j=0; j<inputSize; j++){
                    this.layer[i].weights[j] -= a*loss*this.layer[i].gradients[j];
                }
            }
        }
    }

    Layer[] Net;
    NeuralNetwork(int depth, int[][] size){
        this.Net = new Layer[depth];
        for(int i=0; i<size.length; i++){
            this.Net[i] = new Layer(size[i][0], size[i][1]);
        }
    }

    NeuralNetwork(int depth, int width){
        this.Net = new Layer[depth];
        for (int i=0; i<depth; i++){
            this.Net[i] = new Layer(width, width);
        }
    }

    double[] feedForward(double[] input){
        double[] activations = Arrays.copyOf(input, input.length);
        double[] temp = new double[1];
        for (int i=0; i<this.Net.length; i++){
            temp = this.Net[i].feedForward(activations);
            activations = temp;
        }
        return activations;
    }

    void gradient(TempObject output){
        TempObject result = output;
        TempObject temp;
        for (int i=0; i<this.Net.length; i++){
            temp = this.Net[i].gradient(result);
            result = temp;
        }
    }

    void zeroGradient(){
        for (int i=0; i<this.Net.length; i++){
            this.Net[i].zeroGradient();
        }
    }

    void backPropigate(double a, double loss){
        for (int i=0; i<this.Net.length; i++){
            this.Net[i].backPropigate(a, loss);
        }
    }
}

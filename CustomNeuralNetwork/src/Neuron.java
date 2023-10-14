import java.util.Arrays;
import java.util.Random;

public class Neuron {
    private double[] weights;
    private double bias;
    private double[] multipliers;
    private double activasion;
    private double activation_grad;
    private double[] gradients;
    int size;

    public Neuron(int size){
        this.size = size;
        double r = Math.sqrt(size);
        Random rand = new Random();
        this.weights = new double[size];
        this.gradients = new double[size];
        for(int i=0; i<this.weights.length; i++){
            this.weights[i] = rand.nextDouble();
            this.gradients[i] = 0.0d;
        }
        this.bias = rand.nextDouble();
        this.multipliers = new double[size];
        this.activasion = 0.0d;
        this.activation_grad = 0.0d;

    }
    public static double relu(double x){return x>0? x:x*.1;}
    public static double relu_derivative(double x){return x>0? 1:.1;}
    public static double sum(double[] array){
        double result = 0.0;
        for (int i=0; i<array.length; i++){
            result+=array[i];
        }
        return result;
    }

    public double dot(double[] input){
        double result = 0.0;
        for (int i=0; i<input.length; i++){
            result += input[i]*this.weights[i];
        }
        return result;
    }

    public double feedForward(double[] input){
        double dot_p = this.dot(input);
        this.multipliers = input;
        double actv = relu(dot_p+this.bias);
        this.activasion = actv;
        return actv;
    }

    public double gradient(double[] prev_grad){
        double new_grad = sum(prev_grad) * this.activasion;
        this.activation_grad = new_grad;
        return new_grad;
    }

    public void back_propigate(double a, double loss){
        Random rand = new Random();
        for (int i=0; i<this.weights.length; i++){
            this.weights[i] -= a*loss*relu_derivative(this.activation_grad)*this.multipliers[i];
            if (Double.isNaN(this.weights[i]) || Double.isInfinite(this.weights[i])) {
                this.weights[i] = rand.nextDouble();
            }
        }
        this.bias -= a*loss*this.activation_grad;
        if (Double.isNaN(this.bias) || Double.isInfinite(this.bias)){
            this.bias = rand.nextDouble();
        }
    }

    public void setMultipliers(double [] array){this.multipliers = array;}
    public double[] getMultiplers(){return this.multipliers;}
    public void setActivation_grad(double num){this.activation_grad = num;}
    public void setGradients(double[] grads){this.gradients = grads;}
    public void setActivasion(double num){this.activasion = num;}
    public double getActivasion(){return this.activasion;}

    @Override
    public String toString(){return Arrays.toString(this.weights)+" "+Double.toString(this.bias);}

}

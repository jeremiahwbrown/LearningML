import static java.lang.Math.abs;

public class NeuralNetwork {
    int size;
    int depth;
    Layer[] net;
    double[][] X_train;
    double [] y;
    boolean isCategorical;
    Layer outputLayer;

    public static double relu(double x){return x>0? x:x*.1;}
    public static double relu_derivative(double x){return x>0? 1:.1;}
    public NeuralNetwork(int size, int depth, double[][] X_train, double[] y, boolean isCategorical){
        this.size = size;
        this.depth = depth;
        this.net = new Layer[depth];
        for (int i=0; i<this.net.length; i++){
            this.net[i] = new Layer(this.size);
        }
        this.X_train = X_train;
        this.y = y;
        this.isCategorical = isCategorical;
        this.outputLayer = new Layer(this.size);
    }

    public static double[] softmax(double[] array){
        double exp_sum = 0.0;
        for (int i=0; i<array.length; i++){
            exp_sum += Math.exp(array[i]);
        }
        double[] exponents = new double[array.length];
        for (int i=0; i<exponents.length; i++){
            exponents[i] = Math.exp(array[i])/exp_sum;
        }
        return exponents;
    }
    public static double sum(double[] array){
        double result = 0.0;
        for (int i=0; i<array.length; i++){
            result+=array[i];
        }
        return result;
    }

    public double feedForward(double[] input){
        double[] activations = input;
        double[] temp;
        for (int i=0; i<this.depth; i++){
            temp = this.net[i].feedForward(activations);
            activations = temp;
        }
        return sum(activations);
    }

    public double[] cat_feedForward(double[] input){
        double[] activations = input;
        double[] temp;
        for (int i=0; i<this.depth; i++){
            temp = this.net[i].feedForward(activations);
            activations = temp;
        }
        for (int i=0; i<this.size; i++){
            this.outputLayer.layer[i].setMultipliers(activations);
        }
        double[] output = softmax(activations);
        for (int i=0; i<this.size; i++){
            this.outputLayer.layer[i].setActivasion(output[i]);
        }
        return output;
    }

    public void gradient(double y){
        double[] prev_grad = new double[this.size];
        for (int i=0; i<this.size; i++){
            double grad = (this.net[this.depth-1].layer[i].getActivasion() - y);
            prev_grad[i] = grad;
            this.net[this.depth-1].layer[i].setActivation_grad(grad);
        }
        double[] temp;
        for (int i=this.depth-2; i>=0; i--){
            temp = this.net[i].gradient(prev_grad);
            prev_grad = temp;
        }
    }

    public void cat_gradient(){

    }

    public void back_propigate(double a, double loss){
        for (int i=0; i<this.depth; i++) {
            this.net[i].back_propigate(a, loss);
        }
    }

    public void cat_back_propigate(double a, double loss){
//        double[] prev_grad = new double[this.size];
//        double grad;
//        double[] mult;
//        for (int i=0; i<this.size; i++){
//            for (int j=0; j<this.size; j++){
//                mult = this.outputLayer.layer[i].getMultiplers();
//                if (i==j){
//                    grad = this.outputLayer.layer[i].getActivasion()*(1 - mult[j]);
//                } else {
//                    grad = -1*this.outputLayer.layer[i].getActivasion()*mult[j];
//                }
//
//            }
//
//
//        }
    }

    public void zero_grad(){
        for(int i=0; i<this.outputLayer.layer.length; i++){
            this.outputLayer.layer[i].setGradients(new double[this.outputLayer.layer[i].size]);
        }
        double[] grads = new double[this.size];
        for (int i=0; i<this.depth; i++){
            for (int j=0; j<this.size; j++){
                this.net[i].layer[j].setGradients(grads);
            }
        }
    }


    public double cost(){
        double loss = 0.0;
        double est = 0.0;
        for (int i=0; i<this.X_train.length; i++){
            est = this.feedForward(this.X_train[i]);
            loss += Math.pow((est - this.y[i]), 2);
        }
        return loss/X_train.length;
    }


    public void fit(){
        double cost = 100000000.0;
        double loss = 0.0;
        double a = 4e-3;
        int increment = 0;
        for (int i=0; i<5; i++){
            while (abs(cost)>1e-2 & increment<=20000){
                for (int j=0; j<this.X_train.length; j++){
                    loss = 2*(this.feedForward(X_train[j])-this.y[j]);
                    this.gradient(this.y[i]);
                    this.back_propigate(a, loss);
                }
                //System.out.println("Increment: "+increment);
                increment++;
                cost = this.cost();
//                System.out.println("a = "+a);
//                this.printDifference();
//                System.out.println();
            }
            if (abs(cost)<=1e-2){
                System.out.println("Min cost reached at a="+a+" cost="+cost+ " increment="+increment +"\n");
                break;
            }
            increment = 0;
            a = a/10.0;
        }
    }

    public void printNet(){
        for (int i=0; i<this.depth; i++){
            this.net[i].printLayer();
            System.out.println();
        }
    }

    public double[] predictions(){
        double[] result = new double[this.X_train.length];
        for (int i=0; i<this.X_train.length; i++){
            result[i] = this.feedForward(X_train[i]);
        }
        return result;
    }

    public void printDifference(){
        for(int i=0; i<X_train.length; i++){
            System.out.println("Real: "+this.y[i]+" "+"Estimate: "+this.feedForward(X_train[i]));
        }
    }

}

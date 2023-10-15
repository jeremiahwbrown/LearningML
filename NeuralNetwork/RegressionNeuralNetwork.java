public class RegressionNeuralNetwork{
    NeuralNetwork network;
    double gradient;
    double[][] x_data;
    double[] y_data;
    double g;

    public RegressionNeuralNetwork(int depth, int[][] size, double[][] x, double[] y){
        this.x_data = x;
        this.y_data = y;
        this.gradient = 0.0d;
        this.network = new NeuralNetwork(depth, size);
        //int tempSize = this.network.Net[this.network.Net.length-1].getOutputSize();
        this.g = 0.9;
    }

    public RegressionNeuralNetwork(int depth, int width, double[][] x, double[] y){
        this.x_data = x;
        this.y_data = y;
        this.network = new NeuralNetwork(depth, width);
        //int tempSize = this.network.Net[this.network.Net.length-1].getOutputSize();
    }

    //public void setMomenumt(double x){this.g=x;}

    public double feedForward(double[] input){
        double[] out = this.network.feedForward(input);
        double result = CustomMath.sum(out);
        return result;
    }

    public void gradient(double loss){
        this.gradient = loss;
        NeuralNetwork.TempObject temp = new NeuralNetwork.TempObject();
        this.network.gradient(temp, loss, true);
    }

    public void zeroGradient(){
        this.gradient = 0.0d;
        this.network.zeroGradient();
    }

    public void backPropigate(double a){
        this.network.backPropigate(a, 1, (this.g/this.x_data.length));
        //this.network.backPropigate(a, (this.g/this.x_data.length), (this.g/this.x_data.length));
    }

    public double epoch(double a){
        this.zeroGradient();
        double loss = 0.0;
        double cost=0.0;
        for (int i=0; i<this.x_data.length; i++){
            loss = this.feedForward(x_data[i])-this.y_data[i];
            this.gradient(loss);
            cost+=Math.pow(loss, 2.0);
        }
        this.backPropigate(a);
        return cost/this.x_data.length;
    }

    public double cost(){
        double loss = 0.0;
        double cost=0.0;
        for (int i=0; i<this.x_data.length; i++){
            loss = this.feedForward(x_data[i])-this.y_data[i];
            cost+=Math.pow(loss, 2.0);
        }
        return cost/this.x_data.length;
    }

    public double[] predictions(){
        double[] result = new double[y_data.length];
        for (int i=0; i<y_data.length; i++){
            result[i] = this.feedForward(x_data[i]);
        }
        return result;
    }

    void print(){this.network.print();}

    void printGradients(){this.network.printGradients();}
    public void fit(double a){
        double cost=this.cost();
        int increment = 0;
        int max_iterations=20000;
        double temp = 0.0;
        System.out.println("Initial cost="+cost);

        whole:
        for (int i=0; i<10; i++){
            while(increment<=max_iterations){
                temp = cost;
                cost = this.epoch(a);
                if(Double.isNaN(cost) || Double.isInfinite(cost)){
                    break whole;
                }
                if(temp-cost<0 || Math.abs(temp-cost)<1e-9){
                    break;
                }
                increment+=1;
            }
            System.out.println("Increments="+increment+" cost="+cost+" a="+a+"\n");
            //this.printGradients();
            //this.print();
            if (increment>=max_iterations || Math.abs(temp-cost)<=1e-9){
                increment=0;
            } else {
                //break;
                a/=10;
                increment=0;
            }
        }

    }
}

public class RegressionNeuralNetwork{
    NeuralNetwork network;
    double gradient;
    double[][] x_data;
    double[] y_data;
    double a;
    public RegressionNeuralNetwork(int depth, int[][] size, double[][] x, double[] y, double eta){
        this.x_data = x;
        this.y_data = y;
        this.gradient = 0.0d;
        this.network = new NeuralNetwork(depth, size);
        int tempSize = this.network.Net[this.network.Net.length-1].getOutputSize();
        this.a = eta;
    }

    public RegressionNeuralNetwork(int depth, int width, double[][] x, double[] y, double eta){
        this.x_data = x;
        this.y_data = y;
        this.network = new NeuralNetwork(depth, width);
        int tempSize = this.network.Net[this.network.Net.length-1].getOutputSize();
        this.a = eta;
    }

    public double feedForward(double[] input){
        double[] out = this.network.feedForward(input);
        double result = CustomMath.sum(out);
        return result;
    }

    public void gradient(double loss){
        this.gradient += loss;
        NeuralNetwork.TempObject temp = new NeuralNetwork.TempObject(this.network.Net[this.network.Net.length-1].getOutputSize(),
            this.network.Net[this.network.Net.length-1].getInputSize(), this.gradient);
        this.network.gradient(temp);
    }

    public void zeroGradient(){
        this.gradient = 0.0d;
        this.network.zeroGradient();
    }

    public void backPropigate(double loss){
        this.network.backPropigate(this.a, loss);
    }

    public void epoch(){
        
    }
}

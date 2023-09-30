public class Layer {
    Neuron[] layer;

    public Layer(int size){
        this.layer = new Neuron[size];
        for(int i=0; i<size; i++){
            this.layer[i] = new Neuron(size);
        }
    }

    public double[] feedForward(double[] prev_activations){
        double[] result = new double[this.layer.length];
        for (int i=0; i<this.layer.length; i++){
            result[i] = this.layer[i].feedForward(prev_activations);
        }
        return result;
    }

    public double[] gradient(double[] prev_grad){
        double[] result = new double[this.layer.length];
        for (int i=0; i<result.length; i++){
            result[i] = this.layer[i].gradient(prev_grad);
        }
        return result;
    }

    public void back_propigate(double a, double loss){
        for (int i=0; i<this.layer.length; i++){
            this.layer[i].back_propigate(a, loss);
        }
    }

    public void printLayer(){
        for(int i=0; i<this.layer.length; i++){
            System.out.println(this.layer[i]);
        }
    }

}

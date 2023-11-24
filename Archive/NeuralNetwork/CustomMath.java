public class CustomMath {

    public static double relu(double x){return x>0? x:x*.1;}
    public static double relu_derivative(double x){return x>0? 1:.1;}
    public static double sum(double[] array){
        double result = 0.0;
        for (int i=0; i<array.length; i++){
            result+=array[i];
        }
        return result;
    }

    public static double dotProduct(double[] a, double[] b){
        if(a.length!=b.length){
            throw new IllegalArgumentException("Array sizes not equal!");
        }
        double result = 0.0;
        for (int i=0; i<a.length; i++){
            result += a[i]*b[i];
        }
        return result;
    }
}

/**
 * Created by kuang048 on 11/27/17.
 */
public class point {
    double[] value;
    cluster cluster;
    String label;

    public point(double[] value){
        this.value = value;
    }
    public point(double[] value,String label){
        this.value = value;
        this.label = label;
    }
    public point(double[] value,cluster c){
        this.value = value;
        this.cluster = c;
    }
    public double distance_from(point p2, String func){
        double distance = 0;
        if (func.equals("SSE")){
            for (int d = 0; d < value.length; d++){
                distance = distance + Math.pow(this.value[d] - p2.value[d],2);
            }
            distance = Math.sqrt(distance);
        }
        else if (func.equals("I2")){
            //TODO
        }
        else if (func.equals("E1")){

        }
        return distance;
    }
    public void setValue(double[] value){
        this.value = value;
    }
}

/**
 * Created by kuang048 on 11/27/17.
 */
public class point {
    double[] value;
    cluster cluster;

    public double distance_from(point p2, String func){
        double distance = 0;
        if (func.equals("SSE")){
            for (int d = 0; d < value.length; d++){
                distance = distance + Math.pow(this.value[d] - p2.value[d],2);
            }
        }
        else if (func.equals("I2")){

        }
        else if (func.equals("E1")){

        }
        return distance;
    }
}

/**
 * Created by kuang048 on 11/27/17.
 */
public class point {
    SparseVector value;
    cluster cluster;
    String label;

    public point(SparseVector value){
        this.value = value;
    }
    public point(SparseVector value,String label){
        this.value = value;
        this.label = label;
    }
    public point(SparseVector value,cluster c){
        this.value = value;
        this.cluster = c;
    }
    public double distance_from(point p2, String func){
        double distance = 0;
        if (func.equals("SSE")){
            distance = this.value.euclidean_dist(p2.value);
        }
        else if (func.equals("I2")){
            //TODO
        }
        else if (func.equals("E1")){

        }
        return distance;
    }
    public void setValue(SparseVector value){
        this.value = value;
    }
}

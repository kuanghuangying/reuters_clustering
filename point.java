/**
 * Created by kuang048 on 11/27/17.
 */
public class point {
    SparseVector value;
    int cluster_id;
    String label;

    public point(SparseVector value){
        this.value = value;
    }
    public point(SparseVector value,String label){
        this.value = value;
        this.label = label;
    }
    public point(SparseVector value, int cluster_id){
        this.value = value;
        this.cluster_id = cluster_id;
    }
    public double distance_from(SparseVector c2, String func){
        double distance = 0;
        if (func.equals("SSE")){
            distance = this.value.euclidean_dist(c2);
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

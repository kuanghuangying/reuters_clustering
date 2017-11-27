/**
 * Created by kuang048 on 11/27/17.
 */
public class kmeans {
    point[] points;
    String[] class_label;
    int n_cluster;
    String critFunc;
    int seed;

    public kmeans(point[] points, String[] class_label, int n_cluster, String critFunc, int seed){
        this.points = points;
        this.class_label = class_label;
        this.n_cluster = n_cluster;
        this.critFunc = critFunc;
        this.seed = seed;
    }

    public void initialize_centroid(){

    }
}

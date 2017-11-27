import java.util.Random;

/**
 * Created by kuang048 on 11/27/17.
 */
public class kmeans {
    point[] points;
    String[] class_label;
    int n_cluster;
    String critFunc;
    int seed;
    int dimension;

    public kmeans(point[] points, String[] class_label, int n_cluster, String critFunc, int seed){
        this.points = points;
        this.class_label = class_label;
        this.n_cluster = n_cluster;
        this.critFunc = critFunc;
        this.seed = seed;
        this.dimension = points.length;
    }

    public void initialize_centroid(){
        for (int i = 0; i < n_cluster; i++){

            double[] val = new double[dimension];
            Random generator = new Random(seed);
            for (int d = 0; d < dimension; d++){
                double num = generator.nextDouble();
                val[d] = num;
                System.out.println(num);
            }
            point centroid = new point(val);
            cluster new_cluster = new cluster(i,centroid);
        }
    }
}

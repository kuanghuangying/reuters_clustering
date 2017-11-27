
import java.util.Map;
import java.util.Random;



/**
 * Created by kuang048 on 11/27/17.
 */
public class kmeans {
    point[] points;
    Map<String,String> class_label;
    cluster[] clusters;
    int n_cluster;
    String critFunc;
    int seed;
    int dimension;
    int point_num;

    public kmeans(point[] points, Map<String,String> class_label, int n_cluster, String critFunc, int seed){
        this.points = points;
        this.class_label = class_label;
        this.n_cluster = n_cluster;
        this.critFunc = critFunc;
        this.seed = seed;
        this.dimension = points.length;
        this.clusters = new cluster[n_cluster];
        this.point_num = class_label.size();
    }

    public void initialize_centroid(){
        for (int i = 0; i < n_cluster; i++){
            Random generator = new Random(seed);
            int num = (point_num-1)*generator.nextInt();
            System.out.println(num);
            point centroid = points[num];
            cluster new_cluster = new cluster(i,centroid);
            centroid.cluster = new_cluster;
            clusters[i] = new_cluster;
            System.out.format("cluster id: %d",i);
            System.out.println();
        }
        System.out.println("finished initializing centroids");
    }

    public void assign_cluster(){
        for (point p : points){
            //calculate distance/similarity with all centroids
            double closest;
            switch (critFunc){
                case ("SSE"):
                    closest = Double.MAX_VALUE;
                    for (cluster cluster : clusters){
                        double distance = p.distance_from(cluster.centroid, critFunc);
                        if (distance < closest) {
                            closest = distance;
                            p.cluster = cluster;
                        }
                    }
                case ("I2"):
                    closest = Double.MIN_VALUE;
                    for (cluster cluster : clusters){
                        double similarity = p.distance_from(cluster.centroid, critFunc);
                        if (similarity > closest) {
                            closest = similarity;
                            p.cluster = cluster;
                        }
                    }
                case ("E1"):
                    closest = Double.MIN_VALUE;
                    for (cluster cluster : clusters){
                        double similarity = p.distance_from(cluster.centroid, critFunc);
                        if (similarity > closest) {
                            closest = similarity;
                            p.cluster = cluster;
                        }
                    }
            }
        }
        System.out.println(points[0].label + " belongs to " + points[0].cluster.cluster_id);
        System.out.println(points[1].label + " belongs to " + points[1].cluster.cluster_id);
    }
    
    /* update centroid?
                double sum_vec = 0.0;
            for (double x : val) sum_vec += x;
            for (int k = 0; k < val.length;k++){
                val[k] = val[k]/sum_vec;
                //System.out.print(String.valueOf(val[k])+ ",");
            }
     */
}

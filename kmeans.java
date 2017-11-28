
import java.util.*;


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
    double criterionVal = Double.MAX_VALUE;

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

    public void initialize_centroid(int trial_id){
        Random generator = new Random(seed);
        for (int i = 0; i < n_cluster; i++){
            int num = generator.nextInt(point_num-1);
            point centroid = points[num];
            cluster new_cluster = new cluster(i,centroid);
            points[num].cluster_id = i;
            clusters[i] = new_cluster;
//            System.out.format("cluster id: %d",i);
//            System.out.println();
        }
        System.out.println("finished initializing centroids for trial " + String.valueOf(trial_id));
    }

    public void update_cluster(point p, int c_id){
        int original_id = p.cluster_id;
        clusters[c_id].points.remove(p);
        p.cluster_id = c_id;
        clusters[c_id].points.add(p);
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
                            update_cluster(p,cluster.cluster_id);
                        }
                    }
                case ("I2"):
                    closest = Double.MIN_VALUE;
                    for (cluster cluster : clusters){
                        double similarity = p.distance_from(cluster.centroid, critFunc);
                        if (similarity > closest) {
                            closest = similarity;
                            update_cluster(p,cluster.cluster_id);
                        }
                    }
                case ("E1"):
                    closest = Double.MIN_VALUE;
                    for (cluster cluster : clusters){
                        double similarity = p.distance_from(cluster.centroid, critFunc);
                        if (similarity > closest) {
                            closest = similarity;
                            update_cluster(p,cluster.cluster_id);
                        }
                    }
            }
            //System.out.println(p.label + " belongs to " + p.cluster_id);
        }
        //System.out.println(points[0].label + " belongs to " + points[0].cluster.cluster_id);
        //System.out.println(points[1].label + " belongs to " + points[1].cluster.cluster_id);
    }

    public void update_centroid(){
        //detect empty cluster
        int empty_cluster = 0;
        List<Integer> cluster_size = new ArrayList<Integer>(n_cluster);
        for (int cid = 0; cid < n_cluster; cid++) {
            if (clusters[cid].getSize() == 0) {
                empty_cluster++;
            }
            cluster_size.add(clusters[cid].getSize());
            //System.out.println(clusters[cid].toString());
        }
        if(empty_cluster != 0){
            System.out.format("empty cluster number: %d \n",empty_cluster);
        }

        //becasue there hasn't been issue with empty cluster, pending implementation later
        //for each empty cluster, we need to find a large cluster to split into two
//        for (int a = 0; a < empty_cluster; a++){
//            int max_cluster_id = cluster_size.indexOf(Collections.max(cluster_size));
//            // split
//
//            //update cluster_size
//        }
        SparseVector[] new_centr_list = new SparseVector[n_cluster];
        for (int j = 0; j < n_cluster; j ++){
            new_centr_list[j] = new SparseVector(dimension);
        }
        for (int i = 0; i < points.length; i++) {
            int c_id = points[i].cluster_id;
            for (int index : points[i].value.map.keySet()){
                if (new_centr_list[c_id].get(index) == 0.0) {
                    new_centr_list[c_id].put(index, points[i].value.map.get(index));
                }
                else{
                    new_centr_list[c_id].put(index, points[i].value.map.get(index) + new_centr_list[c_id].get(index));
                }
            }
        }

        // take the average of each dimension to produce the new centroid
        for (int id = 0; id < n_cluster; id++){
            SparseVector new_centr = new_centr_list[id];
            if(clusters[id].getSize() == 0){
                empty_cluster ++;
            }
            else {
                double sc_factor = 1.0/((double) clusters[id].getSize());
                new_centr = new_centr.scale(sc_factor);
            }
            clusters[id].centroid = new_centr;
            //System.out.format("updated new centroid: %d \n",id);
        }

    }
    public void update_Criterion() {
        double new_critirion = 0.0;
        switch (critFunc) {
            case ("SSE"):
                for (int i = 0; i < points.length; i++) {
                    int c_id = points[i].cluster_id;
                    double distance = points[i].distance_from(clusters[c_id].centroid, critFunc);
                    new_critirion += (distance * distance);
                }
        }
        criterionVal = new_critirion;
    }

    public boolean converge(double threshold){
        double old_critirion = criterionVal;
        update_Criterion();
        if (Math.abs(criterionVal-old_critirion) <= threshold) {
            return true;
        }
        else {
            //System.out.println((Math.abs(criterionVal-old_critirion)));
            return false;
        }
    }
}

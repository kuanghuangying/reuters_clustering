import java.util.HashSet;
import java.util.Set;

/**
 * Created by kuang048 on 11/27/17.
 */
public class cluster {
    int cluster_id;
    SparseVector centroid;
    private int size;
    Set<point> points;

    public cluster(int cluster_id, point centroid){
        this.cluster_id = cluster_id;
        this.centroid = centroid.value;
        points = new HashSet<point>();
        points.add(centroid);
        size = points.size();
    }
    public int getSize(){
        return points.size();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (point p : points) {
            s.append("point " + p.label + " in cluster " + cluster_id + "\n");
        }
        return s.toString();
    }
}

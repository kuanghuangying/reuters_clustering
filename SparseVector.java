import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by kuang048 on 11/27/17.
 * Reference: https://introcs.cs.princeton.edu/java/44map/SparseVector.java.html
 */
public class SparseVector {
    int len;
    Map<Integer, Double> map ;

    public SparseVector(int len) {
        this.len  = len;
        this.map = new LinkedHashMap<>();
    }

    public void put(int i, double value) {
        if (i < 0 || i > len) throw new RuntimeException("Illegal index");
        if (value == 0.0) map.remove(i);
        else              map.put(i, value);
    }
    public double get(int i) {
        if (i < 0 || i > len) throw new RuntimeException("Illegal index");
        if (map.containsKey(i)) return map.get(i);
        else                return 0.0;
    }

    public double dot(SparseVector that) {
        if (this.len != that.len) throw new IllegalArgumentException("Vector lengths disagree");
        double sum = 0.0;

        // iterate over the vector with the fewemap nonzeros
        if (this.map.size() <= that.map.size()) {
            for (int i : this.map.keySet())
                if (that.map.containsKey(i)) sum += this.get(i) * that.get(i);
        }
        else  {
            for (int i : that.map.keySet())
                if (this.map.containsKey(i)) sum += this.get(i) * that.get(i);
        }
        return sum;
    }
    
    public double norm() {
        return Math.sqrt(this.dot(this));
    }

    public SparseVector scale(double alpha) {
        SparseVector result = new SparseVector(len);
        for (int i : this.map.keySet())
            result.put(i, alpha * this.get(i));
        return result;
    }

    public double euclidean_dist(SparseVector that) {
        // iterate over the vector with the fewemap nonzeros
        double sum = 0.0;
        Set<Integer> added_index = new HashSet<>();
        for (int i : this.map.keySet()){
            if (that.map.containsKey(i)) sum += Math.pow(this.get(i) - that.get(i), 2);
            else sum += this.get(i) * this.get(i);
            added_index.add(i);
        }
        for (int i : that.map.keySet()){
            if (!added_index.contains(i)) {
                if (this.map.containsKey(i)) sum += Math.pow(this.get(i) - that.get(i), 2);
                else sum += that.get(i) * that.get(i);
            }
        }
        return Math.sqrt(sum);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i : map.keySet()) {
            s.append("(" + i + ", " + map.get(i) + ") ");
        }
        return s.toString();
    }

}

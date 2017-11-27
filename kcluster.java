import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by kuang048 on 11/26/17.
 */
public class kcluster{

    private static String critFunc;
    private static List<String> inFile;
    private static List<String> classFile;
    private static int n_cluster;
    private static int n_trial;
    private static String outFileName;
    private static int number_point;
    private static int dimension;
    private static final int[] seeds = {1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39};

    private  void start(){

    }
    private static point[] getAllPoints(){
        point[] points = new point[number_point];
        String[] content;
        String id = "";
        int dim = 0;
        double v;
        int point_counter = 0;
        boolean first_line =true;
        for (String id_dim_val : inFile){
            if(first_line){
                content = id_dim_val.split(",");
                id = content[0];
                dim = Integer.valueOf(content[1]);
                v = Double.valueOf(content[2]);
                points[point_counter] = new point(new double[dimension+1]);
                points[point_counter].value[dim] = v;
                points[point_counter].label = id;
            }
            else {
                String[] new_content = id_dim_val.split(",");
                String current_id = new_content[0];
                int current_dim = Integer.valueOf(new_content[1]);
                double current_v = Double.valueOf(new_content[2]);

                //same id
                if(current_id.equals(id)){
                    points[point_counter].value[current_dim] = current_v;
                }
                //different id
                else{
                    point_counter ++;
                    points[point_counter] = new point(new double[dimension+1]);
                    points[point_counter].value[current_dim] = current_v;
                    points[point_counter].label = current_id;
                }
            }

        }
        return points;
    }

    public static void main(String[] args) throws IOException {
        //kcluster input-file criterion-function class-file #clusters #trials output-file
        //kcluster.java "freq.csv" SSE "reuters21578.class" 20 20 "out.txt"
        inFile = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
        critFunc = String.valueOf(args[1]);
        classFile = Files.readAllLines(Paths.get(args[2]),StandardCharsets.UTF_8);
        n_cluster = Integer.valueOf(args[3]);
        n_trial = Integer.valueOf(args[4]);
        outFileName = String.valueOf(args[5]);
        number_point = classFile.size();
        dimension = Files.readAllLines(Paths.get("reuters21578.clabel"),StandardCharsets.UTF_8).size();
        long startTime = System.currentTimeMillis();

        point[] points = getAllPoints();

        for(int i = 0; i < points.length;i++){
            point p = points[i];
            System.out.println(String.valueOf(i) + " " + p.value.toString());
            //TODO
        }
        for (int seed : seeds){
            //kmeans trial = new kmeans(inFile, classFile, n_cluster, critFunc, seed);
        }


        //load from inFile to data points


        /*
        *  write
        */
//        FileWriter ruleWriter = new FileWriter(.txt");
//
//        ruleWriter.flush();
//        ruleWriter.close();




        // reporting time
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;

      //cluster.buildReport(totalTime);
      //System.out.println("Total execution time: " + Double.toString(totalTime/ 1000.0) + " seconds");
  //        String jsonString = new JSONObject()
  //                .put("sup", cluster.minsup)
  //                .put("file","" + args[2])
  //                .put("itemset_time", Double.toString(totalTime/ 1000.0) + " seconds")
  //                .put("num_patterns", cluster.itemSetFrequencies.size()).toString();
  //        System.out.println(jsonString);



  }


}

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by kuang048 on 11/26/17.
 */
public class kcluster{

    static String critFunc;
    static List<String> inFile;
    static List<String> classFile;
    static int n_cluster;
    static int n_trial;
    static String outFileName;
    static final int[] seeds = {1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39};

    public void start(){

    }

    public static void main(String[] args) throws IOException {
        //kcluster input-file criterion-function class-file #clusters #trials output-file
        inFile = Files.readAllLines(Paths.get(args[0]));
        critFunc = String.valueOf(args[1]);
        classFile = Files.readAllLines(Paths.get(args[2]));
        n_cluster = Integer.valueOf(args[3]);
        n_trial = Integer.valueOf(args[4]);
        outFileName = String.valueOf(args[5]);
        long startTime = System.currentTimeMillis();


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

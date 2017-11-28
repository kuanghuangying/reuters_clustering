import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by kuang048 on 11/26/17.
 */
public class kcluster{

    private static String critFunc;
    private static List<String> inFile;
    private static List<String> classFile;
    private static int n_cluster;
    private static int n_trial;
    private static String inFIleName;
    private static String outFileName;
    private static int number_point;
    private static int dimension;
    private static Map<String,String> id_label_map;
    private static Map<String,Integer> topic_col_map;
    private static final int[] seeds = {1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39};
    static String deliminator = ",";
    static String newline = "\n";


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
                points[point_counter] = new point(new SparseVector(dimension));
                points[point_counter].value.put(dim,v);
                points[point_counter].topic_id = topic_col_map.get(id_label_map.get(id));
                points[point_counter].label = id;
                first_line = false;
            }
            else {
                String[] new_content = id_dim_val.split(",");
                String current_id = new_content[0];
                int current_dim = Integer.valueOf(new_content[1]);
                double current_v = Double.valueOf(new_content[2]);

                //same id
                if(current_id.equals(id)){
                    //System.out.println(current_dim + " -- " + current_v);
                    points[point_counter].value.put(current_dim,current_v);
                }
                //different id
                else{
                    //System.out.println(id_dim_val);
                    point_counter ++;
                    points[point_counter] = new point(new SparseVector(dimension));
                    points[point_counter].value.put(current_dim,current_v);
                    points[point_counter].topic_id = topic_col_map.get(id_label_map.get(current_id));
                    points[point_counter].label = current_id;
                    id = current_id;
                }
            }

        }
        return points;
    }

    private static Map<String,String> getAllLabels(){
        Map<String,String> label_map = new HashMap<>();
        topic_col_map = new HashMap<>();
        String[] content;
        int i = 0;
        for (String id_topic : classFile){
            content = id_topic.split(",");
            String id = content[0];
            String topic = content[1];
            label_map.put(id,topic);
            if (!topic_col_map.containsKey(topic)){
                topic_col_map.put(topic,i);
                i++;
            }
        }
        return label_map;
    }

    private static void write_evaluation(double best_criterion, long totalTime) throws IOException{
        PrintWriter report = new PrintWriter(new FileWriter("evaluation.csv",true));
        StringBuilder sb = new StringBuilder();
        sb.append(critFunc);
        sb.append(deliminator);
        sb.append(n_cluster);
        sb.append(deliminator);
        sb.append(best_criterion);
        sb.append(deliminator);
        sb.append(totalTime);
        sb.append(newline);

        report.write(sb.toString());
        report.close();
    }

    private static void write_output_file(kmeans best_trial) throws IOException{

        PrintWriter report = new PrintWriter(new FileWriter(outFileName));

        StringBuilder sb = new StringBuilder();
        for (point p : best_trial.points){
            sb.append(p.label);
            sb.append(deliminator);
            sb.append(p.cluster_id);
            sb.append(newline);
        }
        report.write(sb.toString());
        report.close();
    }
    private static int[][] get_distribution(kmeans best_trial){
        int[][] distribution = new int[best_trial.clusters.length][20];
        for (int c_id = 0; c_id < distribution.length;c_id++){
            for (point p : best_trial.clusters[c_id].points){
                int topic_id = p.topic_id;
                distribution[c_id][topic_id] ++;
            }
        }
        return distribution;
    }

    private static void write_distribution(int[][] distribution) throws IOException{

        PrintWriter report = new PrintWriter(new FileWriter(inFIleName.substring(0,inFIleName.indexOf(".")) +critFunc+n_cluster+".txt"));
        StringBuilder sb = new StringBuilder();

        for (int[] cluster: distribution){

            sb.append(p.label);
            sb.append(deliminator);
            sb.append(p.cluster_id);
            sb.append(newline);
        }
        report.write(sb.toString());
        report.close();
    }


    public static void main(String[] args) throws IOException {
        //kcluster input-file criterion-function class-file #clusters #trials output-file
        //java kcluster "freq.csv" SSE "reuters21578.class" 20 20 "out.txt"
        inFile = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
        inFIleName = String.valueOf(args[0]);
        critFunc = String.valueOf(args[1]);
        classFile = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);
        n_cluster = Integer.valueOf(args[3]);
        n_trial = Integer.valueOf(args[4]);
        outFileName = String.valueOf(args[5]);
        number_point = classFile.size();
        dimension = Files.readAllLines(Paths.get("reuters21578.clabel"), StandardCharsets.UTF_8).size();
        System.out.println(dimension);


        long startTime = System.currentTimeMillis();

        id_label_map = getAllLabels();
        System.out.println("finish getting labels");

        point[] points = getAllPoints();
        System.out.println("finish getting points");


        int trial_id = 1;
        double threshold = 5;
        double best_criterion = 0;
        switch (critFunc) {
            case ("SSE"):
                best_criterion = Double.MAX_VALUE;
            case ("I2"):
                best_criterion = Double.MIN_VALUE;
            case ("E1"):
                best_criterion = Double.MIN_VALUE;
        }
        kmeans best_trial = null;
        for (int seed : seeds) {
            kmeans trial = new kmeans(points, id_label_map, n_cluster, critFunc, seed);
            trial.initialize_centroid(trial_id);

            int iteration = 0;
            while (true) {
                trial.assign_cluster();
                trial.update_centroid();
                if (trial.converge(threshold) || iteration > 15) {
                    switch (critFunc) {
                        case ("SSE"):
                            if (trial.criterionVal < best_criterion) {
                                best_criterion = trial.criterionVal;
                                best_trial = trial;
                            }
                        case ("I2"):
                            if (trial.criterionVal > best_criterion){
                                best_criterion = trial.criterionVal;
                                best_trial = trial;
                            }
                        case ("E1"):
                            if (trial.criterionVal > best_criterion) {
                                best_criterion = trial.criterionVal;
                                best_trial = trial;
                            }
                    }
                    System.out.println("criterion function value is: " + String.format("%.2f", trial.criterionVal));
                    break;
                }
                //System.out.format("iteration %d \n", iteration);
                iteration++;
            }
            trial_id++;
            break;
        }
        System.out.println("-- best criterion function value is: " + String.format("%.2f", best_criterion));


        /*
        *  write
        */
        // reporting time
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;



        write_evaluation(best_criterion,totalTime);
        write_output_file(best_trial);
        int[][] distribution =   get_distribution(best_trial);
        write_distribution(distribution);



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



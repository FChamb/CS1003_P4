import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class CS1003P4 {
    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Wrong number of command line arguments!");
        }
        try {
            File directory  = new File(args[0]);
            if (!directory.exists()) {
                throw new FileNotFoundException("Directory \"" + args[0] + "\" does not exist!");
            }
            CS1003P4 project = new CS1003P4();
            project.search(directory.getPath(), args[1], Double.parseDouble(args[2]));
        } catch (IllegalArgumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void search(String directory, String searchTerm, double similarity) {
        //System.out.println(directory + "\n" + searchTerm + "\n" + similarity);
        String[] files = new File(directory).list();
        System.out.println(Arrays.toString(files));

        SparkConf conf = new SparkConf().setAppName("SparkPractical").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        //JavaRDD<String> line = sc.textFile(files[0]);
        //System.out.println(line.take(1));
    }
}

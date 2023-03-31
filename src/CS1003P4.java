import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CS1003P4 {
    /**
     * This main method is the starting point for this program. It begins by looking at the command line
     * arguments. Firstly these arguments are checked to see if a total of three exist. An error is produced
     * if not. After that, the directory provided by the user is validated. Should the directory of
     * text files not exist, an error message is produced. The last check is to see if the directory provided
     * actually contains any files. Should it not, the proper error is produced. Finally, a new CS1003P4 object
     * is created and the search method is called from the object using the provided command line arguments.
     * @param args - the command line arguments. An array of string which contain the user's requests
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Wrong number of command line arguments!");
        }
        try {
            File directory  = new File(args[0]);
            if (!directory.exists()) {
                throw new FileNotFoundException("Directory \"" + args[0] + "\" does not exist!");
            }
            if (directory.list() == null || directory.list().length == 0) {
                throw new FileNotFoundException("\"" + directory + "\" does not contain any files!");
            }
            CS1003P4 project = new CS1003P4();
            project.search(directory.getPath(), args[1], Double.parseDouble(args[2]));
        } catch (IllegalArgumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * search is the starting method which begins the process by adding all the text file names from the provided
     * directory into an array. Then the proper Spark configurations are created. First a SparkConf is made and then
     * a JavaSparkContext is created using the SparkConf. A for loop
     * @param directory
     * @param searchTerm
     * @param similarity
     */
    public void search(String directory, String searchTerm, double similarity) {
        SparkConf conf = new SparkConf().setAppName("SparkPractical").setMaster("local[*]");
        Logger.getRootLogger().setLevel(Level.OFF);
        JavaSparkContext sc = new JavaSparkContext(conf);
        HashSet<String> cleanSearch = createBigram(cleanText(searchTerm));
        JavaPairRDD<String, String> wholeText = sc.wholeTextFiles(directory);
        JavaRDD<String> slidingWindow = wholeText.flatMap(x -> {
            String[] search = cleanText(searchTerm).split(" ");
            String[] words = cleanText(x._2()).split(" ");
            String[] window = new String[words.length - search.length + 1];
            for (int i = 0; i < window.length; i++) {
                String substring = "";
                for (int j = 0; j < search.length; j++) {
                    if (j != search.length - 1) {
                        substring += words[i + j] + " ";
                    } else {
                        substring += words[i + j];
                    }
                }
                window[i] = substring;
            }
            return Arrays.asList(window).iterator();
        });
        JavaRDD<String> finalOutput = slidingWindow.filter(data -> calculateJaccard(createBigram(data), cleanSearch) >= similarity);
        finalOutput.foreach(y -> System.out.println(y));
    }

    public static String cleanText(String line) {
        line = line.replaceAll("[^a-zA-Z0-9]+", " ");
        line = line.toLowerCase();
        return line;
    }

    public static HashSet<String> createBigram(String word) {
        HashSet<String> bigram = new HashSet<>();
        for (int i = 0; i < word.length() - 1; i ++) {
            String letter1 = "" + word.charAt(i);
            String letter2 = "" + word.charAt(i + 1);
            String bothLetters = letter1 + letter2;
            bigram.add(bothLetters);
        }
        return bigram;
    }

    public static double calculateJaccard(HashSet<String> set1, HashSet<String> set2) {
        HashSet<String> intersection = new HashSet<>();
        HashSet<String> union = new HashSet<>();
        if (set1.size() > set2.size()) {
            intersection.addAll(set1);
            intersection.retainAll(set2);
        } else {
            intersection.addAll(set2);
            intersection.retainAll(set1);
        }
        set1.addAll(set2);
        union.addAll(set1);
        return (double) intersection.size() / union.size();
    }
}
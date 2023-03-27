import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
        String[] files = new File(directory).list();
        try {
            if (files == null || files.length == 0) {
                throw new FileNotFoundException("\"" + directory + "\" does not contain any files!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SparkConf conf = new SparkConf().setAppName("SparkPractical").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("ERROR");
        //for (String file : files) {
            JavaPairRDD<String, String> wholeText = sc.wholeTextFiles(directory + "/" + files[0]);
            JavaRDD<String> lines = wholeText.flatMap(line -> Arrays.asList(line._2().replaceAll("[^a-zA-z0-9]+", " ").toLowerCase().split("[ \t\n\r]")).iterator());
            List<String> line = lines.collect();
            System.out.println(line);
            //String line = wholeText.take(2).toString();
            //String[] words = splitWords(line);
            //System.out.println(Arrays.toString(words));
            //generateSplits(searchTerm, words);
        //}
    }

    public String[] splitWords(String line) {
        //line = line.substring(line.indexOf(".txt") + 6);
        line = line.replaceAll("[^a-zA-Z0-9]", " ");
        line = line.toLowerCase();
        return line.split("[ \t\n\r]");
    }

    public void generateSplits(String search, String[] words) {
        int length = search.split(" ").length;
        for (int i = 0; i < words.length - 3; i++) {
            String substring = words[i] + " " + words[i + 1] + " " + words[i + 2];
            System.out.println(substring);
        }
    }

    public HashSet<String> createBigram(String word) {
        HashSet<String> bigram = new HashSet<String>();
        for (int i = 0; i < word.length() - 1; i ++) {
            String letter1 = "" + word.charAt(i);
            String letter2 = "" + word.charAt(i + 1);
            String bothLetters = letter1 + letter2;
            bigram.add(bothLetters);
        }
        return bigram;
    }

    public double calculateJaccard(HashSet<String> set1, HashSet<String> set2) {
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

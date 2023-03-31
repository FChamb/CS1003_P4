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

public class CS1003P4 {
    /**
     * This main method is the starting point for this program. It begins by looking at the command line
     * arguments. Firstly these arguments are checked to see if a total of three exist. An error is produced
     * if not. After that, the directory provided by the user is validated. Should the directory of
     * text files not exist, an error message is produced. A check then sees if the directory provided
     * actually contains any files. Should it not, the proper error is produced. The last check is parsing
     * the third argument to a double. Should this not work the proper is produced. Finally, a new CS1003P4 object
     * is created and the search method is called from the object using the provided command line arguments.
     * @param args - the command line arguments. An array of string which contain the user's requests
     */
    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                throw new IllegalArgumentException("Wrong number of command line arguments!");
            }
            File directory  = new File(args[0]);
            if (!directory.exists()) {
                throw new FileNotFoundException("Directory \"" + args[0] + "\" does not exist!");
            }
            if (directory.list() == null || directory.list().length == 0) {
                throw new FileNotFoundException("\"" + directory + "\" does not contain any files!");
            }
            double similarity = -1.0;
            try {
                similarity = Double.parseDouble(args[2]);
                if (similarity > 1.0 || similarity < 0.0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("\"" + args[2] + "\" is not a valid similarity value!");
                System.exit(1);
            }
            CS1003P4 project = new CS1003P4();
            project.search(directory.getPath(), args[1], similarity);
        } catch (IllegalArgumentException | FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * search is the starting method which begins the process. The proper Spark configurations are created.
     * First a SparkConf is made and then a JavaSparkContext is created using the SparkConf. The Logger level is
     * set to off which disabled the many error/info prompts provided in the terminal by spark. The search term is
     * then cleaned, and it's bigram set is computed using the appropriate methods. A JavaPairRDD is created by
     * calling the JavaSparkContext and using the wholeTextFiles command and the provided directory. The newly acquired
     * data is then flat mapped into the proper sliding windows using a lambda expression. That expression works by
     * taking each individual text file and using two for loops to grab words and split them into appropriately
     * sized substrings. The JavaRDD, slidingWindow, then contains a list of these "windows." Lastly this method creates
     * one final JavaRDD of the outputs by filtering each window segment into a bigram set and passing this to
     * calculateJaccard. If the resulting double is greater than or equal to the provided similarity index, it is added.
     * The results of this RDD are printed to the terminal.
     * @param directory - a string path to the user provided directory
     * @param searchTerm - a string of the user requested search term
     * @param similarity - a double value of the user provided similarity threshold
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

    /**
     * cleanText is a simple method. It takes in a string to be cleaned and does two operations on it. First
     * all non-numeric characters of one or more instances are replaced with a single space. Then the line
     * is converted to lower case and returned.
     * @param line - a string of text to be cleaned
     * @return - returns a string value with the cleaned text
     */
    public static String cleanText(String line) {
        line = line.replaceAll("[^a-zA-Z0-9]+", " ");
        line = line.toLowerCase();
        return line;
    }

    /**
     * TAKEN FROM MY CS1003_P1 SUBMISSION
     * createBigram takes a string input. Using that, it then takes two letters at a time and adds them to the HashSet.
     * Finally, the HashSet is returned with all the bigrams.
     * @param word - a string value to be converted into a bigram
     * @return - a hashset of strings of the input value's bigrams
     */
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

    /**
     * TAKEN FROM MY CS1003_P1 SUBMISSION
     * calculateJaccard takes two HashSets. Using these sets, the intersection is found by adding everything
     * from one set and then retaining everything from the second set. The union is found
     * by adding everything from set 1 and set 2. Finally, the intersection size is divided by the union size
     * and returned.
     * @param set1 - the hashset i.e. bigram of the first value
     * @param set2 - the hashset i.e. bigram of the second value
     * @return - a double value containing the Jaccard similarity between two sets
     */
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
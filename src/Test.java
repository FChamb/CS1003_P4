import org.apache.derby.impl.store.raw.log.Scan;
import scala.tools.nsc.Global;
import scala.tools.nsc.ScalaDoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Test {
    /**
     * Testing class for testing CS1003_P4. THe main method creates a testing object and calls run
     * all tests which run several checks.
     * @param args - command line arguments, never used or accessed
     */
    public static void main(String[] args) {
        Test test = new Test();
        test.AllTests();
    }

    /**
     * This method runs through every test and prints out the output neatly in the
     * terminal. The final part of this method runs test.sh which is a shell script
     * that checks several outputs of CS1003_P4. This is done through a process builder
     * and a scanner to grab the output.
     */
    public void AllTests() {
        System.out.println("----------CS1003_P4 Testing----------");
        System.out.println("Loading bigram testing...");
        System.out.println("Value 1: 'cocoa'\nExpected output: [co, oc, oa]");
        testBigram("cocoa");
        System.out.println("Value 2: 'cheese'\nExpected output: [ch, ee, se, he, es]");
        testBigram("cheese");
        System.out.println("-------------------------------------");
        System.out.println("Loading similarity testing...");
        System.out.println("Value 1: 'cocoa', Value 2: 'cheese'\nExpected output: 0.0");
        testSimilarity("cocoa", "cheese");
        System.out.println("Value 3: 'speled', Value 4: 'spelled'\nExpected output: 0.833");
        testSimilarity("speled", "spelled");
        System.out.println("-------------------------------------");
        System.out.println("Loading test shell script...");
        File file = new File("test.sh");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(file.getAbsolutePath());
        try {
            Process process = processBuilder.start();
            String output = "";
            Scanner scan = new Scanner(new InputStreamReader(process.getInputStream()));
            while (scan.hasNext()) {
                output += scan.nextLine() + "\n";
            }
            int exit = process.waitFor();
            if (exit == 0) {
                System.out.println("Running script...");
                System.out.println(output);
                System.exit(0);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("-------------------------------------");
    }

    /**
     * testBigram prints the output of createBigram for a given term
     * @param value - a string value to test
     */
    public void testBigram(String value) {
        System.out.println("Bigram set of '" + value + "': " + CS1003P4.createBigram(value));
    }

    /**
     * testSimilarity prints the output of calculateJaccard for two given terms
     * @param value1 - a string value to test
     * @param value2 - a string value to test
     */
    public void testSimilarity(String value1, String value2) {
        String output = "" + CS1003P4.calculateJaccard(CS1003P4.createBigram(value1), CS1003P4.createBigram(value2));
        if (output.length() > 5) {
            output = output.substring(0, 5);
        }
        System.out.println("Jaccard index of '" + value1 + "' & '" + value2 + "': " + output);
    }
}

import scala.tools.nsc.Global;
import scala.tools.nsc.ScalaDoc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("/bin/bash -c export CLASSPATH=${CLASSPATH}:\"../spark/*\":.");
        Runtime.getRuntime().exec("javac *.java");
        String command = "java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 ../Tests/data \"setting sail to the rising wind\" 0.75";
        Process process = Runtime.getRuntime().exec(command);
        Scanner scan = new Scanner(new InputStreamReader(process.getInputStream()));
        while (scan.hasNext()) {
            System.out.println(scan.nextLine());
        }
    }
}

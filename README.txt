CS1003 Practical 4

DESCRIPTION:
This submission of CS1003_P4 contains two main classes and one shell script. The main class, CS1003P4
contains the java and apache spark integration. This takes a user's provided files, search term, and
similarity threshold to scan through all texts and return every matching line with same or higher
similarity. The Test class contains methods to test individual aspects of the submitted practical.
The shell script, test.sh, contains command line code to run the main class through various tests and
return the output. This script can be run separate to only test the program's functionality.

INSTRUCTIONS:
Run the following commands:
1. export CLASSPATH=${CLASSPATH}:"/cs/studres/CS1003/0-General/spark/*":.
2. javac *.java
3. java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 path/To/Directory "desired search term" 0.5 {similarity index, 0.0 - 1.0}
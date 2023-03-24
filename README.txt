export CLASSPATH=${CLASSPATH}:"../spark/*":.
javac *.java
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 "" "" ""
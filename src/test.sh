#!/bin/bash

export CLASSPATH=${CLASSPATH}:"/cs/studres/CS1003/0-General/spark/*":.
rm -f *.class
javac *.java

touch stdout
touch stderr

echo "  "

echo "arguments : fakeDirectory \"asghjkasghlasj\" 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 fakeDirectory "asghjkasghlasj" 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

touch emptyDirectory
echo "arguments : emptyDirectory \"asghjkasghlasj\" 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 emptyDirectory "asghjkasghlasj" 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout
rm -r emptyDirectory

echo "  "

echo "arguments : data 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"asghjkasghlasj\" \"notANumber\" "
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "asghjkasghlasj" "notANumber" > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"asghjkasghlasj\" 1.25"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "asghjkasghlasj" 1.25 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"new ebooks the project\" 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "new ebooks the project" 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"tHiS 'is a \\test R..u*n\" 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "tHiS 'is a \\test R..u*n" 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"she generally gave herself very good advice though she very seldom followed it\" 0.95"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "she generally gave herself very good advice though she very seldom followed it" 0.95 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"to be or not to be\" 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "to be or not to be" 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"setting sail to the rising wind\" 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "setting sail to the rising wind" 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"hide the christmas tree carefully\" 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "hide the christmas tree carefully" 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"what sort of madness is this\" 0.50"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "what sort of madness is this" 0.50 > stdout 2> stderr
LC_ALL=C sort Tests/queries/what-50/stdout -o Tests/queries/what-50/stdout
cat stdout

echo "  "

echo "arguments : data \"what sort of madness is this\" 0.62"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "what sort of madness is this" 0.62 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

echo "  "

echo "arguments : data \"what sort of madness is this\" 0.75"
java --add-exports java.base/sun.nio.ch=ALL-UNNAMED CS1003P4 data "what sort of madness is this" 0.75 > stdout 2> stderr
LC_ALL=C sort stdout -o stdout
cat stdout

rm stdout
rm stderr
# Makefile

SOURCE=./src/
BIN=./bin/

JC = javac

default:
	$(JC) -d $(BIN) $(SOURCE)*.java

clean:
	rm $(BIN)*.class

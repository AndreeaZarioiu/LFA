JFLAGS = -g
JC = javac
JVM = java

sources = $(wildcard *.java)
classes = $(sources:.java=.class)

all: build

build: $(classes)

run: build
	$(JVM) Main $(input) $(base)

.PHONY: clean
clean :
	rm -f *.class *~

%.class : %.java
	$(JC) $(JFLAGS) $<


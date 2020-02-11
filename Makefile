# Makefile to build the packages and main application
#
#

.PHONY: all packages controller clean

all: packages controller

packages:
	javac -cp ./bin -d ./bin ./src/p2p/p2pHost.java
	javac -cp ./bin -d ./bin ./src/p2p/p2p.java

controller:
	javac -cp ./bin -d ./bin ./src/controller.java

clean:
	rm -rf ./bin/*

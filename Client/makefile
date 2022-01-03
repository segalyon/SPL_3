CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread -lboost_filesystem -lboost_thread

all: BGSclient
	g++ -o bin/BGSclient bin/connectionHandler.o bin/echoClient.o bin/channel.o bin/receiverChannel.o bin/senderChannel.o $(LDFLAGS)

BGSclient: bin/connectionHandler.o bin/echoClient.o bin/channel.o bin/receiverChannel.o bin/senderChannel.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/channel.o: src/channel.cpp
	g++ $(CFLAGS) -o bin/channel.o src/channel.cpp

bin/receiverChannel.o: src/receiverChannel.cpp
	g++ $(CFLAGS) -o bin/receiverChannel.o src/receiverChannel.cpp

bin/senderChannel.o: src/senderChannel.cpp
	g++ $(CFLAGS) -o bin/senderChannel.o src/senderChannel.cpp


.PHONY: clean
clean:
	rm -f bin/*

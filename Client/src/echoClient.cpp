#include <stdlib.h>
#include <connectionHandler.h>
#include <channel.h>
#include <receiverChannel.h>
#include <senderChannel.h>
#include <iostream>
#include <thread>

using namespace boost;

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    //
    bool loginState = false;
    Channel *senderChannel = new SenderChannel(connectionHandler, loginState);
    Channel *receiverChannel = new ReceiverChannel(connectionHandler, loginState);

    // start in new threads to prevent blocking
    thread senderThread = senderChannel->getThread();
    thread receiverThread = receiverChannel->getThread();

    // close
    senderThread.join();
    receiverThread.join();
    return 0;
}
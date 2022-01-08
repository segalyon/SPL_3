//
// Created by roy on 12/28/21.
//

#ifndef CLIENT_CHANNEL_H
#define CLIENT_CHANNEL_H

#include "../include/connectionHandler.h"
#include <thread>
#include <iostream>
#include <iomanip>
#include <ctime>
#include <sstream>

using namespace std;

class Channel{
public:
    virtual void run() = 0;
    ~Channel();
    std::thread getThread();

protected:
    static std::vector<std::string> split(const std::string& s, const std::string& delimiter);
    void shortToBytes(short num, char* bytesArr);
    short bytesToShort(char* bytesArr);
    string getCurrentDatetime();
};

#endif //CLIENT_CHANNEL_H
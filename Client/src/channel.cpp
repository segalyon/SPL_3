//
// Created by roy on 12/28/21.
//

#include "../include/channel.h"


std::vector<std::string> Channel::split(const std::string &s, const std::string &delimiter) {

    std::vector<std::string> v;
    int s_pos = 0;
    int e_pos;
    while ((e_pos = s.find(delimiter, s_pos)) != std::string::npos) {
        v.push_back(s.substr(s_pos, e_pos - s_pos));
        s_pos = e_pos + 1;
    }
    v.push_back(s.substr(s_pos, e_pos - s_pos));

    return v;
}

void Channel::shortToBytes(short num, char *bytesArr)
{
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}
short Channel::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

string Channel::getCurrentDatetime() {
    //auto t = std::time(nullptr);
    //auto tm = *std::localtime(&t);

    //std::ostringstream oss;
    //oss << std::put_time(&tm, "%d-%m-%Y %H-%M-%S");
    //auto str = oss.str();

    return "16-01-2022 11:00";
}

std::thread Channel::getThread() {
    return std::thread( [this] { this->run(); } );
}

Channel::~Channel() {}
//
// Created by roy on 12/28/21.
//

#ifndef CLIENT_SENDERCHANNEL_H
#define CLIENT_SENDERCHANNEL_H

#include "../include/channel.h"

class SenderChannel : public Channel  {
private:
    ConnectionHandler &connection;
    bool &loginState;
public:
    SenderChannel(ConnectionHandler &connection, bool &loginState): connection(connection), loginState(loginState) {};
    virtual void run();
};


#endif //CLIENT_SENDERCHANNEL_H

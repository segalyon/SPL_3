//
// Created by roy on 12/28/21.
//

#ifndef CLIENT_RECEIVERCHANNEL_H
#define CLIENT_RECEIVERCHANNEL_H

#include "../include/channel.h"

class ReceiverChannel : public Channel {
private:
    ConnectionHandler &connection;
    bool &loginState;
public:
    ReceiverChannel(ConnectionHandler &connection, bool &loginState): connection(connection), loginState(loginState) {};
    virtual void run();

};


#endif //CLIENT_RECEIVERCHANNEL_H

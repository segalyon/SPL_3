package bgu.spl.net.api.bidi;

import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.MessagingProtocol;

public interface BidiMessagingProtocol<T>  {
    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    void start(int connectionId, Connections<T> connections);
    void process(T message);
    boolean shouldTerminate();
}

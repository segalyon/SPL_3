package bgu.spl.net.api.bidi;
import bgu.spl.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsImpl<T> implements Connections{
    //fields
    private ConcurrentHashMap<Integer, ConnectionHandler> connectionsMap;

    public ConnectionsImpl() {
        connectionsMap=new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, Object msg) {
        if (!connectionsMap.containsKey(connectionId))
            return false;
        else{

            ConnectionHandler handler=connectionsMap.get(connectionId);
            handler.send(msg);
            return true;
        }
    }

    @Override
    public void broadcast(Object msg) {
        for (ConnectionHandler h:connectionsMap.values()) {
            h.send(msg);
        }

    }

    @Override
    public void disconnect(int connectionId) {
        ConnectionHandler handler = connectionsMap.remove(connectionId);
//        try {
//            handler.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public synchronized int initConnection(ConnectionHandler handler){
        int id = connectionsMap.size() + 1;
        connectionsMap.put(id, handler);
        return id;
    }
}



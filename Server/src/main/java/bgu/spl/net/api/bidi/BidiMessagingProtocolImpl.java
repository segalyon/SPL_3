package bgu.spl.net.api.bidi;

//import bgu.spl.net.api.Messsages.*;
import bgu.spl.net.api.Messages.*;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.bidi.User;

import java.lang.Error;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {
    //fields
    private Connections connection;
    private int connectionId;
    private User user;

    public BidiMessagingProtocolImpl() {
    }

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connection=connections;
        this.connectionId=connectionId;
    }

    /**
     * Recieves a message and according to the opcode of the message handles the process
     * @param message
     */
    @Override
    public void process(Message message) {
        DataBase db = DataBase.getInstance();
        Short opcode=message.getOpcode();
        switch (opcode.intValue()){
            case 1:
                Register register = (Register)message;
                synchronized (db.getLockerRegister()){
                    if(!db.isRegisterd(register.getUsername())){
                        db.registerUser(register);
                        connection.send(connectionId, new Ack(message.getOpcode(), null));
                    }
                    else
                    {
                        connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                    }
                }
                break;
            case 2:
                Login login=(Login) message;
                if(!(db.getUserByUsername(login.getUsername())==null)) {
                    if (db.getCurrentUser(login.getUsername()) == null)
                    { db.loginUser(login);
                        connection.send(connectionId, new Ack(message.getOpcode(), null));
                    }
                    else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                }
                else
                    {
                        connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                    }
                break;
            case 3:
                Logout logout=(Logout) message;
                if(!(db.getUserByUsername(user.getUsername())==null))
                {
                    if(!(db.getCurrentUser(user.getUsername())==null))
                    {
                        db.removeUserfromuserByUserName(user.getUsername());
                    }
                    else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                }
                else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                break;
            case 4:
                Follow follow= (Follow) message;
                if(!(db.getUserByUsername(follow.getUsername())==null))
                {
                    if (!(db.getCurrentUser(follow.getUsername()) == null))
                    {
                        if(user.isFollowing(db.getCurrentUser(follow.getUsername()))) {
                            user.follow(db.getCurrentUser(follow.getUsername()));
                            connection.send(connectionId, new Ack(message.getOpcode(), null));
                        }
                    }
                    else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                }
                else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                break;
            case 5: postProcess((POST)message);
                break;
            case 6:
                PM pm= (PM) message;
                if(!(db.getUserByUsername(pm.getUsername())==null)){
                    if (!(db.getCurrentUser(pm.getUsername()) == null)){
                        if(user.isFollowing(db.getCurrentUser(pm.getUsername()))){
                            connection.send(connectionId, new Ack(message.getOpcode(), pm.getContent()));
                        }
                    }
                    else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                }
                else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                break;
            case 7:
                LogStat logStat= (LogStat) message;
               if(!(db.getUserByUsername(user.getUsername())==null)){
                   if(!(db.getCurrentUser(user.getUsername()) == null)){
                       for(String s:logStat.getUsers()){
                        //   connection.send(connectionId, new Ack(message.getOpcode(), logStat);
                       }
                   }
               }
                break;
            case 8: statProcess((STAT)message);
                break;
        }

    }

    /**

    @Override
    public boolean shouldTerminate() {
        return false;
    }


    /**
     * for the followUnfollow process message:
     * receives a list of users and returns a string of their usernames seperated by '\0'
     * @param list
     * @return
     */
    private String ToStringtmpUserLIst(List<User> list){
        String s="";
        for (User u:list) {
            s+=u.getUsername()+"\0";
        }
        return s;
    }

    /**
     * for the stat process message:
     * given a user, creates a list of opcodes for the creation of the ack message in the stat process
     * @param statUser
     * @param opcode
     * @return
     */
    private ConcurrentLinkedQueue ToShortStatMessage(User statUser,short opcode){
        ConcurrentLinkedQueue<Short> tmp=new ConcurrentLinkedQueue();
        tmp.add(opcode);
        tmp.add(statUser.getNumOfPosts().shortValue());
        tmp.add((short)statUser.getFollowers().size());
        tmp.add((short)statUser.getFollowing().size());
        return tmp;
    }



    /**
     * given a list, creates a list without repetitions
     * @param list
     * @return
     */
    public LinkedList<String> SetList(LinkedList<String> list){
        LinkedList<String> setList=new LinkedList<>();
        for (String s:list) {
            if(!setList.contains(s))
                setList.add(s);
        }
        return setList;
    }

}

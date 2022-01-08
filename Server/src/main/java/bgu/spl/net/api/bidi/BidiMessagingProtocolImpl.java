package bgu.spl.net.api.bidi;

//import bgu.spl.net.api.Messsages.*;
import bgu.spl.net.api.Messages.*;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.bidi.User;

import java.awt.*;
import java.lang.Error;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {
    //fields
    private Connections connection;
    private int connectionId;
    private boolean shouldTerminate;
    private User user;

    public BidiMessagingProtocolImpl() {
        shouldTerminate = false;
    }

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connection=connections;
        this.connectionId=connectionId;
    }
    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
    }

    @Override
    public void process(Message message) {
        DataBase db = DataBase.getInstance();
        Short opcode=message.getOpcode();
        if(opcode.intValue()>2 && user == null){
            connection.send(connectionId,new ErrorMessage(message.getOpcode()));
        }
        else if((opcode.intValue()>2) && !db.isConnected(user.getUsername())) {
            connection.send(connectionId,new ErrorMessage(message.getOpcode()));
        }
        else {
            switch (opcode.intValue()) {
                case 1:
                    Register register = (Register) message;
                    synchronized (db.getLockerRegister()) {
                        if (!db.isRegisterd(register.getUsername())) {
                            db.registerUser(register, connectionId);
                            connection.send(connectionId, new Ack(message.getOpcode(), null));
                        } else {
                            connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                        }
                    }
                    break;
                case 2:
                    Login login = (Login) message;
                    if (!(db.getUserByUsername(login.getUsername()) == null)) {
                        if (!db.isConnected(login.getUsername())) {
                            if(db.getUserByUsername(login.getUsername()).getPassword().equals(login.getPassword()) && login.getCaptcha() == "1") {
                                db.loginUser(login, connectionId);
                                this.user = db.getUserByUsername(login.getUsername());
                                connection.send(connectionId, new Ack(message.getOpcode(), null));
                                Queue<Message> notes = db.getWaitingMessagesForThisUser(login.getUsername());
                                if(notes!= null) {
                                    while (!notes.isEmpty()) {
                                        connection.send(connectionId, notes.remove());
                                    }
                                }
                            }
                            else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                        } else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                    } else {
                        connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                    }
                    break;
                case 3:
                    Logout logout = (Logout) message;
                    if (!(db.getUserByUsername(user.getUsername()) == null)) {
                        if (db.isConnected(user.getUsername())) {
                            connection.send(connectionId, new Ack(message.getOpcode(), null));
                            // close handler and connection
                            db.removeConnection(user.getUsername());
                            connection.disconnect(connectionId); // remove handler and connection
                            //
                        }
                        else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                    } else connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                    break;
                case 4:
                    Follow follow = (Follow) message;
                    if (!(db.getUserByUsername(follow.getUsername()) == null)) {
                        List<byte[]> optional = new LinkedList<>();
                        optional.add(shortToBytes(follow.isFollow()));
                        optional.add(follow.getUsername().getBytes());
                        if (!user.isFollowing(db.getUserByUsername(follow.getUsername())) && follow.isFollow() == (short) 1) {
                            user.follow(db.getUserByUsername(follow.getUsername()));
                            connection.send(connectionId, new Ack(message.getOpcode(), optional));
                            break;
                        } else if (user.isFollowing(db.getUserByUsername(follow.getUsername())) && follow.isFollow() == (short) 0) {
                            user.unfollow(db.getUserByUsername(follow.getUsername()));
                            connection.send(connectionId, new Ack(message.getOpcode(), optional));
                            break;
                        }
                    }
                    connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                    break;
                case 5:
                    POST post = (POST) message;
                    List<User> followers = db.getFollowers(user);
                    List<String> users = new LinkedList<>();
                    // get tagged
                    String currentUsername = null; // null if now working a word currently
                    for (char c : post.getContent().toCharArray()) {
                        if (c == '@') {
                            currentUsername = "";
                        } else if (currentUsername != null && c != ' ') {
                            currentUsername += c;
                        } else if (currentUsername != null && c == ' ') {
                            if (db.getUserByUsername(currentUsername) != null)
                                users.add(currentUsername);
                            currentUsername = null;
                        }
                    }
                    if (currentUsername != null) {
                        if (db.getUserByUsername(currentUsername) != null)
                            users.add(currentUsername);
                    }
                    // add followers to prevent double msgs
                    for (User follower : followers) {
                        if (!users.contains(follower.getUsername()))
                            users.add(follower.getUsername());
                    }
                    // send to all
                    for (String sendTo : users) {
                        sendNotificationToUser(sendTo, user.getUsername(), (short) 0, post.getContent());
                    }
                    connection.send(connectionId, new Ack(message.getOpcode(), null));
                    db.addToPosts(post);
                    user.incrementNumOfPosts();
                    break;
                case 6:
                    PM pm = (PM) message;
                    boolean success = false;
                    if (!(db.getUserByUsername(pm.getUsername()) == null)) {
                        if (user.isFollowing(db.getUserByUsername(pm.getUsername()))) {
                            success = true;
                            connection.send(connectionId, new Ack(message.getOpcode(), null));
                        }


                    }
                    if (!success) {
                        connection.send(connectionId, new ErrorMessage(message.getOpcode()));
                    } else {
                        sendNotificationToUser(pm.getUsername(), user.getUsername(), (short) 1, pm.getContent());
                      //  db.addToPosts(pm);
                    }
                    break;
                case 7:
                    LogStat logStat = (LogStat) message;
                    for(User current: db.getUsers()){
                        if(!user.didUserBlockedMe(current)){
                            sendStatOfUser(current, message.getOpcode());
                        }
                    }
                    break;
                case 8:
                    boolean isFine=true;
                    STAT stat=(STAT) message;
                    for(String s:stat.getUsernames()){
                        User current=db.getUserByUsername(s);
                        if(current==null) {
                            isFine = false;
                        }
                        else{
                            if(user.didUserBlockedMe(current) || current.didUserBlockedMe(user)){
                                isFine=false;
                            }
                        }
                    }
                    if(!isFine){
                        connection.send(connectionId,new ErrorMessage(stat.getOpcode()));
                    }
                    else {
                        for (String s : stat.getUsernames()) {
                            User spe = db.getUserByUsername(s);
                            sendStatOfUser(spe, message.getOpcode());
                        }
                    }
                    break;
                case 12:
                    Block block=(Block) message;
                    if(db.getUserByUsername(block.getUsername())==null){
                        connection.send(connectionId, new ErrorMessage(block.getOpcode()));
                    }
                    else{
                        user.blockingUser(db.getUserByUsername(block.getUsername()));
                        connection.send(connectionId,new Ack(block.getOpcode(), null));
                    }
            }
        }
    }

    private void sendStatOfUser(User receipentUser, short opcode){
        List<byte[]> result=new ArrayList<>();
        String birthday=receipentUser.getBirthday();
        String year="";
        int counter=0;
        for(char c:birthday.toCharArray()) {
            if(counter>=6)
                year+=c;
            counter++;
        }
        String age= String.valueOf(2022-Integer.parseInt(year));
        result.add(age.getBytes());
        result.add(String.valueOf(receipentUser.getNumOfPosts()).getBytes());
        result.add(String.valueOf(receipentUser.getFollowers().size()).getBytes());
        result.add(String.valueOf(receipentUser.getFollowing().size()).getBytes());
        connection.send(connectionId,new Ack(opcode, result));

    }
    private void sendNotificationToUser(String username,String sendingUsername, short PMorPublic, String content){
       DataBase db=DataBase.getInstance();
        Notification notification= new Notification(PMorPublic,sendingUsername,content);
        if(!db.isConnected(username)) {
            db.addWaitingMessage(db.getUserByUsername(username), notification);
        }
        else  connection.send(db.getConnection(username),notification);
    }
    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
}

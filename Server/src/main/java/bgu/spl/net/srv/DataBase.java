package bgu.spl.net.srv;
import bgu.spl.net.api.Messages.Login;
import bgu.spl.net.api.Messages.Message;
//import bgu.spl.net.api.Messsages.Message;
import bgu.spl.net.api.Messages.Register;
import bgu.spl.net.srv.bidi.User;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataBase {

    //fields
    private ConcurrentHashMap<User, LinkedList<Message>>  waitingMessageListByUser;
    private ConcurrentHashMap<String,User >  userByUserName;
    private ConcurrentHashMap<String,User> currentUsers;
    //private ConcurrentLinkedQueue<User> registrationOrderList;
    private Object lockerRegister;
    //private Object lockerLogin;

    private static class SingletonHolder {
        private static DataBase instance = new DataBase();
    }
    public static DataBase getInstance() {
        return SingletonHolder. instance;
    }
    private DataBase()
    {
        this.waitingMessageListByUser=new ConcurrentHashMap<>();
        this.userByUserName=new ConcurrentHashMap<>();
        this.currentUsers=new ConcurrentHashMap<>();
        // this.registrationOrderList=new ConcurrentLinkedQueue<>();
        //this.lockerRegister=new Object();
        //this.lockerLogin=new Object();
    }

    public void addWaitingMessage(User user, Message msg){
        waitingMessageListByUser.get(user).add(msg);
    }

    public boolean isRegisterd(String username) {
        return userByUserName.containsKey(username);
    }

    public synchronized void registerUser(Register register){
        User user=new User(register.getUsername(), register.getPassword(), register.getBirthday());
        waitingMessageListByUser.put(user, new LinkedList<>());
        userByUserName.put(register.getUsername(),user);
        //registrationOrderList.add(user);
    }

    public User tryLogIn(String username, String password) {
        if (userByUserName.containsKey(username)) {
            if (userByUserName.get(username).getPassword().equals(password) && userByUserName.get(username).isLogedIn()==false)
                return userByUserName.get(username);
        }
        return null;
    }

    //returns a user by the username, if no such user exists returns null
    public User getUserByUsername(String username){
        return userByUserName.get(username);
    }
    public User getCurrentUser(String username){return currentUsers.get(username);}
    public User removeUserfromuserByUserName(String username)
    {
      return  currentUsers.remove(username);
    }
    public void loginUser(Login login)
    {
        User user= userByUserName.get(login.getUsername());
        currentUsers.put(login.getUsername(),user);
    }
//    public String getRegistrationOrderList(){
//        String list="";
//        for (User u:registrationOrderList) {
//            list+=u.getUsername()+"\0";
//        }
//        return list;
//    }

 //   public short getRegistrationListSize(){
 //       return(short)registrationOrderList.size();
 //   }

    public LinkedList<Message>getWaitingMessages(User user) {
        return waitingMessageListByUser.get(user);
    }

    public Object getLockerRegister() {
        return lockerRegister;
    }
//
//    public Object getLockerLogin() {
//        return lockerLogin;
//    }
}

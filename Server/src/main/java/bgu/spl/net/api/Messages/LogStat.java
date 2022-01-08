package bgu.spl.net.api.Messages;

import bgu.spl.net.srv.bidi.User;

import java.util.ArrayList;
import java.util.List;

public class LogStat extends Message {

    List<String> users = new ArrayList<>();

    public LogStat() {
        super((short) 7);

//        String currentUsername = "";
//        for(int i = 0; i < words.get(0).length; i++){
//            if(words.get(0)[i] == '|') {
//                users.add(currentUsername);
//                currentUsername = "";
//            }
//            else{
//                currentUsername += words.get(0)[i];
//            }
//        }
//        // add final one
//        if(currentUsername.length() > 0){
//            users.add(currentUsername);
//        }
    }
    public List<String> getUsers(){
        return users;
    }
}

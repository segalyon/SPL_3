package bgu.spl.net.api.Messages;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class POST extends Message {
    //fields
  //  private String sendingUser;
    private String content;
    public POST(List<byte[]> words) {
        super((short) 5);
        this.content= new String(words.get(0), StandardCharsets.UTF_8);
    }

    public String getContent() {
        return content;
    }
/*
    public LinkedList<String> usersFromContent(){
        LinkedList<String> userNames=new LinkedList<>();
        String[] splited = content.split(" ");
        for (String s:splited) {
            if(s.charAt(0)=='@' && s.length()>1)
                userNames.add(s.substring(1));
        }
        return userNames;
    }

    public String getSendingUser() {
        return sendingUser;
    }

    public void setSendingUser(String sendingUser) {
        this.sendingUser = sendingUser;
    }

*/
}

package bgu.spl.net.api.Messages;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class STAT extends Message {
    //fields
    private List<String> usernames;

    public STAT(List<byte[]> words) {
        super((short) 8);
        String allnames = new String(words.get(0), StandardCharsets.UTF_8);
        String name="";
        usernames = new LinkedList<>();
       for(char c: allnames.toCharArray())
       {
           if(!(c=='|'))
           {
               name+=c;
           }
           else
               {
                   usernames.add(name);
                   name="";
               }
       }
       if(name.length()>0){
           usernames.add(name);
       }
    }
    public List<String> getUsernames(){return usernames;}

}

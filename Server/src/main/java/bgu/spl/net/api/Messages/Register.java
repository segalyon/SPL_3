package bgu.spl.net.api.Messages;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Register extends Message {
    //fields
    private String username;
    private String password;
    private String birthday;


    public Register(List<byte[]> words) {
        super((short) 1);
        username = new String(words.get(0), StandardCharsets.UTF_8);
        password = new String(words.get(1), StandardCharsets.UTF_8);
        birthday= new String(words.get(2), StandardCharsets.UTF_8);
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getBirthday() {
        return birthday;
    }


}

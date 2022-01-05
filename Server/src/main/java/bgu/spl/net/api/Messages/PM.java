package bgu.spl.net.api.Messages;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class PM extends Message {
    //fields
    private String username;
    private String content;
    private String sendingDateAndTime;

    public PM(List<byte[]> words) {
        super((short) 6);
        username = new String(words.get(0), StandardCharsets.UTF_8);
        content = new String(words.get(1), StandardCharsets.UTF_8);
        sendingDateAndTime= new String(words.get(2), StandardCharsets.UTF_8);

    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
    public String getSendingDateAndTime(){return sendingDateAndTime;}

    public void setSendingUser(String sendingUser) {
        this.sendingUser = sendingUser;
    }
}

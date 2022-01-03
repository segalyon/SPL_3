package bgu.spl.net.api.Messages;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Block extends Message {
    //fields
    private boolean follow;
    private String username;

    //constructor
    public Block(List<byte[]> words) {
        // opcode
        super((short) 12);

        // username
        String username = new String(words.get(0), StandardCharsets.UTF_8);
    }

    public boolean isFollow() {
        return follow;
    }

    public String getUsername() {
        return username;
    }
}

package bgu.spl.net.api.Messages;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Follow extends Message {
    //fields
    private short follow;
    private String username;

    //constructor
    public Follow(List<byte[]> words) {
        // opcode
        super((short) 4);

        follow = (short)(words.get(0)[0] == 1 ? 1 : 0);
        username = new String(words.get(1), StandardCharsets.UTF_8);
    }

    public short isFollow() {
        return follow;
    }

    public String getUsername() {
        return username;
    }
}

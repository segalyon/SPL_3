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

        // follow/unfollow
        //byte[] followUnfollowByteArr = new byte[1];
        //followUnfollowByteArr[0] = words.get(0)[0];

        follow = words.get(0)[0];
        if (words.size() == 2){
            username = new String(words.get(1), StandardCharsets.UTF_8);
        }
        else{
            byte[] usernameByteArr = new byte[words.get(0).length - 1];
            for (int i = 1; i < words.get(0).length; i++){
                usernameByteArr[i - 1] = words.get(0)[i];
            }
            username = new String(words.get(1), StandardCharsets.UTF_8);
        }
    }

    public short isFollow() {
        return follow;
    }

    public String getUsername() {
        return username;
    }
}

package bgu.spl.net.api.Messages;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class Follow extends Message {
    //fields
    private boolean follow;
    private String username;

    //constructor
    public Follow(List<byte[]> words) {
        // opcode
        super((short) 4);

        // follow/unfollow
        byte[] followUnfollowByteArr = new byte[1];
        followUnfollowByteArr[0] = words.get(0)[0];
        short followUnfollow = bytesToShort(followUnfollowByteArr);

        // username
        byte[] usernameByteArr = new byte[words.get(0).length - 1];
        for (int i = 1; i < words.get(0).length; i++){
            usernameByteArr[i - 1] = words.get(0)[i];
        }
        String username = new String(usernameByteArr, StandardCharsets.UTF_8);
    }

    public boolean isFollow() {
        return follow;
    }

    public String getUsername() {
        return username;
    }
}

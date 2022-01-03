package bgu.spl.net.srv.bidi;

import java.util.List;

public class User {
    private String username;
    private String password;
    private String birthday;
    private List<User> followedUsers;
    private List<User> blockedUsers;
    public User(String username, String password, String birthday)
    {
        this.username=username;
        this.password=password;
        this.birthday=birthday;
    }
    public void FollowUser(String username)
    {

    }
}

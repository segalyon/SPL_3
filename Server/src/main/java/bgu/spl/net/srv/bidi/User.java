package bgu.spl.net.srv.bidi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private String username;
    private String password;
    private String birthday;
    private boolean logedIn;
    private AtomicInteger numOfPosts;
    private AtomicInteger conncetionID;
    private List<User> following;
    private List<User> followers;
    public User(String username, String password, String birthday)
    {
        this.username = username;
        this.password = password;
        this.birthday=birthday;
        this.logedIn=false;
        this.numOfPosts=new AtomicInteger(0);
        following=new LinkedList<>();
        followers=new LinkedList<>();
    }
    public List<User> getFollowing() {
        return following;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void addToFollowers(User user) {
        followers.add(user);
    }

    public void removeFromFollowers(User user){
        followers.remove(user);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getLoginState() {
        return logedIn;
    }

    public void setLogedIn(boolean logedIn) {
        this.logedIn = logedIn;
    }

    public AtomicInteger getNumOfPosts() {
        return numOfPosts;
    }
    // adding a user to ourlist
    public void follow(User addingUser){
        if(!following.contains(addingUser)){
            following.add(addingUser);
            addingUser.addToFollowers(this);// add followers
        }
    }
    public boolean isFollowing(User check){
        return following.contains(check);
    }
    //removing a user from ourlist
    public boolean unfollow(User addingUser){
        if(following.contains(addingUser)){
            following.remove(addingUser);
            addingUser.removeFromFollowers(this);
            return true;
        }
        return false;
    }
}

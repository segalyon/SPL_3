package bgu.spl.net.srv.bidi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private String username;
    private String password;
    private String birthday;
    private boolean logedIn;
    private int numOfPosts;
    private List<User> following;
    private List<User> followers;
    private List<User> blockingUsers;
    private List<User> blockersUsers;
    public User(String username, String password, String birthday)
    {
        this.username = username;
        this.password = password;
        this.birthday=birthday;
        this.logedIn=false;
        this.numOfPosts=0;
        following=new LinkedList<>();
        followers=new LinkedList<>();
        blockingUsers=new LinkedList<>();
        blockersUsers=new LinkedList<>();
    }
    public String getBirthday(){return birthday;}
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
    public synchronized void incrementNumOfPosts(){
        numOfPosts++;
    }

    public boolean getLoginState() {
        return logedIn;
    }

    public void setLogedIn(boolean logedIn) {
        this.logedIn = logedIn;
    }

    public int getNumOfPosts() {
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
    public void unfollow(User addingUser){
        if(following.contains(addingUser)){
            following.remove(addingUser);
            addingUser.removeFromFollowers(this);
        }
    }
    public boolean didUserBlockedMe(User user){
        return blockersUsers.contains(user);
    }
    public void blockingUser(User user){
        blockingUsers.add(user);
        user.someoneBlockedme(this);
    }
    public void someoneBlockedme(User user){
        blockersUsers.add(user);
    }
}

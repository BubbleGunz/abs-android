package group9.android_project;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Benjamin on 2015-11-03.
 */
public class User {
    public int id;
    public String firstname, lastname, username, email, password, confirmpassword,token;
    public Date date;
    public User(){}

    public User(String firstname, String lastname, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
    }

   /* public static ArrayList<User> setFriend(ArrayList<User> friends) {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(friends.g,friends.lastname,user.username));
        return users;
    }*/


}

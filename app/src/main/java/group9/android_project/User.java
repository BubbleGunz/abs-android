package group9.android_project;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Benjamin on 2015-11-03.
 */
public class User implements Serializable {
    public int id;
    public String firstname, lastname, username, email, password, confirmpassword,token;
    public Date date;
    public User(){}

    public User(String firstname, String lastname, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
    }

}

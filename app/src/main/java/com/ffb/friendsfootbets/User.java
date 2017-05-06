package com.ffb.friendsfootbets;

import java.util.ArrayList;

/**
 * Created by Younes and Mehdi on 06/05/2017.
 */

public class User {
    String username;
    String email;
    ArrayList<User> usersFollowed;
    String photoUrl;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<User> getUsersFollowed() {
        return usersFollowed;
    }

    public void setUsersFollowed(ArrayList<User> usersFollowed) {
        this.usersFollowed = usersFollowed;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

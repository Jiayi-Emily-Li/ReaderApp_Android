package edu.northeastern.cs5520group7.model;

public class User {
    public String username;
    public String name;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }

}


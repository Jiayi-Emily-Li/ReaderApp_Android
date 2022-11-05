package edu.northeastern.cs5520group7.model;

import java.util.HashMap;

public class User {
    private String userId;
    private String name;
    private String token;
    private HashMap<String, String> histories;

    public User(){

    }
    public User(String userId, String name, String token) {
        this.userId = userId;
        this.name = name;
        this.token = token;
        this.histories = new HashMap<>();
    }

    public User(String userId, String name, String token, HashMap<String, String> histories) {
        this.userId = userId;
        this.name = name;
        this.token = token;
        this.histories = histories;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HashMap<String, String> getHistories() {
        return histories;
    }

    public void addHistory(String from, String to, String time, String image) {
        this.histories.put("from", from);
        this.histories.put("to", to);
        this.histories.put("time", time);
        this.histories.put("image", image);
    }
}

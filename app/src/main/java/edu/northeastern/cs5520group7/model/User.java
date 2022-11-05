package edu.northeastern.cs5520group7.model;

import java.util.ArrayList;

public class User {
    private String userId;
    private String name;
    private String token;
    private ArrayList<History> histories;

    public User() {

    }

    public User(String userId, String name, String token) {
        this.userId = userId;
        this.name = name;
        this.token = token;
        this.histories = new ArrayList<>();
    }

    public User(String userId, String name, String token, ArrayList<History> histories) {
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

    public ArrayList<History> getHistories() {
        return histories;
    }

    public void addHistory(String from, String to, String time, String image) {
        this.histories.add(new History(from, to, time, image));
    }


    @Override
    public String toString() {
        String s = new String();
        for (History h : histories) {
            s += h.toString() + '\n';
        }

        return s;
    }
}

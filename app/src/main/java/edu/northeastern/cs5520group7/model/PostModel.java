package edu.northeastern.cs5520group7.model;

import com.google.gson.annotations.SerializedName;

public class PostModel {
    private int userId;
    private Integer id;
    private String title;

    //We can have different name, just use specify which field is to be associated as below

    @SerializedName("body")
    private String text;

    public PostModel(int userId, String title, String text) {
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    // declare
    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}

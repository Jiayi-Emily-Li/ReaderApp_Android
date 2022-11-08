package edu.northeastern.cs5520group7.model;

import java.io.Serializable;

public class History implements Serializable {
    String from;
    String to;
    String time;
    String image;

    public History () {

    }
    public History(String from, String to, String time, String image) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.image = image;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "History{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", time='" + time + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}


package edu.northeastern.cs5520group7.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {

    //SerializedName is the field name of the google books json file

    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("volumeInfo")
    @Expose
    private VolumeInfo volumeInfo;


    public String getKind() {
        return kind;
    }


    public String getId() {
        return id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setKind(String category) {
        this.kind = kind;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

}

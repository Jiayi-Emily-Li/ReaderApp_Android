package edu.northeastern.cs5520group7.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {
    @SerializedName("kind")
    @Expose
    private String category;

    @SerializedName("totalItems")
    @Expose
    private Integer totalBooks;

    @SerializedName("items")
    @Expose
    private List<Item> books = null;

    public String getCategory() {
        return category;
    }

    public Integer getTotalBooks() {
        return totalBooks;
    }

    public List<Item> getBooks() {
        return books;
    }

    public void setBooks(List<Item> books) {
        this.books = books;
    }
}

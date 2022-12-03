package edu.northeastern.cs5520group7.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultiBooks {
    @SerializedName("kind")
    @Expose
    private String category;

    @SerializedName("totalItems")
    @Expose
    private Integer totalBooks;

    @SerializedName("items")
    @Expose
    private List<Book> books = null;

    public String getCategory() {
        return category;
    }

    public Integer getTotalBooks() {
        return totalBooks;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

package edu.northeastern.cs5520group7.model;

public class Post {
    String bookID;
    String title;
    String rating;
    String review;

    public Post(){

    }
    public Post(String bookID, String title, String rating, String review){
        this.bookID = bookID;
        this.title = title;
        this.rating = rating;
        this.review = review;
    }

    public String getBookID(){
        return bookID;
    }
    public void setBookID(String bookID){
        this.bookID = bookID;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getRating(){
        return rating;
    }
    public void setRating(String rating){
        this.rating = rating;
    }
    public String getReview(){
        return review;
    }
    public void setReview(String review){
        this.review = review;
    }
}

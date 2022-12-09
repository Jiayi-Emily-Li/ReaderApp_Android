package edu.northeastern.cs5520group7.model;

public class Review {
    private String review;
    private String commenter;
    private String rating;

    public Review(String review, String commenter, String rating) {
        this.review = review;
        this.commenter = commenter;
        this.rating = rating;
    }

    public Review() {
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "review='" + review + '\'' +
                ", commenter='" + commenter + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}

package edu.northeastern.cs5520group7.model;

public class Review {
    private String review;
    private String commenter;

    public Review(String review, String commenter) {
        this.review = review;
        this.commenter = commenter;
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


    @Override
    public String toString() {
        return "Review{" +
                "review='" + review + '\'' +
                ", commenter='" + commenter + '\'' +
                ", rating='" +
                '}';
    }
}

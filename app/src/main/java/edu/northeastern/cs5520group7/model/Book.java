package edu.northeastern.cs5520group7.model;

public class Book {
    String bookId;
    String bookname;
    String author;
    String category;
    String rating;

    public Book(String bookId, String bookname, String author, String category, String rating) {
        this.bookId = bookId;
        this.bookname = bookname;
        this.author = author;
        this.category = category;
        this.rating = rating;

    }

    /**
     * Gets the book's title.
     * @return String representing the book's title.
     */
    public String getBookname() {
        return bookname;
    }

    /**
     * Gets the book's author.
     * @return String representing the book's author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the book's genre.
     * @return String representing the book's genre.
     */
    public String getCategory() {return category;}

    /**
     * Gets the book's average rating.
     * @return Double representing the book's average rating.
     */
    public String getRating() {
        return rating;
    }


    /**
     * Gets the textual representation of a book.
     * @return String representation of the book.
     */
    @Override
    public String toString() {
        return bookname + " by " + author;
    }

    /**
     * Gets the book's ID.
     * @return Integer representation of the book ID.
     */
    public String getBookId() {
        return bookId;
    }

}

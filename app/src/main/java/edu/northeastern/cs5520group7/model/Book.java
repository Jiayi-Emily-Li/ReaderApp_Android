package edu.northeastern.cs5520group7.model;

public class Book {
    int bookID;
    String title;
    String author;
    String genre;
    int publicationYear;
    String isbn;
    double rating;
    String description;
    private String imageURL;
    //private ImageButton linkedImageButton;

    public Book(int bookID, String title, String author, String genre, int publicationYear,
                String isbn, double rating, String description, String imageURL) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.rating = rating;
        this.description = description;
        this.imageURL = imageURL;
    }

    /**
     * Gets the book's title.
     * @return String representing the book's title.
     */
    public String getTitle() {
        return title;
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
    public String getGenre() {return genre;}

    /**
     * Gets the book's average rating.
     * @return Double representing the book's average rating.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Gets the book's isbn.
     * @return String representing the book's isbn.
     */
    public String getISBN() {
        return isbn;
    }
    /**
     * Gets the textual representation of a book.
     * @return String representation of the book.
     */
    @Override
    public String toString() {
        return title + " by " + author;
    }

    /**
     * Gets the book's ID.
     * @return Integer representation of the book ID.
     */
    public int getBookID() {
        return bookID;
    }
    /**
     * Gets the book's publication year.
     * @return Integer representing the book's publication year.
     */
    public int getPublicationYear() {
        return publicationYear;
    }
    /**
     * Gets the book's description.
     * @return String representing the book's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets a link to the book's cover image.
     * @return String representing the book's cover image URL.
     */
    public String getImageURL() { return imageURL; }
}

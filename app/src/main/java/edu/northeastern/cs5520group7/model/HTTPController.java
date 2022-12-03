package edu.northeastern.cs5520group7.model;

import edu.northeastern.cs5520group7.model.api.Book;
import edu.northeastern.cs5520group7.model.api.MultiBooks;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HTTPController {
    @GET("/books/v1/volumes")
    Call<MultiBooks> getVolumeBooks(@Query(value = "q") String searchText);

    @GET("/books/v1/volumes/{id}")
    Call<Book> getBookItem(@Path("id") String id);

    @GET("/books/v1/volumes?q=categories:young+fiction&maxResults=30")
    Call<MultiBooks> getNewReleaseBooks();

    @GET("/books/v1/volumes")
    Call<MultiBooks> getCategories(@Query(value = "q") String searchText, @Query("filter") String filter, @Query("orderBy") String orderBy, @Query("maxResults") int maxResults);

}


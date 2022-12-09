package edu.northeastern.cs5520group7.model;

import edu.northeastern.cs5520group7.model.api.Item;
import edu.northeastern.cs5520group7.model.api.Items;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HTTPController {
    @GET("/books/v1/volumes")
    Call<Items> getVolumeBooks(@Query(value = "q") String searchText);

    @GET("/books/v1/volumes/{id}")
    Call<Item> getBookItem(@Path("id") String id);

    @GET("/books/v1/volumes")
    Call<Items> getCategoryResults(@Query(value = "q") String searchText, @Query("filter") String filter, @Query("orderBy") String orderBy, @Query("maxResults") int maxResults);

    @GET("/books/v1/volumes")
    Call<Items> getSearchResults(@Query(value = "q") String searchText, @Query("startIndex") int startIndex, @Query("orderBy") String orderBy, @Query("maxResults") int maxResults);

}


package edu.northeastern.cs5520group7.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IPlaceholder {

    // Get requests just need getter on Models
    @GET("posts")   // "post/" is the relative Url of your api. We define base Url at a common place later
    Call<List<PostModel>>  getPostModels();


    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int id);

    @GET("posts")
    Call<List<PostModel>>  getPostsWithQuery(@Query("userId") int userId);

    @GET("posts")
    Call<List<PostModel>>  getPostsWithQueryMultipleParams(@Query("userId") int userId,
                                                           @Query("_sort") String sort,
                                                           @Query("_order") String order);

    // Post request needs constructors in our Models
    @POST("posts")
    Call<PostModel> createPost(@Body PostModel post);

    @Headers({"Static-Header: 123", "Static-Header2: 456"})
    @POST("posts")
    Call<PostModel>  getPostModelsWithHeaders(@Header("Dynamic-Header") String header,
                                              @Body PostModel post);

    // Another way to do this is from URLEncoding, there's also an API for it on retrofit

    //Retrofit also supports PUT,PATCH and DELETE , same as Post
}


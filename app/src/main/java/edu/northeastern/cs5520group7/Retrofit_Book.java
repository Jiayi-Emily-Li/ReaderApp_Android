package edu.northeastern.cs5520group7;

import edu.northeastern.cs5520group7.model.HTTPController;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Retrofit_Book {
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder().baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static HTTPController getAPI() {
        return getRetrofit().create(HTTPController.class);
    }

    public static Retrofit getNewBooks() {
        return new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static HTTPController getNewBooksAPI() {
        return getNewBooks().create(HTTPController.class);
    }

}



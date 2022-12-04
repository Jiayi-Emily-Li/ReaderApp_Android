package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.cs5520group7.Adapter.SingleCategoryAdapter;
import edu.northeastern.cs5520group7.model.HTTPController;
import edu.northeastern.cs5520group7.model.api.Book;
import edu.northeastern.cs5520group7.model.api.MultiBooks;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverSingleCategory extends AppCompatActivity implements View.OnClickListener {
    TextView sortRel, sortNew;
    String category;

    RecyclerView sCatRV;
    SingleCategoryAdapter singleCategoryAdapter;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;

    HTTPController httpController;
    Call<MultiBooks> sCatBooksCall;
    List<Book> books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        sCatRV = findViewById(R.id.sCatRV);
        sortNew = findViewById(R.id.sortNewest);
        sortRel = findViewById(R.id.sortRelevance);
        progressBar = findViewById(R.id.progressBar);

        httpController = Retrofit_Book.getNewBooksAPI();

        category = getIntent().getStringExtra("categoryName");
        Log.d("categoryName", category);
        if (category != null) {
            setTitle(category);
            progressBar.setVisibility(View.VISIBLE);
            sCatRV.setVisibility(View.VISIBLE);
            callSingleCategoryBooks("relevance", 40);
        }

        sortRel.setOnClickListener(this);
        sortNew.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sortNewest:
                progressBar.setVisibility(View.VISIBLE);
                sCatRV.setVisibility(View.VISIBLE);
                callSingleCategoryBooks("newest", 40);
                break;
            case R.id.sortRelevance:
                progressBar.setVisibility(View.VISIBLE);
                sCatRV.setVisibility(View.VISIBLE);
                callSingleCategoryBooks("relevance", 40);
                break;
        }
    }

    private void callSingleCategoryBooks(String sortBy, int maxResults) {
        sCatBooksCall = httpController.getSearchResults(category, 0, sortBy, maxResults);
        Log.d("category", category);
        sCatBooksCall.enqueue(new Callback<MultiBooks>() {
            @Override
            public void onResponse(Call<MultiBooks> call, Response<MultiBooks> response) {
                sCatRV.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    books = response.body().getBooks();
                    for (int i = 0; i < response.body().getBooks().size(); i++) {
                        singleCategoryAdapter = new SingleCategoryAdapter(DiscoverSingleCategory.this, books);
                        layoutManager = new LinearLayoutManager(DiscoverSingleCategory.this, LinearLayoutManager.VERTICAL, false);
                        sCatRV.setLayoutManager(layoutManager);
                        sCatRV.setAdapter(singleCategoryAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<MultiBooks> call, Throwable t) {
                Toast.makeText(DiscoverSingleCategory.this, "Loading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
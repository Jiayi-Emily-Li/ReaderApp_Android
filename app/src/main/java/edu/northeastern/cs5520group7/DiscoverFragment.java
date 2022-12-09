package edu.northeastern.cs5520group7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.northeastern.cs5520group7.Adapter.DiscoverAdapter;
import edu.northeastern.cs5520group7.model.HTTPController;
import edu.northeastern.cs5520group7.model.api.MultiBooks;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    HTTPController httpController;
    Call<MultiBooks> cat1_Call, cat2_Call, cat3_Call, cat4_Call;

    Spinner cat_spinner;
    List<String> categories;
    ShimmerFrameLayout shimmer_1, shimmer_2, shimmer_3, shimmer_4;
    TextView Err_1, Err_2, Err_3, Err_4;
    RecyclerView RV_1, RV_2, RV_3, RV_4;
    DiscoverAdapter discoverAdapter;
    LinearLayoutManager cat1_LM, cat2_LM, cat3_LM, cat4_LM;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<String> readerFavCat;
    String cat_1, cat_2, cat_3, cat_4;


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        RV_1 = view.findViewById(R.id.RV_1);
        RV_2 = view.findViewById(R.id.RV_2);
        RV_3 = view.findViewById(R.id.RV_3);
        RV_4 = view.findViewById(R.id.RV_4);
        shimmer_1 = view.findViewById(R.id.shimmer_1);
        shimmer_2 = view.findViewById(R.id.shimmer_2);
        shimmer_3 = view.findViewById(R.id.shimmer_3);
        shimmer_4 = view.findViewById(R.id.shimmer_4);

        swipeRefreshLayout = view.findViewById(R.id.discoverPg);

        //drop-down menu
        cat_spinner = view.findViewById(R.id.cat_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_choices, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_spinner.setAdapter(spinnerAdapter);
        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                }else{
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    if (selectedItem == "Biography"){selectedItem = "biography&autobiography";}
                    else if(selectedItem == "Business"){selectedItem = "investing+business";}
                    else if(selectedItem == "Children"){selectedItem = "childrens";}
                    else if (selectedItem == "Comics"){selectedItem = "comics&graphic novels";}
                    else if (selectedItem == "Fantasy"){selectedItem = "fiction+fantasy";}
                    else if (selectedItem == "Fiction"){selectedItem = "young+fiction";}
                    else if (selectedItem == "Thriller"){selectedItem = "thriller+horror+suspense";}
                    else if (selectedItem == "Travel"){selectedItem = "travel+travelling";}
                    Intent intent = new Intent(getContext(), DiscoverSingleCategory.class);
                    intent.putExtra("categoryName", selectedItem);
                    getContext().startActivity(intent);
            }}


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //default recommend result for new users or users without book list
        cat_1 = "young+fiction";
        cat_2 = "romance";
        cat_3 = "thriller+horror+suspense";
        cat_4 = "childrens";
        readerFavCat = new ArrayList<>(Arrays.asList(cat_1, cat_2, cat_3, cat_4));

        httpController = Retrofit_Book.getAPI();
        getRecommendBooks();

        return view;
    }


    //reloading result when refresh the discover page
    @Override
    public void onRefresh() {
        hideRVs();
        showShimmers();
        getRecommendBooks();
    }

    //hiding the book result from the previous request
    public void hideRVs() {
        RV_1.setVisibility(View.GONE);
        RV_2.setVisibility(View.GONE);
        RV_3.setVisibility(View.GONE);
        RV_4.setVisibility(View.GONE);
    }

    //set shimmer on while the book results still loading
    public void showShimmers() {
        shimmer_1.setVisibility(View.VISIBLE);
        shimmer_2.setVisibility(View.VISIBLE);
        shimmer_3.setVisibility(View.VISIBLE);
        shimmer_4.setVisibility(View.VISIBLE);
    }



    private void getRecommendBooks() {
        callRecommend_1();
        callRecommend_2();
        callRecommend_3();
        callRecommend_4();
    }

    private void callRecommend_1() {
        cat1_Call = httpController.getCategoryResults("categories:" + cat_1,"ebooks", "relevance", 40);
        cat1_Call.enqueue(new Callback<MultiBooks>() {
            @Override
            public void onResponse(Call<MultiBooks> call, Response<MultiBooks> response) {
                RV_1.setVisibility(View.VISIBLE);
                shimmer_1.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    for(int i = 0; i < response.body().getBooks().size(); i++) {
                        discoverAdapter = new DiscoverAdapter(getContext(), response.body().getBooks());
                        Log.d("book", response.body().getBooks().get(i).getVolumeInfo().getTitle());
                        cat1_LM = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        RV_1.setLayoutManager(cat1_LM);
                        RV_1.setAdapter(discoverAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<MultiBooks> call, Throwable t) {
                RV_1.setVisibility(View.GONE);
                shimmer_1.setVisibility(View.GONE);
                Err_1.setVisibility(View.VISIBLE);
                Err_1.setText(t.getMessage());


            }
        });

    }

    private void callRecommend_2() {
        cat2_Call = httpController.getCategoryResults("categories:" + cat_2,"ebooks", "relevance", 40);
        cat2_Call.enqueue(new Callback<MultiBooks>() {
            @Override
            public void onResponse(Call<MultiBooks> call, Response<MultiBooks> response) {
                RV_2.setVisibility(View.VISIBLE);
                shimmer_2.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    for(int i = 0; i < response.body().getBooks().size(); i++) {
                        discoverAdapter = new DiscoverAdapter(getContext(), response.body().getBooks());
                        cat2_LM = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        RV_2.setLayoutManager(cat2_LM);
                        RV_2.setAdapter(discoverAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<MultiBooks> call, Throwable t) {
                RV_2.setVisibility(View.GONE);
                shimmer_2.setVisibility(View.GONE);
                Err_2.setVisibility(View.VISIBLE);
                Err_2.setText(t.getMessage());


            }
        });

    }

    private void callRecommend_3() {
        cat3_Call = httpController.getCategoryResults("categories:" + cat_3,"ebooks", "relevance", 40);
        cat3_Call.enqueue(new Callback<MultiBooks>() {
            @Override
            public void onResponse(Call<MultiBooks> call, Response<MultiBooks> response) {
                RV_3.setVisibility(View.VISIBLE);
                shimmer_3.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    for(int i = 0; i < response.body().getBooks().size(); i++) {
                        discoverAdapter = new DiscoverAdapter(getContext(), response.body().getBooks());
                        cat3_LM = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        RV_3.setLayoutManager(cat3_LM);
                        RV_3.setAdapter(discoverAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<MultiBooks> call, Throwable t) {
                RV_3.setVisibility(View.GONE);
                shimmer_3.setVisibility(View.GONE);
                Err_3.setVisibility(View.VISIBLE);
                Err_3.setText(t.getMessage());
            }
        });


    }

    private void callRecommend_4() {
        cat4_Call = httpController.getCategoryResults("categories:"+ cat_4,"ebooks", "relevance", 40);
        cat4_Call.enqueue(new Callback<MultiBooks>() {
            @Override
            public void onResponse(Call<MultiBooks> call, Response<MultiBooks> response) {
                RV_4.setVisibility(View.VISIBLE);
                shimmer_4.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    for(int i = 0; i < response.body().getBooks().size(); i++) {
                        discoverAdapter = new DiscoverAdapter(getContext(), response.body().getBooks());
                        cat4_LM = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        RV_4.setLayoutManager(cat4_LM);
                        RV_4.setAdapter(discoverAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<MultiBooks> call, Throwable t) {
                RV_4.setVisibility(View.GONE);
                shimmer_4.setVisibility(View.GONE);
                Err_4.setVisibility(View.VISIBLE);
                Err_4.setText(t.getMessage());
            }
        });

    }
}
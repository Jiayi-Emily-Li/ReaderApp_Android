package edu.northeastern.cs5520group7;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.cs5520group7.Adapter.DiscoverAdapter;
import edu.northeastern.cs5520group7.model.HTTPController;
import edu.northeastern.cs5520group7.model.api.Items;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    HTTPController httpController;
    Call<Items> cat1_Call, cat2_Call, cat3_Call, cat4_Call;

    Spinner cat_spinner;
    ShimmerFrameLayout shimmer_1, shimmer_2, shimmer_3, shimmer_4;
    TextView roll_1tv, roll_2tv, roll_3tv, roll_4tv, Err_1, Err_2, Err_3, Err_4;
    RecyclerView RV_1, RV_2, RV_3, RV_4;
    DiscoverAdapter discoverAdapter;
    LinearLayoutManager cat1_LM, cat2_LM, cat3_LM, cat4_LM;
    SwipeRefreshLayout swipeRefreshLayout;

    List<String> readerFavCat;
    String cat_1, cat_2, cat_3, cat_4;

    FirebaseUser FbUser;
    String curUid;
    DatabaseReference bookAddedRef;
    List<String> sortedPrefList;


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
        getActivity().setTitle("Readers");

        RV_1 = view.findViewById(R.id.RV_1);
        RV_2 = view.findViewById(R.id.RV_2);
        RV_3 = view.findViewById(R.id.RV_3);
        RV_4 = view.findViewById(R.id.RV_4);
        shimmer_1 = view.findViewById(R.id.shimmer_1);
        shimmer_2 = view.findViewById(R.id.shimmer_2);
        shimmer_3 = view.findViewById(R.id.shimmer_3);
        shimmer_4 = view.findViewById(R.id.shimmer_4);
        roll_1tv = view.findViewById(R.id.Roll_1_title);
        roll_2tv = view.findViewById(R.id.Roll_2_title);
        roll_3tv = view.findViewById(R.id.Roll_3_title);
        roll_4tv = view.findViewById(R.id.Roll_4_titile);

        FbUser = FirebaseAuth.getInstance().getCurrentUser();
        curUid = FbUser.getUid();
        bookAddedRef = FirebaseDatabase.getInstance().getReference("readers/" + curUid +"/book_added");

        swipeRefreshLayout = view.findViewById(R.id.discoverPg);
        swipeRefreshLayout.setOnRefreshListener(this);

        sortedPrefList= new ArrayList<>();

        //drop-down menu
        cat_spinner = view.findViewById(R.id.cat_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_choices, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_spinner.setAdapter(spinnerAdapter);
        cat_spinner.setSelection(0);
        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                }else{
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    if (selectedItem.equals("Biography")){selectedItem = "biography&autobiography";}
                    else if(selectedItem.equals("Business")){selectedItem = "investing+business";}
                    else if(selectedItem.equals("Children")){selectedItem = "childrens";}
                    else if (selectedItem.equals("Comics")){selectedItem = "comics&graphic novels";}
                    else if (selectedItem.equals("Fantasy")){selectedItem = "fiction+fantasy";}
                    else if (selectedItem.equals("Fiction")){selectedItem = "young+fiction";}
                    else if (selectedItem.equals("Thriller")){selectedItem = "thriller+horror+suspense";}
                    else if (selectedItem.equals("Travel")){selectedItem = "travel+travelling";}
                    Intent intent = new Intent(getContext(), DiscoverSingleCategory.class);
                    intent.putExtra("categoryName", selectedItem);
                    getContext().startActivity(intent);
            }}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        httpController = Retrofit_Book.getAPI();

        customizedRecommendation();

        return view;
    }


    //reloading result when refresh the discover page
    @Override
    public void onRefresh() {
        hideRVs();
        showShimmers();
        customizedRecommendation();
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


    //get the recommended books recap information from google books
    private void getRecommendBooks(String cat_1, String cat_2, String cat_3, String cat_4) {
        callRecommend_1(cat_1);
        callRecommend_2(cat_2);
        callRecommend_3(cat_3);
        callRecommend_4(cat_4);
    }

    //get the first category - information and display
    private void callRecommend_1(String cat1) {
        cat1_Call = httpController.getCategoryResults("categories:" + cat1,"ebooks", "relevance", 40);
        cat1_Call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                RV_1.setVisibility(View.VISIBLE);
                shimmer_1.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    for(int i = 0; i < response.body().getBooks().size(); i++) {
                        discoverAdapter = new DiscoverAdapter(getContext(), response.body().getBooks());
                        cat1_LM = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        RV_1.setLayoutManager(cat1_LM);
                        RV_1.setAdapter(discoverAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                RV_1.setVisibility(View.GONE);
                shimmer_1.setVisibility(View.GONE);
                Err_1.setVisibility(View.VISIBLE);
                Err_1.setText(t.getMessage());


            }
        });

    }

    //get the second category - information and display
    private void callRecommend_2(String cat2) {
        cat2_Call = httpController.getCategoryResults("categories:" + cat2,"ebooks", "relevance", 40);
        cat2_Call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
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
            public void onFailure(Call<Items> call, Throwable t) {
                RV_2.setVisibility(View.GONE);
                shimmer_2.setVisibility(View.GONE);
                Err_2.setVisibility(View.VISIBLE);
                Err_2.setText(t.getMessage());


            }
        });

    }

    //get the third category - information and display
    private void callRecommend_3(String cat3) {
        cat3_Call = httpController.getCategoryResults("categories:" + cat3,"ebooks", "relevance", 40);
        cat3_Call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
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
            public void onFailure(Call<Items> call, Throwable t) {
                RV_3.setVisibility(View.GONE);
                shimmer_3.setVisibility(View.GONE);
                Err_3.setVisibility(View.VISIBLE);
                Err_3.setText(t.getMessage());
            }
        });


    }

    //get the fourth category - information and display
    private void callRecommend_4(String cat4) {
        cat4_Call = httpController.getCategoryResults("categories:"+ cat4,"ebooks", "relevance", 40);
        cat4_Call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
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
            public void onFailure(Call<Items> call, Throwable t) {
                RV_4.setVisibility(View.GONE);
                shimmer_4.setVisibility(View.GONE);
                Err_4.setVisibility(View.VISIBLE);
                Err_4.setText(t.getMessage());
            }
        });

    }


    //retrieve reader's book list and get the most favorite categories
    private void customizedRecommendation(){
        List<String> output = new ArrayList<>();
        bookAddedRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot singleBookDataSnapshot: snapshot.getChildren()){
                    String category = singleBookDataSnapshot.child("category").getValue(String.class);
                    String[] parts = new String[30];
                    if(category != null){
                        parts = category.split("[/&]");
                        output.add(parts[0]);
                    }else{
                    }
                }
                Log.d("initial",output.toString());
                cat_1 = "young+fiction";
                cat_2 = "romance";
                cat_3 = "thriller+horror+suspense";
                cat_4 = "childrens";
                readerFavCat = new ArrayList<>(Arrays.asList(cat_1, cat_2, cat_3, cat_4));
                Log.d("new user", readerFavCat.toString());
                if(output.size() == 0){
                    //default recommend result for new users or users without book list
                    getRecommendBooks(cat_1, cat_2, cat_3, cat_4);
                    roll_1tv.setText("Fiction");
                    roll_2tv.setText("Romance");
                    roll_3tv.setText("Thriller");
                    roll_4tv.setText("Children");
                }else{
                    sortPrefList(output);
                    Log.d("sortedList", sortedPrefList.toString());
                    for(int i = 0; i < sortedPrefList.size() && i < 4; i++) {
                        String queryKeyword = sortedPrefList.get(i);
                        if (queryKeyword.equals("Biography ")){readerFavCat.set(i,"biography&autobiography");}
                        else if(queryKeyword.equals("Business ")){readerFavCat.set(i,"investing+business");}
                        else if(queryKeyword.equals("Children ")){readerFavCat.set(i,"childrens");}
                        else if (queryKeyword.equals("Comics ")){readerFavCat.set(i,"comics&graphic novels");}
                        else if (queryKeyword.equals("Fantasy ")){readerFavCat.set(i,"fiction+fantasy");}
                        else if (queryKeyword.equals("Fiction ")){readerFavCat.set(i,"young+fiction");}
                        else if (queryKeyword.equals("Thriller ")){readerFavCat.set(i,"thriller+horror+suspense");}
                        else if (queryKeyword.equals("Travel ")){readerFavCat.set(i,"travel+travelling");}
                        else{readerFavCat.set(i,queryKeyword);}
                    }
                    Log.d("exist user", readerFavCat.toString());
                    getRecommendBooks(readerFavCat.get(0), readerFavCat.get(1),readerFavCat.get(2), readerFavCat.get(3));
                    if(sortedPrefList.size()==1){roll_1tv.setText(sortedPrefList.get(0)); roll_2tv.setText("Romance"); roll_3tv.setText("Thriller");
                            roll_4tv.setText("Children");}
                    else if(sortedPrefList.size()==2){roll_1tv.setText(sortedPrefList.get(0)); roll_2tv.setText(sortedPrefList.get(1)); roll_3tv.setText("Thriller");
                        roll_4tv.setText("Children");}
                    else if(sortedPrefList.size()==3){roll_1tv.setText(sortedPrefList.get(0)); roll_2tv.setText(sortedPrefList.get(1));
                        roll_3tv.setText(sortedPrefList.get(2)); roll_4tv.setText("Children");}
                    else if(sortedPrefList.size()==4) {
                        roll_1tv.setText(sortedPrefList.get(0));
                        roll_2tv.setText(sortedPrefList.get(1));
                        roll_3tv.setText(sortedPrefList.get(2));
                        roll_4tv.setText(sortedPrefList.get(3));
                    }
                    else{}

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //sort the favorite category list by frequency
    private void sortPrefList(List<String> prefList){
        Map<String, Integer> mapPrefList = new HashMap<>();

        for (String category: prefList) {
            if(category.equals("Juvenile Fiction ")){
                category = "Fiction ";
            }
            int count = mapPrefList.getOrDefault(category, 0);
            mapPrefList.put(category, count+1);
        }

        List<Pair<Integer, String>> vec = new ArrayList<>();

        for(String string: mapPrefList.keySet()){
            vec.add(new Pair(mapPrefList.get(string), string));
        }

        Collections.sort(vec, (x,y) -> {
           if(x.key < y.key){
               return 1;
           }else if (x.key > y.key) {
               return -1;
           }else{
               if(x.val.compareTo(y.val) < 0){
                   return 1;
               } else {
                   return -1;
               }
           }
        });

        for(int i = 0 ; i < vec.size() && i < 4 ; i++){
           sortedPrefList.add(vec.get(i).val);
        }
    }

    //helper class
    class Pair<K, V> {
        K key;
        V val;

        Pair(K key, V value)
        {
            this.key =key;
            this.val = value;
        }
    }

}
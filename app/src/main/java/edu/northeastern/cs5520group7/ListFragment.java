package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import edu.northeastern.cs5520group7.Adapter.DiscoverAdapter;
import edu.northeastern.cs5520group7.Adapter.ListAdapter;
import edu.northeastern.cs5520group7.model.Book;
import edu.northeastern.cs5520group7.model.HTTPController;
import edu.northeastern.cs5520group7.model.api.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    HTTPController httpController;
    //Call<MultiBooks> list_Call;
    RecyclerView RV_1;
    ShimmerFrameLayout shimmer_1;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseUser FBUser;
    DatabaseReference bookAddedRef;
    String curUid;
    String bookId;
    List<Item> bookList = new ArrayList<>();
    ListAdapter listAdapter;
    DiscoverAdapter discoverAdapter;
    TextView Err_1;
    //RecyclerView recyclerView;
    LinearLayoutManager lm;
    Call<Item> book_call;

    public ListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RV_1 = (RecyclerView) view.findViewById(R.id.RV_1);
        shimmer_1 = view.findViewById(R.id.shimmer_1);
        swipeRefreshLayout = view.findViewById(R.id.listPg);

        httpController = Retrofit_Book.getAPI();
        FBUser = FirebaseAuth.getInstance().getCurrentUser();
        curUid = FBUser.getUid();
        //bookId = FirebaseDatabase.getInstance().getReference("readers/"+curUid+"/book_added/");
        bookAddedRef = FirebaseDatabase.getInstance().getReference().child("readers").child(curUid).child("book_added");
        getBookList();
        return view;
    }

    @Override
    public void onRefresh() {
        showShimmers();
        getBookList();
    }

    public void showShimmers(){
        shimmer_1.setVisibility(View.VISIBLE);
    }

    public void getBookList(){
        bookAddedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot bookSnapshot: snapshot.getChildren()){
                    Log.d("bookSnapshot", bookSnapshot.toString());
                    Book book = bookSnapshot.getValue(Book.class);
                    //bookList.add(book);
                    book_call = httpController.getBookItem(book.getBookId());
                    book_call.enqueue(new Callback<Item>() {
                        @Override
                        public void onResponse(Call<Item> call, Response<Item> response) {
                            RV_1.setVisibility(View.VISIBLE);
                            shimmer_1.setVisibility(View.GONE);
                            if(response.isSuccessful()) {
                                bookList.add(response.body());
//                                listAdapter = new ListAdapter(getContext(), response.body().getVolumeInfo());
//                                lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//                                RV_1.setLayoutManager(lm);
//                                RV_1.setAdapter(listAdapter);


                            }
                        }

                        @Override
                        public void onFailure(Call<Item> call, Throwable t) {
                            RV_1.setVisibility(View.GONE);
                            shimmer_1.setVisibility(View.GONE);
                            Err_1.setVisibility(View.VISIBLE);
                            Err_1.setText(t.getMessage());


                        }
                    });
                }
                //Log.d("bookList", bookList.toString());

                listAdapter = new ListAdapter(getContext(), bookList);
                lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                RV_1.setLayoutManager(lm);
                RV_1.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
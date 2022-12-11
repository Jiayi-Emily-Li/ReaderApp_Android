package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;

import edu.northeastern.cs5520group7.model.HTTPController;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.cs5520group7.Adapter.ReviewAdapter;
import edu.northeastern.cs5520group7.Adapter.DiscoverAdapter;
import edu.northeastern.cs5520group7.model.HTTPController;
import edu.northeastern.cs5520group7.model.Review;
import edu.northeastern.cs5520group7.model.Book;
import edu.northeastern.cs5520group7.model.api.Item;
import edu.northeastern.cs5520group7.model.api.VolumeInfo;

import java.util.ArrayList;
import java.util.List;

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
    List<Item> bookList;
    DiscoverAdapter discoverAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager lm;

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
        RV_1 = view.findViewById(R.id.RV_1);
        shimmer_1 = view.findViewById(R.id.shimmer_1);
        swipeRefreshLayout = view.findViewById(R.id.listPg);

        httpController = Retrofit_Book.getAPI();
        FBUser = FirebaseAuth.getInstance().getCurrentUser();
        curUid = FBUser.getUid();
        //bookId = FirebaseDatabase.getInstance().getReference("readers/"+curUid+"/book_added/");
        bookAddedRef = FirebaseDatabase.getInstance().getReference("readers/"+curUid+"/book_added/"+bookId);
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
        bookAddedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot bookSnapshot: snapshot.getChildren()){
                    Log.d("bookSnapshot", bookSnapshot.toString());
                    Item book = bookSnapshot.getValue(Item.class);
                    bookList.add(book);
                }
                Log.d("bookList", bookList.toString());
                discoverAdapter = new DiscoverAdapter(getContext(), bookList);
                lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(lm);
                recyclerView.setAdapter(discoverAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
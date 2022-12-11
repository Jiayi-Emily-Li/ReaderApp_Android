package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.List;

import edu.northeastern.cs5520group7.Adapter.ListAdapter;
import edu.northeastern.cs5520group7.model.Book;
import edu.northeastern.cs5520group7.model.HTTPController;

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
    List<Book> bookList;
    ListAdapter listAdapter;
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
                    bookList.add(book);
                }
                Log.d("bookList", bookList.toString());
                listAdapter = new ListAdapter(getContext(), bookList);
                lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(lm);
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
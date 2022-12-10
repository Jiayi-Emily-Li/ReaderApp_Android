package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import edu.northeastern.cs5520group7.model.HTTPController;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    HTTPController httpController;
    RecyclerView RV_1;
    ShimmerFrameLayout shimmer_1;
    SwipeRefreshLayout swipeRefreshLayout;

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

    }
}
package edu.northeastern.cs5520group7.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.cs5520group7.R;
import edu.northeastern.cs5520group7.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context myContext;
    private List<Review> reviewList;

    public ReviewAdapter(Context myContext, List<Review> reviewList){
        this.reviewList = reviewList;
        this.myContext = myContext;
    }


    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.review_line_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.reviewTV.setText(review.getReview());
        holder.reviewRB.setRating(Float.parseFloat(review.getRating()));
        holder.commenter.setText(review.getCommenter());

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView reviewTV, commenter;
        RatingBar reviewRB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewTV = itemView.findViewById(R.id.reviewTV);
            commenter = itemView.findViewById(R.id.review_user);
            reviewRB = itemView.findViewById(R.id.review_RB);
        }
    }
}

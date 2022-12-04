package edu.northeastern.cs5520group7.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.northeastern.cs5520group7.BookInfoPage;
import edu.northeastern.cs5520group7.R;
import edu.northeastern.cs5520group7.model.api.Book;

public class SingleCategoryAdapter extends RecyclerView.Adapter<SingleCategoryAdapter.ViewHolder> {

    private Context context;
    private List<Book> books;


    public SingleCategoryAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.discover_book_item_hor, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        //while single book is clicked, send bookId to start a new activity to show the book information
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookId = books.get(viewHolder.getAdapterPosition()).getId();
                Log.d("bookId", bookId);
                Intent intent = new Intent(view.getContext(), BookInfoPage.class);
                intent.putExtra("bookId", bookId);
                view.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView")int position) {
        Book book = books.get(position);

        //set book title
        try{
            holder.sCatBookTitle.setText(book.getVolumeInfo().getTitle());
        } catch (Exception e) {
            holder.sCatBookTitle.setText("-");
        };

        //set book image
        try {
            Glide.with(context).load(book.getVolumeInfo().getImageLinks().getSmallThumbnail()).centerCrop().into(holder.sCatBookIV);
        } catch (Exception exception) {
            //if retrieve failed, use default book image instead
            holder.sCatBookIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_book_img));
        }

        //set author
        try{
            Integer authorNum = book.getVolumeInfo().getAuthors().size();
            StringBuilder authorNames= new StringBuilder();
            for (int i = 0; i < authorNum; i++){
                authorNames.append(book.getVolumeInfo().getAuthors().get(i));
                authorNames.append(" ");
            }
            holder.sCatBookAuthor.setText("By " + authorNames);
        } catch (Exception e) {
                holder.sCatBookAuthor.setText("-");
        }

        //set book page count
         try{
            holder.sCatBookPgCt.setText("Pages: " + String.valueOf(book.getVolumeInfo().getPageCount()));
         } catch (Exception e) {
             holder.sCatBookPgCt.setText("-");
         }


        //setRatingbar
        try{
            holder.sCatBookRating.setRating(book.getVolumeInfo().getAverageRating());
        } catch (Exception e){
        };

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sCatBookIV;
        TextView sCatBookTitle, sCatBookAuthor, sCatBookPgCt;
        RatingBar sCatBookRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sCatBookIV = itemView.findViewById(R.id.singleCatBookImg);
            sCatBookTitle = itemView.findViewById(R.id.singleCatBookTitle);
            sCatBookAuthor = itemView.findViewById(R.id.singleCatBookAuthor);
            sCatBookRating = itemView.findViewById(R.id.singleCatBookRating);
            sCatBookPgCt = itemView.findViewById(R.id.singleCatBookPgCt);

        }
    }
}

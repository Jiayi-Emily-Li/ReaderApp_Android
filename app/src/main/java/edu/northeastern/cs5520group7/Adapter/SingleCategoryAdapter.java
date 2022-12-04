package edu.northeastern.cs5520group7.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView")int position) {
        Book book = books.get(position);

        //set book title
        holder.sCatBookTitle.setText(book.getVolumeInfo().getTitle());

        //set book image
        try {
            InputStream bookImgStream = (InputStream) new URL(book.getVolumeInfo().getImageLinks().getSmallThumbnail()).getContent();
            holder.sCatBookIV.setImageBitmap(BitmapFactory.decodeStream(bookImgStream));
        } catch (Exception exception) {
            //if retrieve failed, use default book image instead
            holder.sCatBookIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_book_img));
        }

        //set author
        Integer authorNum = book.getVolumeInfo().getAuthors().size();
        if (authorNum == 0){
            holder.sCatBookAuthor.setText("-");
        } else {
            StringBuilder authorNames= new StringBuilder();
            for (int i = 0; i < authorNum; i++){
                authorNames.append(book.getVolumeInfo().getAuthors().get(i));
                authorNames.append(" ");
            }
            holder.sCatBookAuthor.setText("By " + authorNames.toString());
        }

        //set category

        holder.sCatBookPgCt.setText("Pages: " + book.getVolumeInfo().getPageCount().toString());

        //setRatingbar
        Float rate = book.getVolumeInfo().getAverageRating();
        if(rate == null){
        } else {
            holder.sCatBookRating.setRating(rate);
        }
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

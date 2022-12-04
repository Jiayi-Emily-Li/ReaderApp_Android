package edu.northeastern.cs5520group7.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import edu.northeastern.cs5520group7.BookInfoPage;
import edu.northeastern.cs5520group7.R;
import edu.northeastern.cs5520group7.model.api.Book;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {


    private Context context;
    private List<Book> books;

    public DiscoverAdapter(Context context, List<Book> books){
        this.context = context;
        this.books = books;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.discover_book_item_vert, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //while single book get selected, sent bookId to single book info page
                String bookId = books.get(viewHolder.getAdapterPosition()).getId();
                Intent intent = new Intent(view.getContext(), BookInfoPage.class);
                intent.putExtra("bookId", bookId);
                view.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Book book = books.get(position);

        //set title
        try{
        holder.titleTV_vert.setText(book.getVolumeInfo().getTitle());
        } catch (Exception e) {
            holder.titleTV_vert.setText("-");
        };

        //set image -try to retrieve image through Image link
        Log.d("imagelink", book.getVolumeInfo().getImageLinks().getSmallThumbnail());
        try {
            InputStream bookImgStream = (InputStream) new URL(book.getVolumeInfo().getImageLinks().getSmallThumbnail()).getContent();
            holder.iv_vert.setImageBitmap(BitmapFactory.decodeStream(bookImgStream));
        } catch (Exception exception) {
            //if retrieve failed, use default book image instead
            holder.iv_vert.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.default_book_img));
        }

        //set author name - may have different amount of authors
        try{
            Integer authorNum = book.getVolumeInfo().getAuthors().size();
            String authorNames= "";
            for (int i = 0; i < authorNum; i++){
                    authorNames = book.getVolumeInfo().getAuthors().get(i) + " ";
                    holder.authorTV_vert.setText("By " + authorNames.toString());
                }
            Log.d("viewHolder", holder.authorTV_vert.toString());
        } catch (Exception e) {
                holder.authorTV_vert.setText("-");
            }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV_vert, authorTV_vert;
        ImageView iv_vert;
        RatingBar aveRB_vert;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV_vert = itemView.findViewById(R.id.titleTV_vert);
            authorTV_vert = itemView.findViewById(R.id.authorTV_vert);
            iv_vert= itemView.findViewById(R.id.IV_vert);
            aveRB_vert = itemView.findViewById(R.id.aveRB_vert);
        }
    }
}

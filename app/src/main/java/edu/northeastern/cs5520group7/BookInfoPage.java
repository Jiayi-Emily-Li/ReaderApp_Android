package edu.northeastern.cs5520group7;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.net.URL;

import edu.northeastern.cs5520group7.model.HTTPController;
import edu.northeastern.cs5520group7.model.api.Book;
import edu.northeastern.cs5520group7.model.api.VolumeInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookInfoPage extends AppCompatActivity {

    TextView titleTV, authorTV, ratingTV, languageTV, pageCountTV;
    TextView descriptionTV, publisherTV, publishedDateTV, isbnTV, categoriesTV, maturityTV;
    ImageView bookIV;
    RatingBar ratingBar;
    Button bookLinkBtn;

    HTTPController httpController;
    Call<Book> bookInfoCall;

    String bookId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_page);

        bookIV = findViewById(R.id.bookIV);
        titleTV = findViewById(R.id.titleTV);
        authorTV = findViewById(R.id.authorTV);
        ratingTV = findViewById(R.id.ratingsTV);
        languageTV = findViewById(R.id.languageTV);
        pageCountTV = findViewById(R.id.pageCountTV);
        ratingBar = findViewById(R.id.averageRatingRB);

        descriptionTV = findViewById(R.id.descriptionTV);
        publisherTV = findViewById(R.id.publisherTV);
        publishedDateTV = findViewById(R.id.publishedDateTV);
        isbnTV = findViewById(R.id.isbnTV);
        categoriesTV = findViewById(R.id.categoriesTV);
        maturityTV = findViewById(R.id.maturityRatingTV);
        bookLinkBtn = findViewById(R.id.bookLink);

        httpController = Retrofit_Book.getAPI();

        bookId = getIntent().getStringExtra("bookId");
        Log.d("bookId received in Book info", bookId);

            RetrieveBookInfo(bookId);


    }

    private void RetrieveBookInfo(String id){
        Log.d("bookId request", id);
        bookInfoCall = httpController.getBookItem(id);
        bookInfoCall.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book = response.body();
                VolumeInfo volumeInfo = response.body().getVolumeInfo();
                if (response.isSuccessful()) {

                    //set page title
                    setTitle(volumeInfo.getTitle());

                    /*---Basic Book Information---*/
                    //set bookTitle
                    titleTV.setText(volumeInfo.getTitle());
                    Log.d("titleTV retrieved", volumeInfo.getTitle());

                    //set book image
                    try{
                        URL url = new URL(volumeInfo.getImageLinks().getSmallThumbnail());
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        bookIV.setImageBitmap(bmp);
                    } catch (Exception e) {
                        bookIV.setImageDrawable(ContextCompat.getDrawable(BookInfoPage.this, R.drawable.default_book_img));
                    }

                    //setRatingbar
                    Float rate = volumeInfo.getAverageRating();
                    if(rate == null){
                    } else {
                        ratingBar.setRating(rate);
                    }

                    //set rating count
                    try {
                        ratingTV.setText(" - " + volumeInfo.getRatingCount() + " ratings");
                    } catch (Exception e) {
                    }

                    //set page count
                    try{
                        pageCountTV.setText(String.valueOf(volumeInfo.getPageCount()));
                    } catch (Exception e) {
                        pageCountTV.setText("-");
                    }

                    //set language
                    languageTV.setText(volumeInfo.getLanguage());
                    Log.d("language", volumeInfo.getLanguage());

                    /*---Additional Book Information---*/
                    //set publisher
                    try{
                        publisherTV.setText(volumeInfo.getPublisher());
                    } catch (Exception e){
                        publisherTV.setText("-");
                    }

                    //set publishDate
                    try {
                        publishedDateTV.setText(volumeInfo.getPublishedDate());
                    } catch(Exception e) {
                        publishedDateTV.setText("-");
                    }

                    //set ISBN
                    Integer isbnNum = volumeInfo.getIndustryIdentifiers().size();
                    String isbn = "";
                    if (isbnNum != 0) {
                        for (int i = 0; i < isbnNum; i++) {
                            isbn = volumeInfo.getIndustryIdentifiers().get(i).getIdentifier() + "\n";
                            isbnTV.append(isbn);
                        }
                    } else {
                        isbnTV.setText("-");
                    }

                    //set Categories
                    Integer catNum = volumeInfo.getCategories().size();
                    String categories = "";
                    if (catNum != 0) {
                        for (int m = 0; m < catNum; m++) {
                            categories = volumeInfo.getCategories().get(m) + "\n";
                            categoriesTV.append(categories);
                        }
                    }else {
                        categoriesTV.setText("-");
                    }

                    //set maturity rating
                    try {
                        maturityTV.setText(volumeInfo.getMaturityRating());
                    }catch (Exception e) {
                        maturityTV.setText("-");
                    }

                    bookLinkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(volumeInfo.getPreviewLink()));
                            startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(BookInfoPage.this, volumeInfo.getTitle() + "cannot be accessed online", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                //do nothing
            }
        });


    }
}
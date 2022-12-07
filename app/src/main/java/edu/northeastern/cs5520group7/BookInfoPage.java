package edu.northeastern.cs5520group7;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.cs5520group7.model.HTTPController;
import edu.northeastern.cs5520group7.model.api.Book;
import edu.northeastern.cs5520group7.model.api.VolumeInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookInfoPage extends AppCompatActivity implements View.OnClickListener {

    TextView titleTV, authorTV, ratingTV, languageTV, pageCountTV;
    TextView descriptionTV, publisherTV, publishedDateTV, isbnTV, categoriesTV, maturityTV;
    ImageView bookIV;
    RatingBar ratingBar;
    Button bookLinkBtn, activeBookmarkBtn, inactiveBookmarkBtn;
    FirebaseUser FBUser;

    HTTPController httpController;
    Call<Book> bookInfoCall;
    String bookId;

    DatabaseReference readerRef;
    DatabaseReference curReaderCurBookRef;
    String curUid;
    Boolean checkStatus;
    Boolean checkUser;
    Map<String, String> reviews;


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

        //retrieve book information
        httpController = Retrofit_Book.getAPI();
        bookId = getIntent().getStringExtra("bookId");
        Log.d("bookId received in Book info", bookId);
        RetrieveBookInfo(bookId);


        //set the bookmark
        activeBookmarkBtn = findViewById(R.id.activeBookmark);
        inactiveBookmarkBtn = findViewById(R.id.inactiveBookmark);

        FBUser = FirebaseAuth.getInstance().getCurrentUser();
        curUid = FBUser.getUid();

        //get ref from DB
        readerRef = FirebaseDatabase.getInstance().getReference("readers");
        curReaderCurBookRef = FirebaseDatabase.getInstance().getReference("readers/"+curUid+"/book_added/"+bookId);

        reviews = new HashMap<>();


        checkUserBookList(bookId);
        if(!checkStatus){
            activeBookmarkBtn.setVisibility(View.GONE);
            inactiveBookmarkBtn.setVisibility(View.VISIBLE);
        } else {
            activeBookmarkBtn.setVisibility(View.VISIBLE);
            inactiveBookmarkBtn.setVisibility(View.GONE);
        }
        Log.d("checkTF", checkStatus.toString());
        activeBookmarkBtn.setOnClickListener(this);
        inactiveBookmarkBtn.setOnClickListener(this);
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
                        Glide.with(getApplicationContext()).load(volumeInfo.getImageLinks().getSmallThumbnail()).centerCrop().into(bookIV);
                    } catch (Exception e) {
                        bookIV.setImageDrawable(ContextCompat.getDrawable(BookInfoPage.this, R.drawable.default_book_img));
                    }

                    //setRatingbar
                    try {
                        Float rate = volumeInfo.getAverageRating();
                        ratingBar.setRating(rate);
                    }catch (Exception e) {

                    }

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


                    //set description
                    try {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            descriptionTV.setText(Html.fromHtml(volumeInfo.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                        }else {
                            descriptionTV.setText(Html.fromHtml(volumeInfo.getDescription()));
                        }
                    } catch (Exception e) {
                        descriptionTV.setText("-");
                    }

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
                    try{
                        Integer isbnNum = volumeInfo.getIndustryIdentifiers().size();
                        String isbn = "";
                        for (int i = 0; i < isbnNum; i++) {
                            isbn = volumeInfo.getIndustryIdentifiers().get(i).getIdentifier() + "\n";
                            isbnTV.append(isbn);
                        }
                    }catch (Exception e) {
                            isbnTV.setText("-");
                    }


                    //set Categories
                    try {
                        Integer catNum = volumeInfo.getCategories().size();
                        String categories = "";
                        for (int m = 0; m < catNum; m++) {
                            categories = volumeInfo.getCategories().get(m) + "\n";
                            categoriesTV.append(categories);
                        }
                    } catch (Exception e){
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

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                //do nothing
            }
        });
    }

    private void checkUserBookList(String bookId) {
        checkStatus = false;
        checkUser = false;
        readerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkStatus = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("dataSnapshot", dataSnapshot.toString());
                    if (dataSnapshot.getValue().equals(curUid)) {
                        checkUser = true;
                        if (dataSnapshot.child("book_added").hasChild(bookId)) {
                            Log.d("2", "bookAddedList");
                            checkStatus = true;
                        }
                    }

                    if(dataSnapshot.child("book_added").getKey().equals(bookId)){
                        int count = 0;
                        for(DataSnapshot reviewSnapshot: dataSnapshot.child("book_add/"+bookId+"/reviews").getChildren()) {
                            String review = reviewSnapshot.getValue(String.class);
                            reviews.put(String.valueOf(count), review);
                            count++;
                        }
                    }
                }
                if(!checkUser){
                    readerRef.child(curUid).child("email").setValue(FBUser.getEmail());
                    checkUser = true;
                }
            };

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activeBookmark:
                removeBookmark();
                break;

            case R.id.inactiveBookmark:
                addBookmark();
                break;
        }
    }

    private void addBookmark() {
        inactiveBookmarkBtn.setVisibility(View.GONE);
        activeBookmarkBtn.setVisibility(View.VISIBLE);
        curReaderCurBookRef.child("author").setValue(authorTV.getText().toString());
        curReaderCurBookRef.child("bookId").setValue(bookId);
        curReaderCurBookRef.child("bookname").setValue(titleTV.getText().toString());
        curReaderCurBookRef.child("rating").setValue(String.valueOf(ratingBar.getRating()));
        curReaderCurBookRef.child("category").setValue(categoriesTV.getText().toString());
        curReaderCurBookRef.child("reviews").setValue(reviews);
        checkStatus = true;
    }

    private void removeBookmark() {
        activeBookmarkBtn.setVisibility(View.GONE);
        inactiveBookmarkBtn.setVisibility(View.VISIBLE);
        curReaderCurBookRef.removeValue();
        checkStatus = false;
    }



}
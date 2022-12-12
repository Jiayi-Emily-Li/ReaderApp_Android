package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakePost extends AppCompatActivity {

    private Button postButton;
    private EditText postReviewText;
    DatabaseReference reviewAddedRef;
    DatabaseReference commenterRef;
    FirebaseUser FBUser;
    String curUid;
    String bookId;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_post);
        postReviewText = findViewById(R.id.postReviewText);
        postButton = findViewById(R.id.postReviewButton);

        FBUser = FirebaseAuth.getInstance().getCurrentUser();
        curUid = FBUser.getUid();
        Bundle extras = getIntent().getExtras();
        bookId = extras.getString("bookId");
        Log.d("bookId", bookId.toString());
        reviewAddedRef = FirebaseDatabase.getInstance().getReference().child("reviews").child(bookId).child(curUid);
        commenterRef = FirebaseDatabase.getInstance().getReference().child("readers").child(curUid);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
            }
        });
    }

    private void addReview(){
//        commenterRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot commenterSnapshot: snapshot.getChildren()){
////                    Reader commenter = commenterSnapshot.getValue(Reader.class);
////                    reviewAddedRef.child("commenter").setValue(commenter.getEmail());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String commenter = mUser.getEmail();
        String review = postReviewText.getText().toString();
        reviewAddedRef.child("commenter").setValue(commenter);
        reviewAddedRef.child("review").setValue(review);
    }


}

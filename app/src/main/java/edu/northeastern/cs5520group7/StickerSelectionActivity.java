package edu.northeastern.cs5520group7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.northeastern.cs5520group7.model.History;
import edu.northeastern.cs5520group7.model.User;

public class StickerSelectionActivity extends AppCompatActivity {

    ImageButton starBtn;
    ImageButton radioBtn;
    ImageButton crossBtn;
    String currentUser;
    User sender;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_selection);

        starBtn = (ImageButton) findViewById(R.id.starBtn);
        radioBtn = (ImageButton) findViewById(R.id.radioBtn);
        crossBtn = (ImageButton) findViewById(R.id.crossBtn);


        String clickedName = getIntent().getStringExtra("clickedName");
        currentUser = getIntent().getStringExtra("currentUser");
        Log.d("clickedName", clickedName);
        Log.d("currentUser", currentUser);

        userRef = FirebaseDatabase.getInstance().getReference("users");
        Date currentTime = Calendar.getInstance().getTime();
        String time = currentTime.toString();


        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHistoryValue(currentUser, clickedName, time, "star");


            }
        });

        radioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHistoryValue(currentUser, clickedName, time, "radio");
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHistoryValue(currentUser, clickedName, time, "cross");
            }
        });
    }
        private void updateHistoryValue(String from, String to, String time, String image){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getName() == currentUser) {
                        sender.setName(currentUser);
                        sender.setToken(user.getToken());
                        String id = user.getUserId();
                        sender.setUserId(id);
                        for(History history : user.getHistories()){
                            sender.addHistory(history.getFrom(), history.getTo(), history.getTime(), history.getImage());
                        }
                        sender.addHistory(from, to, time, image);
                    }
                }
                userRef.child(currentUser).setValue(sender);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
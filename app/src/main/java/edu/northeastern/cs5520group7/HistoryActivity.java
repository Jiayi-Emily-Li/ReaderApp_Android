package edu.northeastern.cs5520group7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs5520group7.model.History;
import edu.northeastern.cs5520group7.model.User;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseDatabase uDatabase;
    private DatabaseReference userRef;
    private TextView starCountSent;
    private String name;
    private static final String TAG = "HistoryActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        name = getIntent().getStringExtra("name");
        Log.d(TAG, "name is " + name);

        starCountSent = (TextView) findViewById(R.id.startCount_sent);

        // connect with Firebase
        userRef = FirebaseDatabase.getInstance().getReference("users");
        display();
    }

    public void display() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user = dataSnapshot.getValue(User.class);
                    if (user.getName().equals(name)) {
                        break;
                    }
                }

                if (user != null) {
                    List<History> fromHistoryList = new ArrayList<>();
                    List<History> toHistoryList = new ArrayList<>();
                    for (History history : user.getHistories()) {
                        if (history.getFrom().equals(name)) {
                            fromHistoryList.add(history);
                        }

                        if (history.getTo().equals(name)) {
                            toHistoryList.add(history);
                        }
                    }


                    long starCount = fromHistoryList.stream().filter(history -> history.getImage().equals("star")).count();
                    starCountSent.setText(String.valueOf(starCount));
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

//                Log.w(TAG,"Fail to read data", error.toException());
            }
        });
    }

}
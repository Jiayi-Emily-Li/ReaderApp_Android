package edu.northeastern.cs5520group7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
    private TextView starTextReceived;

    private TextView radioCountSent;
    private TextView radioTextReceived;

    private TextView crossCountSent;
    private TextView crossTextReceived;
    private String name;
    private static final String TAG = "HistoryActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        name = getIntent().getStringExtra("name");
        Log.d(TAG, "name is " + name);
        starCountSent = (TextView) findViewById(R.id.startCount_sent);
        starTextReceived = (TextView) findViewById(R.id.startText_received);

        radioCountSent = (TextView) findViewById(R.id.radioCount_sent);
        radioTextReceived = (TextView) findViewById(R.id.radioText_received);

        crossCountSent = (TextView) findViewById(R.id.crossCount_sent);
        crossTextReceived = (TextView) findViewById(R.id.crossText_received);
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

                    if (user.getHistories() != null) {
                        for (History history : user.getHistories().values()) {
                            if (history.getFrom().equals(name)) {
                                Log.d(TAG, "From History " + history.toString());
                                fromHistoryList.add(history);
                            }

                            if (history.getTo().equals(name)) {
                                Log.d(TAG, "To history " + history.toString());
                                toHistoryList.add(history);
                            }
                        }
                    }


                    starCountSent.setText(String.valueOf(
                            fromHistoryList.stream().filter(history -> history.getImage().equals("star")).count()));
                    radioCountSent.setText(String.valueOf(
                            fromHistoryList.stream().filter(history -> history.getImage().equals("radio")).count()));
                    crossCountSent.setText(String.valueOf(
                            fromHistoryList.stream().filter(history -> history.getImage().equals("cross")).count()));

                    StringBuilder sbStar = new StringBuilder();
                    StringBuilder sbRadio = new StringBuilder();
                    StringBuilder sbCross = new StringBuilder();

                    for (History history : toHistoryList) {
                        if (history.getImage().equals("star")) {
                            sbStar.append("\nSender: " + history.getFrom() + "\n" + "Time: " + history.getTime() + '\n');
                        } else if (history.getImage().equals("radio")) {
                            sbRadio.append("\nSender: " + history.getFrom() + "\n" + "Time: " + history.getTime() + '\n');
                        } else if (history.getImage().equals("cross")) {
                            sbCross.append("\nSender: " + history.getFrom() + "\n" + "Time: " + history.getTime() + '\n');
                        }
                    }

                    starTextReceived.setText(sbStar.toString());
                    radioTextReceived.setText(sbRadio.toString());
                    crossTextReceived.setText(sbCross.toString());
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

//                Log.w(TAG,"Fail to read data", error.toException());
            }
        });
    }

}
package edu.northeastern.cs5520group7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.northeastern.cs5520group7.model.History;
import edu.northeastern.cs5520group7.model.User;

public class StickerSelectionActivity extends AppCompatActivity {

    ImageButton starBtn;
    ImageButton radioBtn;
    ImageButton crossBtn;

    Button backtoHomeBtn;

    String currentUser;
    String clickedName;
    String targetToken;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_selection);

        starBtn = (ImageButton) findViewById(R.id.starBtn);
        radioBtn = (ImageButton) findViewById(R.id.radioBtn);
        crossBtn = (ImageButton) findViewById(R.id.crossBtn);


        clickedName = getIntent().getStringExtra("clickedName");
        currentUser = getIntent().getStringExtra("currentUser");
        targetToken = getIntent().getStringExtra("Token");
        Log.d("clickedName", clickedName);
        Log.d("currentUser", currentUser);
        Log.d("token", targetToken);

        userRef = FirebaseDatabase.getInstance().getReference("users");
        Date currentTime = Calendar.getInstance().getTime();
        String time = currentTime.toString();


        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatecurrentUserHistoryValue(currentUser, clickedName, time, "star");
                updateReceivedUserHistoryValue(currentUser, clickedName, time, "star");


            }
        });

        radioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatecurrentUserHistoryValue(currentUser, clickedName, time, "radio");
                updateReceivedUserHistoryValue(currentUser, clickedName, time, "radio");
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatecurrentUserHistoryValue(currentUser, clickedName, time, "cross");
                updateReceivedUserHistoryValue(currentUser, clickedName, time, "cross");

            }
        });


    }

    private void updatecurrentUserHistoryValue(String from, String to, String time, String image) {
        History newHistory = new History(from, to, time, image);

        DatabaseReference historyCurrUserRef = userRef.child(currentUser).child("histories");


        Log.d("currU", currentUser.toString());
        historyCurrUserRef.push().setValue(newHistory);
    }

    private void updateReceivedUserHistoryValue(String from, String to, String time, String image) {
        History newHistory = new History(from, to, time, image);

        DatabaseReference historyClickedUserRef = userRef.child(clickedName).child("histories");

        Log.d("recU", clickedName.toString());
        historyClickedUserRef.push().setValue(newHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(StickerSelectionActivity.this, "Sent successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
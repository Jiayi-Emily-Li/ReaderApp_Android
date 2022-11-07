package edu.northeastern.cs5520group7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

import edu.northeastern.cs5520group7.model.History;

public class StickerSelectionActivity extends AppCompatActivity {

    ImageButton starBtn;
    ImageButton radioBtn;
    ImageButton crossBtn;

    Button notifyFriendBtn;
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

        notifyFriendBtn = (Button) findViewById(R.id.NotiBtn);
        backtoHomeBtn = (Button) findViewById(R.id.backUserHome);


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

        notifyFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendMessageToDevice(targetToken);
            }
        });

        backtoHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StickerSelectionActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }
        });


    }

    private void updatecurrentUserHistoryValue(String from, String to, String time, String image) {
        History newHistory = new History(from, to, time, image);

        DatabaseReference historyCurrUserRef = userRef.child(currentUser).child("histories");


        Log.d("currU", currentUser.toString());
        historyCurrUserRef.push().setValue(newHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(StickerSelectionActivity.this, "Sent successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
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


    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
   /* private void sendMessageToDevice(String targetToken) {

        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("title", "Message Title from 'SEND MESSAGE TO CLIENT BUTTON'");
            /*
            // We can add more details into the notification if we want.
            // We happen to be ignoring them for this demo.
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            */
           // jdata.put("title", "data title from 'SEND MESSAGE TO CLIENT BUTTON'");
           // jdata.put("content", "data content from 'SEND MESSAGE TO CLIENT BUTTON'");

            /***
             * The Notification object is now populated.
             * Next, build the Payload that we send to the server.
             */

            // If sending to a single client
           /* jPayload.put("to", targetToken); // CLIENT_REGISTRATION_TOKEN);


            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/
}
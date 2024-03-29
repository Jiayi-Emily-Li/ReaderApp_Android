package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import edu.northeastern.cs5520group7.model.History;

public class StickerSelectionActivity extends AppCompatActivity {

    ImageButton starBtn;
    ImageButton radioBtn;
    ImageButton crossBtn;



    String currentUser;
    String clickedName;
    String targetToken;
    private DatabaseReference userRef;
    private static final String SERVER_KEY = "key=AAAAZOyBdhI:APA91bFs6BMDiNyffh588kESWGKO1pvanPCSBt3dQPrsMSuXFv9QKDAvehzsSTGDqrppWJ5uWzMUZg0tMQLcx0wnzCdyeWSxtl1m2bwpW76Y9TGFOvV0brjkzFX2Ji4m9nQqVAnatETE";

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
                updateCurrentUserHistoryValue(currentUser, clickedName, time, "star");
                updateReceivedUserHistoryValue(currentUser, clickedName, time, "star");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(targetToken, starBtn);
                    }
                }).start();

            }
        });

        radioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentUserHistoryValue(currentUser, clickedName, time, "radio");
                updateReceivedUserHistoryValue(currentUser, clickedName, time, "radio");
            }
        });

        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentUserHistoryValue(currentUser, clickedName, time, "cross");
                updateReceivedUserHistoryValue(currentUser, clickedName, time, "cross");

            }
        });


    }

    private void updateCurrentUserHistoryValue(String from, String to, String time, String image) {
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


            private void sendMessage(String targetToken, ImageButton selected) {
                JSONObject jPayload = new JSONObject();
                JSONObject jNotification = new JSONObject();

                try{

                    jNotification.put("title", "Stick It To 'Em");
                    jNotification.put("body", "Sticker Sent From " + currentUser);
                    jNotification.put("sound", "default");
                    jNotification.put("badge", "1");
                    jNotification.put("image", selected);
                    jNotification.put("click_action", "OPEN_ACTIVITY_1");

                    // If sending to a single client
                    jPayload.put("to", targetToken);
                    jPayload.put("priority", "high");
                    jPayload.put("notification", jNotification);

                    // HTTP and send the payload
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", SERVER_KEY);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Send FCM message content.
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(jPayload.toString().getBytes());
                    outputStream.close();

                    // Read FCM response.
                    InputStream inputStream = conn.getInputStream();
                    final String resp = convertStreamToString(inputStream);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("fcm", "run: " + resp);
                        }
                    });

                } catch (JSONException | IOException e) {
                    Log.e("fcm","sendMessageToNews threw error",e);
                }
            }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

}
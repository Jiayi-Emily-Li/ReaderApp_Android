package edu.northeastern.cs5520group7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserHomeActivity extends AppCompatActivity {

    private Button showHistoryButton;
    private String name;
    private Button sendStickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        showHistoryButton = (Button) findViewById(R.id.showHistoryButton);
        name = getIntent().getStringExtra("name");
        showHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, HistoryActivity.class);
                intent.putExtra("name", name);
                UserHomeActivity.this.startActivity(intent);
            }
        });

        sendStickerButton = (Button) findViewById(R.id.sendStickerButton);
        sendStickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, UserList.class);
                intent.putExtra("currentUser", name);
                UserHomeActivity.this.startActivity(intent);

            }
        });


    }
}
package edu.northeastern.cs5520group7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserHomeActivity extends AppCompatActivity {

    Button showHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        showHistoryButton = (Button) findViewById(R.id.showHistoryButton);
        showHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, HistoryActivity.class);
                UserHomeActivity.this.startActivity(intent);
            }
        });


    }
}
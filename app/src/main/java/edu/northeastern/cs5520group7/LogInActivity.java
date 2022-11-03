package edu.northeastern.cs5520group7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    String name;
    EditText userName;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userName = (EditText) findViewById(R.id.userName);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 name = userName.getText().toString();

                Intent intent = new Intent(LogInActivity.this, UserHomeActivity.class);
                LogInActivity.this.startActivity(intent);

            }
        });

    }
}
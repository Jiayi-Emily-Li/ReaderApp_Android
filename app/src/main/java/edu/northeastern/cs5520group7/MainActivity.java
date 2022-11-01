package edu.northeastern.cs5520group7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button stickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stickButton = (Button) findViewById(R.id.stickButton);
        stickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Stick.class);
                MainActivity.this.startActivity(intent);
            }
        });


    }
    public void startRetrofitActivity(View view){
        startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
    }
}
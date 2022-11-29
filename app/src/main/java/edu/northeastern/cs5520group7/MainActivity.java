package edu.northeastern.cs5520group7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String SERVER_KEY = "key=AAAAZOyBdhI:APA91bFs6BMDiNyffh588kESWGKO1pvanPCSBt3dQPrsMSuXFv9QKDAvehzsSTGDqrppWJ5uWzMUZg0tMQLcx0wnzCdyeWSxtl1m2bwpW76Y9TGFOvV0brjkzFX2Ji4m9nQqVAnatETE";


    private Button stickButton;
    private Button aboutButton;
    private Button finalProjButton;

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

        aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, About.class);
                MainActivity.this.startActivity(intent);
            }
        });


        finalProjButton = (Button) findViewById(R.id.finalProjectButton);
        finalProjButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Entry.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }
    public void startRetrofitActivity(View view){
        startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
    }
}
package edu.northeastern.cs5520group7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startRetrofitActivity(View view){
        startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
    }
}
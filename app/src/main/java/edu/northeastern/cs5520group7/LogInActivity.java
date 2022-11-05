package edu.northeastern.cs5520group7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import edu.northeastern.cs5520group7.model.User;

public class LogInActivity extends AppCompatActivity {

    String name;
    EditText userName;
    Button submitButton;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userName = (EditText) findViewById(R.id.userName);

        submitButton = (Button) findViewById(R.id.submitButton);
        userRef = FirebaseDatabase.getInstance().getReference("users");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userName.getText().toString();

                Intent intent = new Intent(LogInActivity.this, UserHomeActivity.class);
                intent.putExtra("name", name);
                LogInActivity.this.startActivity(intent);
                User user = new User("userId", name, "token1");
                user.addHistory("abc", name, new Date().toString(), "star");
                user.addHistory("cds", name, new Date().toString(), "circle");
                user.addHistory("kfc", name, new Date().toString(), "star");

                user.addHistory(name, "qwe", new Date().toString(), "star");
                user.addHistory(name, "wq", new Date().toString(), "star");
                user.addHistory(name, "xzczx", new Date().toString(), "star");

                userRef.child(name).setValue(user);
            }
        });

    }
}
package edu.northeastern.cs5520group7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.northeastern.cs5520group7.model.History;
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

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.hasChild(name)) {
                            User user = new User("userId", name, "token1");
                            userRef.child(name).setValue(user);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

//                Log.w(TAG,"Fail to read data", error.toException());
                    }
                });

            }
        });

    }


    public void findUser() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user = dataSnapshot.getValue(User.class);
                    if (user.getName().equals(name)) {
                        break;
                    }
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

//                Log.w(TAG,"Fail to read data", error.toException());
            }
        });
    }
}
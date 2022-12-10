package edu.northeastern.cs5520group7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button sendEmailBtn;
    EditText emailToReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();

        sendEmailBtn = findViewById(R.id.sendEmailBtn);
        emailToReset = findViewById(R.id.emailToReset);

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailToReset.getText().toString();

                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(ResetPassword.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPassword.this, "Email sent!\nPlease check your email inbox.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassword.this, Entry.class));
                            } else {
                                String errorMsg = task.getException().getMessage();
                                Toast.makeText(ResetPassword.this, "Error: "+ errorMsg, Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            }
        });

    }
}
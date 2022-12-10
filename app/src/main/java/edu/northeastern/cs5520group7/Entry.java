package edu.northeastern.cs5520group7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Entry extends AppCompatActivity {
    private Button registerButton;
    private Button signInButton;
    private Button forgotPasswordBtn;
    private EditText userNameInput;
    private EditText passwordInput;
    String userNamePattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    public static String userNameText;
    public static String passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Entry.this, Register.class);
                startActivity(intent);
            }
        });

        userNameInput = findViewById(R.id.userNameInput);
        passwordInput = findViewById(R.id.passwordInput);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignIn();

            }
        });

        forgotPasswordBtn = (Button) findViewById(R.id.forgotPasswordBtn);
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Entry.this, ResetPassword.class);
                startActivity(intent);

            }
        });

    }

    private void performSignIn() {
        String userName = userNameInput.getText().toString();
        String password = passwordInput.getText().toString();


        if (!userName.matches(userNamePattern)) {
            userNameInput.setError("Please enter a valid email address");
        } else if (password.isEmpty() || password.length() < 6) {
            passwordInput.setError("Please enter a valid password(length >= 6)");
        } else {
            progressDialog.setMessage("please waiting...");
            progressDialog.setTitle("sign in");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextPage();

                        Toast.makeText(Entry.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Entry.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

    }

    private void sendUserToNextPage() {
        Intent intent = new Intent(Entry.this, HomePage.class);
        intent.putExtra("userName", userNameInput.getText().toString());
        intent.putExtra("currentUser",mUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
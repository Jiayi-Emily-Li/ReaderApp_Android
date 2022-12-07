package edu.northeastern.cs5520group7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.northeastern.cs5520group7.databinding.ActivityHomePageBinding;

public class HomePage extends AppCompatActivity {

    private FirebaseUser user;
    ActivityHomePageBinding binding;
    public static String userNameText;
    public static FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        userNameText = intent.getStringExtra("userName");
        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = intent.getParcelableExtra("currentUser");

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.discover:
                    replaceFragment(new DiscoverFragment());
                    break;
                case R.id.myList:
                    replaceFragment(new ListFragment());
                    break;
                case R.id.post:
                    replaceFragment(new PostFragment());
                    break;

            }




            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }



}
package edu.northeastern.cs5520group7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {

    private static final String TAG = UserList.class.getSimpleName();

    private ListView listView;
    //set up arrayList to store the data
    private ArrayList<String> users_Name = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //connected to firebase
        database = FirebaseDatabase.getInstance();
        mRef=database.getReference("Users");

        //connected element of arrayList to the listView
        listView = (ListView) findViewById(R.id.UserListView);
        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users_Name);
        listView.setAdapter(arrAdapter);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //change to User class? Added later
                String name = snapshot.getValue(String.class);
                users_Name.add(name);
                arrAdapter.notifyDataSetChanged();
                Log.e(TAG, "onChildAdded: dataSnapshot = " + snapshot.getValue().toString());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.v(TAG, "onChildChanged: " + snapshot.getValue().toString());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
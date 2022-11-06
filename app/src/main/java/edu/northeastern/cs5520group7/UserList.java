package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs5520group7.Adapter.SelectListener;
import edu.northeastern.cs5520group7.Adapter.UserAdapter;
import edu.northeastern.cs5520group7.model.History;
import edu.northeastern.cs5520group7.model.User;

public class UserList extends AppCompatActivity implements SelectListener {


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()/*.child("Users")*/;



        //connected element of arrayList to the RecycleView
        recyclerView = (RecyclerView) findViewById(R.id.UserRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<User> userList = new ArrayList<>();
        ArrayList<History> histories = new ArrayList<>();



        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("message for users", snapshot.getValue().toString());
                userList.clear();
                for(DataSnapshot Snapshot : snapshot.getChildren()){
                    for(DataSnapshot usersSnapshot: Snapshot.getChildren()) {
                        String name = usersSnapshot.child("name").getValue().toString();
                        String userId = usersSnapshot.child("userId").getValue().toString();
                        String token = usersSnapshot.child("token").getValue().toString();

                        User user = new User(userId, name,token);
                        userList.add(user);

                }}
                userAdapter = new UserAdapter(UserList.this, userList, UserList.this);
                recyclerView.setAdapter(userAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    @Override
    public void itemClicked(User user) {
        Toast.makeText(this,"clicked: " + user.getName(),Toast.LENGTH_SHORT).show();
        new Thread(new Runnable(){

            @Override
            public void run() {
                sendMessageToDevice(user.getToken());
            }
        }).start();
    }

    private void sendMessageToDevice(String targetToken){
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();

    }
}
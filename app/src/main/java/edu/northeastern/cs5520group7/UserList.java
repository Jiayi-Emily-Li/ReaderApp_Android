package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs5520group7.Adapter.UserAdapter;
import edu.northeastern.cs5520group7.model.User;

public class UserList extends AppCompatActivity {


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child("user1");


        //connected element of arrayList to the RecycleView
        recyclerView = (RecyclerView) findViewById(R.id.UserRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<User> userList = new ArrayList<>();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userList.clear();
                //for(DataSnapshot Snapshot : snapshot.getChildren()){
                    //User user = Snapshot.getValue(User.class);
                    //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    //assert user != null;

                    //userList.add(user);
                    User value = snapshot.getValue(User.class);
                    Log.d("message FOR USER", value.toString());

                //}
                userAdapter = new UserAdapter(UserList.this, userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}
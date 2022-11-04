package edu.northeastern.cs5520group7.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.cs5520group7.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context myContext;
    private List<User> userList;




    public UserAdapter(Context myContext, List<User> userList){
        this.userList = userList;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.username.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}

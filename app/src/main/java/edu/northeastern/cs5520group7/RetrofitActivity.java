package edu.northeastern.cs5520group7;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.northeastern.cs5520group7.model.Comment;
import edu.northeastern.cs5520group7.model.IPlaceholder;
import edu.northeastern.cs5520group7.model.PostModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitActivity";

    private Retrofit retrofit;
    private Button retrofitBtn;
    private IPlaceholder api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        retrofitBtn = findViewById(R.id.button4);
        retrofitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPosts();
                //getComments();
                //postWithQ();
                //createANewPost();
                //createANewPostWithHeaders();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        api = retrofit.create(IPlaceholder.class);


    }
    private void getPosts(){
        // to execute the call
        Call<List<PostModel>> call = api.getPostModels();
        //call.execute() runs on the current thread, which is main at the momement. This will crash
        // use Retrofit's method enque. This will automaically push the network call to background thread
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                //This gets called when atleast the call reaches a server and there was a response BUT 404 or any legitimate error code from the server, also calls this
                // check response code is between 200-300 and API was found

                if(!response.isSuccessful()){
                    Log.d(TAG, "Call failed!" + response.code());
                    return;
                }

                Log.d(TAG, "Call Successed!");
                List<PostModel> postModels = response.body();
                for(PostModel post : postModels){
                    StringBuffer  str = new StringBuffer();
                    str.append("Code:: ")
                            .append(response.code())
                            .append("ID : ")
                            .append(post.getId())
                            .append("\n")
                            .append("UserID :")
                            .append(post.getUserId())
                            .append("\n")
                            .append("Title: ")
                            .append(post.getTitle())
                            .append("\n")
                            .append("Description: ")
                            .append(post.getText())
                            .append("\n");


                    Log.d(TAG, str.toString());


                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                // this gets called when url is wrong and therefore calls can't be made OR processing the request goes wrong.
                Log.d(TAG, "Call failed!" + t.getMessage());
            }
        });
    }
    private void getComments() {
        // to execute the call
        //api : https://jsonplaceholder.typicode.com/posts/5/comments
        Call<List<Comment>> call = api.getComments(5);
        //call.execute() runs on the current thread, which is main at the momement. This will crash
        // use Retrofit's method enque. This will automaically push the network call to background thread
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                //This gets called when atleast the call reaches a server and there was a response BUT 404 or any legitimate error code from the server, also calls this
                // check response code is between 200-300 and API was found

                if(!response.isSuccessful()){
                    Log.d(TAG, "Call failed!" + response.code());
                    return;
                }

                Log.d(TAG, "Call Successed!");
                List<Comment> comments = response.body();
                for(Comment comment : comments){
                    StringBuffer  str = new StringBuffer();
                    str.append("ID : ")
                            .append(comment.getId())
                            .append("\n")
                            .append("PostID :")
                            .append(comment.getPostId())
                            .append("\n")
                            .append("Name: ")
                            .append(comment.getName())
                            .append("\n")
                            .append("Text: ")
                            .append(comment.getText())
                            .append("\n");


                    Log.d(TAG, str.toString());


                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // this gets called when url is wrong and therefore calls can't be made OR processing the request goes wrong.
                Log.d(TAG, "Call failed!" + t.getMessage());
            }
        });
    }
}

package edu.northeastern.cs5520group7;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;


public class MeFragment extends Fragment {

    private Firebase ref;
    private EditText feedback;
    Context thisContext;
    Button sendFeedbackBtn;
    private FirebaseUser user;
    private TextView hi_text;
    public static String userNameText;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisContext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        feedback = (EditText) view.findViewById(R.id.feedbackText);
        Firebase.setAndroidContext(MeFragment.this.thisContext);
        userNameText = HomePage.userNameText;

        hi_text = view.findViewById(R.id.hello);
        hi_text.setText("Hi, " + userNameText + "\nYour feedback is valuable to us!" + "\nPlease leave it below: ");

        ref = new Firebase("https://stick-it-to-em-b92ab-default-rtdb.firebaseio.com/");
        sendFeedbackBtn = (Button) view.findViewById(R.id.feedbackButton);
        sendFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackSent(v);
            }
        });

        return view;

    }

    public void feedbackSent(View view) {
        user = HomePage.currentUser;

        String feedbackInput = feedback.getText().toString();
        Firebase refFeedback = ref.child("Feedback from " + user.getUid().toString() + ": ");
        refFeedback.setValue(feedbackInput);
        Toast.makeText(thisContext, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();

    }


}
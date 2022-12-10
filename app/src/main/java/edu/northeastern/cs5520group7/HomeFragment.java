package edu.northeastern.cs5520group7;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {


    public static String userNameText;
    private TextView hi_text;
    private FirebaseUser user;
    private ImageButton resetBtn;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userNameText = HomePage.userNameText;
        user = HomePage.currentUser;
//        hi_text = view.findViewById(R.id.hi_text);
//        hi_text.setText("Hi, " + userNameText);
        resetBtn = (ImageButton) view.findViewById(R.id.currentReaderButton);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
        return view;
    }



    public void showPopup(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.pop_window, null);
        PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        int location[] = new int[2];
        anchorView.getLocationOnScreen(location);
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

        TextView cr = popupView.findViewById(R.id.currentReader);
        userNameText = HomePage.userNameText;
        cr.setText("Current reader: \n" + userNameText);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
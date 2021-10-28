package com.example.moveonotes.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.moveonotes.Interface.CallBackFragment;
import com.example.moveonotes.R;
import com.example.moveonotes.Utils.GlobalApplicationContext;
import com.example.moveonotes.Utils.SharedPrefHelper;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class WelcomeActivity extends AppCompatActivity implements CallBackFragment {

    //Variables
    private TextView userNameText;

    //LifeCycleMethods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        InitializeVariables();
        InitializeDynamicVariables();
        showScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //Init Methods
    private void InitializeDynamicVariables() {
        userNameText.setText(SharedPrefHelper.getInstance(GlobalApplicationContext.getContext()).getUser().getFirstName()+" "+ SharedPrefHelper.getInstance(GlobalApplicationContext.getContext().getApplicationContext()).getUser().getLastName());
    }

    private void InitializeVariables() {
        userNameText = findViewById(R.id.welcome_name_text);
    }

    //Activity Methods
    //Activity Methods
    private void showScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    showActivity(LoginActivity.class);
                } else {
                        showActivity(MainActivity.class);
                }

            }
        }, 3000);
    }

    @Override
    public void showActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
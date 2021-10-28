package com.example.moveonotes.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.moveonotes.Interface.CallBackFragment;
import com.example.moveonotes.R;
import com.example.moveonotes.Repo.Repository;
import com.example.moveonotes.Utils.GlobalApplicationContext;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity implements CallBackFragment {

    //Variables
    private TextView creditText;
    private TextView splashText;

    //LifeCycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        InitializeVariables();
        //TODO: DELETE SIGN OUT
        //FirebaseAuth.getInstance().signOut();
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
    private void InitializeVariables() {
        creditText = findViewById(R.id.splash_text);
        splashText = findViewById(R.id.splash_desc_text);
    }

    private void InitializeDynamicVariables() {
        creditText.setText(R.string.Splash_credit);
        splashText.setText(R.string.splash_description);
    }


    //Activity Methods
    private void showScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    showActivity(LoginActivity.class);
                } else {
                    showActivity(WelcomeActivity.class);
                }

            }
        }, 3000);
    }


    //Interface Methods
    @Override
    public void showActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


}
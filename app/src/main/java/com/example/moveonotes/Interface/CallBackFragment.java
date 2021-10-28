package com.example.moveonotes.Interface;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public interface CallBackFragment {
   // void showFragment(int fragmentId);
   // void showFragment(int fragmentId, Bundle bundle);
    void showActivity(Class<? extends AppCompatActivity> activity);
}

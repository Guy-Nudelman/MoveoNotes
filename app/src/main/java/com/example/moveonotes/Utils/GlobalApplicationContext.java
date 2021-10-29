package com.example.moveonotes.Utils;

import android.app.Application;
import android.content.Context;

public class GlobalApplicationContext extends Application {

    //Variables
    public static GlobalApplicationContext instance;

    //Global Context Methods
    public static GlobalApplicationContext getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}

package com.example.paul.livecoding;


import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application {
    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
    }
}
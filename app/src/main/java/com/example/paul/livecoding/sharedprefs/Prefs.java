package com.example.paul.livecoding.sharedprefs;

import android.app.Application;
import android.content.SharedPreferences;

public class Prefs extends Application {
    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
    }
}
package com.example.victor.todo;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by victor on 6/30/16.
 */

public class ToDoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}

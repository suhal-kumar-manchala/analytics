package com.example.analytics;

import android.app.Application;

import com.linkedin.android.shaky.EmailShakeDelegate;
import com.linkedin.android.shaky.Shaky;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Shaky.with(this, new EmailShakeDelegate("hello@world.com"));
    }
}
package com.example.analytics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class CrashActivity extends AppCompatActivity {

    Button crashButton, signOut;
    FirebaseCrashlytics crashlytics; // instance of Firebase Crashlytics.
    FirebaseAnalytics analytics; // instance of Firebase analytics.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        //initializing the instances.
        crashlytics = FirebaseCrashlytics.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        // initializing views
        signOut = findViewById(R.id.sign_out);
        crashButton = findViewById(R.id.crash_button);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        crashButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                // logic to send the info related to the crash.
                crashlytics.setCustomKey("Device Model", Build.DEVICE);
                crashlytics.setCustomKey("Android Version", Build.VERSION.RELEASE_OR_CODENAME);
                crashlytics.setCustomKey("App Version Code", BuildConfig.VERSION_CODE);
                crashlytics.setCustomKey("App Version Name", BuildConfig.VERSION_NAME);

                //crash button click event.
                Bundle params = new Bundle();
                params.putString("crashing", "Application Crashed");
                analytics.logEvent("crashed_event", params);
                throw new RuntimeException("This is an Intention to crash the app");
            }
        });
    }
}

package com.example.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Random;

public class RandomActivity extends AppCompatActivity {

    TextView userName, numberText;
    Button signOut, update, next;
    View frameLayout;
    FirebaseAuth mAuth; // Firebase Authentication Instance.
    FirebaseUser mUser; // Current User.
    FirebaseAnalytics analytics;

    HashMap<Integer, String> colorCodes; // map of color codes mapped with integers.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        analytics = FirebaseAnalytics.getInstance(this); // getting Firebase Analytics Instance.
        mAuth = FirebaseAuth.getInstance();

        //Initializing the hashmap.
        colorCodes = new HashMap<>();
        colorCodes.put(1, "#FF0000");  // Red
        colorCodes.put(2, "#00FF00");  // Green
        colorCodes.put(3, "#0000FF");  // Blue
        colorCodes.put(4, "#FFFF00");  // Yellow
        colorCodes.put(5, "#FF00FF");  // Magenta
        colorCodes.put(6, "#FFA500");  // Orange
        colorCodes.put(7, "#CD5C5C");  // Indian Red
        colorCodes.put(8, "#F08080");  // Light Coral
        colorCodes.put(9, "#FA8072");  // salmon
        colorCodes.put(10,"#00FFFF");  // Aqua

        // Initializing the views
        userName = findViewById(R.id.user_name);
        signOut = findViewById(R.id.sign_out);
        numberText = findViewById(R.id.numberText);
        update = findViewById(R.id.update);
        next = findViewById(R.id.crash);
        frameLayout = findViewById(R.id.rectangle);

        // logic to display the current user name.
        mUser = mAuth.getCurrentUser();
        userName.setText(mUser.getEmail());

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logic to set random color and number.
                Random random = new Random();
                int num = random.nextInt(10) + 1;
                numberText.setText(String.valueOf(num));
                frameLayout.setBackgroundColor(Color.parseColor(colorCodes.get(num)));
                // update event log.
                Bundle params = new Bundle();
                params.putString("color_code", colorCodes.get(num));
                analytics.logEvent("update_click", params);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // next button event log.
                Bundle params = new Bundle();
                params.putString("action_took", "TO Crash Screen");
                analytics.logEvent("crash_click", params);
                Intent intent = new Intent(getApplicationContext(), CrashActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

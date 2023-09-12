package com.example.analytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextInputEditText email, password;
    Button loginButton;

    FirebaseAuth mAuth;  //Firebase Authentication
    FirebaseAnalytics analytics; // Firebase Analytics

    @Override
    public void onStart() {
        super.onStart();
        //logic to check the user session, whether the user has logged out previously.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), RandomActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Firebase Instances
        analytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        // Initializing views.
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText, passwordText;
                emailText = String.valueOf(email.getText());
                passwordText = String.valueOf(password.getText());

                if (TextUtils.isEmpty(emailText)) {
                    Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordText)) {
                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }


                //Sign in or login Authentication Using Firebase.
                mAuth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                                    analytics.setUserId(userId);
                                    Bundle params = new Bundle();
                                    params.putString("user_email", userId);
                                    analytics.logEvent("login_event", params);
                                    Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                                    //Navigating to Screen 2.
                                    launchRandomActivity();
                                } else {
                                    // creating a user if the user doesn't exist using Firebase Authentication.
                                    mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                                                        analytics.setUserId(userId);
                                                        Bundle params = new Bundle();
                                                        params.putString("user_email", userId);
                                                        analytics.logEvent("signup_event", params);
                                                        Toast.makeText(MainActivity.this, "Registered and Logging In", Toast.LENGTH_SHORT).show();

                                                        // Navigating to Screen 2
                                                        launchRandomActivity();
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });
    }

    private void launchRandomActivity() {
        Intent intent = new Intent(getApplicationContext(), RandomActivity.class);
        startActivity(intent);
        finish();
    }
}

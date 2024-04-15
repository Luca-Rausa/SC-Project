package com.example.myapplication4;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        // Initialize buttons
        signInButton = findViewById(R.id.signUp);
        signUpButton = findViewById(R.id.signUpButton);

        // Set click listeners
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign in activity
                startActivity(new Intent(Welcome.this, SignIn.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign up activity
                startActivity(new Intent(Welcome.this, SignUp.class));
            }
        });
    }
}

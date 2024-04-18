package com.example.myapplication4;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        editTextUsername = findViewById(R.id.enterEmail);
        editTextPassword = findViewById(R.id.enterPass);
        signInButton = findViewById(R.id.signin);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enabling the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if username and password match the hardcoded values
                if (username.equals("test@gmail.com") && password.equals("test")) {
                    // Set the global isLoggedIn variable to true
                    MainActivity.isLoggedIn = true;
                    startActivity(new Intent(SignIn.this, Home.class));
                    finish();
                } else {
                    // Show error message if credentials are incorrect
                    Toast.makeText(SignIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Handle toolbar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            startActivity(new Intent(SignIn.this, Welcome.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


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

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button signInButton;
    private Button signUpButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        editTextUsername = findViewById(R.id.enterEmail);
        editTextPassword = findViewById(R.id.enterPass);
        signInButton = findViewById(R.id.signin);
        signUpButton = findViewById(R.id.button);
        dbHelper = new DatabaseHelper(this);

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

                // Query the database
                User user = dbHelper.getUser(username, password);
                MainActivity.user = user;

                if (user == null) {
                    Toast.makeText(SignIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

                // Check if user is student or staff
                if (user != null) {
                    MainActivity.isLoggedIn = true;

                    MainActivity.isStaff = Objects.equals(user.getRole(), "Staff");
                    Toast.makeText(SignIn.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignIn.this, Home.class));
                    finish();
                } else {
                    Toast.makeText(SignIn.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
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


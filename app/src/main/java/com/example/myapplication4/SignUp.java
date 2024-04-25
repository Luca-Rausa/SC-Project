package com.example.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SignUp extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Spinner spinner;
    private Button signUpButton;
    private Button signInButton;
    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enabling the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextFirstName = findViewById(R.id.editTextTextFirst);
        editTextLastName = findViewById(R.id.editTextTextLast);
        signInButton = findViewById(R.id.signInButton);
        spinner = findViewById(R.id.spinner);
        signUpButton = findViewById(R.id.signUp);

        // Initialize spinner with options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Listen for spinner item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String firstname = editTextFirstName.getText().toString().trim();
                String lastname = editTextLastName.getText().toString().trim();

                // Validate inputs
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO: Save user data

                // Navigate to home page
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to home page
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            }
        });
    }

    // Handle toolbar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            startActivity(new Intent(SignUp.this, Welcome.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

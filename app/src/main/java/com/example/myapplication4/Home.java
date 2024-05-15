package com.example.myapplication4;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    private ImageButton requiredForms, eventHub, feedback, chatbot, banner;
    private Button signOutButton;
    private TextView name;

    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        name = findViewById(R.id.textView2);

        if (MainActivity.user != null) {
            String firstName = MainActivity.user.getFirstname() + ","; // Get the user's first name

            // Update the TextView text to display the user's first name
            name.setText(firstName);
        }

        // Initialize buttons
        requiredForms = findViewById(R.id.requiredFormsButton);
        eventHub = findViewById(R.id.eventhubButton);
        feedback = findViewById(R.id.feedbackButton);
        chatbot = findViewById(R.id.chatbotButton);
        banner = findViewById(R.id.bannerButton);
        signOutButton = findViewById(R.id.signout);

        // Set click listeners
        requiredForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign in activity
                startActivity(new Intent(Home.this, Forms.class));
            }
        });

        eventHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign up activity
                startActivity(new Intent(Home.this, EventHub.class));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign in activity
                startActivity(new Intent(Home.this, Feedback.class));
            }
        });

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign up activity
                startActivity(new Intent(Home.this, Chatbot.class));
            }
        });

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SC website
                String url = "https://stegercenter.vt.edu";

                // Create an Intent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

                // Start the web browser activity
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign up activity
                MainActivity.isLoggedIn = false;
                startActivity(new Intent(Home.this, SignIn.class));
                finish();
            }
        });

        // Navigation Bar
        bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
//                    startActivity(new Intent(getApplicationContext(), Home.class));
//                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (itemId == R.id.forms) {
                    startActivity(new Intent(getApplicationContext(), Forms.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (itemId == R.id.events) {
                    startActivity(new Intent(getApplicationContext(), EventHub.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (itemId == R.id.feedback) {
                    startActivity(new Intent(getApplicationContext(), Feedback.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (itemId == R.id.chatbot) {
                    startActivity(new Intent(getApplicationContext(), Chatbot.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}

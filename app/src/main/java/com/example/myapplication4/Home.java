package com.example.myapplication4;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.net.Uri;
import android.Manifest;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    private ImageButton requiredForms, eventHub, feedback, chatbot, banner;
    private Button signOutButton;
    private TextView name;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        name = findViewById(R.id.textView2);

        if (MainActivity.user != null) {
            String firstName = MainActivity.user.getFirstname() + ",";

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
                startActivity(new Intent(Home.this, ViewFeedback.class));
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

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permits granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please, grant location permits", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

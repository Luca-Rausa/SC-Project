package com.example.myapplication4;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    private Button mealPrepButton;
    private Button travelItineraryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Initialize buttons
        mealPrepButton = findViewById(R.id.button);
        travelItineraryButton = findViewById(R.id.button6);

        // Set click listeners
        mealPrepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign in activity
                startActivity(new Intent(Home.this, MealPlanning.class));
            }
        });

        travelItineraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign up activity
                startActivity(new Intent(Home.this, TravelItinerary.class));
            }
        });
    }
}

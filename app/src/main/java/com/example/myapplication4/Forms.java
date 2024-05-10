package com.example.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Forms extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forms);

        // View Results Buttons
        Button mealResultsButton = findViewById(R.id.mealResults);
        Button travelResultsButton = findViewById(R.id.travelResults);

        if (MainActivity.isStaff) {
            mealResultsButton.setVisibility(View.VISIBLE);
            travelResultsButton.setVisibility(View.VISIBLE);
        } else {
            mealResultsButton.setVisibility(View.GONE);
            travelResultsButton.setVisibility(View.GONE);
        }

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enabling the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Arrow Buttons
        ImageButton mealButton = findViewById(R.id.mealButton);
        ImageButton travelButton = findViewById(R.id.travelButton);

        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MealPlanning
                startActivity(new Intent(Forms.this, MealPlanning.class));
            }
        });

        travelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Forms.this, TravelItinerary.class));
            }
        });

        mealResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MealPlanning
                startActivity(new Intent(Forms.this, MealResults.class));
            }
        });

        travelResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Forms.this, TravelResults.class));
            }
        });
    }

    // Handle toolbar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            startActivity(new Intent(Forms.this, Home.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

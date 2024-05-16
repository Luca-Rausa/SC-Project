package com.example.myapplication4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText feed;
    private DatabaseHelper dbHelper;
    private Button next;
    private ListView FeedBackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        ratingBar = findViewById(R.id.ratingBar);
        feed = findViewById(R.id.feedarea);
        next = findViewById(R.id.nextbtn);
        dbHelper = new DatabaseHelper(this);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enabling the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs from EditText fields
                float ratingNum = ratingBar.getRating();
                String rating = String.valueOf(ratingNum);
                String user_feedback = feed.getText().toString();
                dbHelper.AddFeedBack(new FeedBackData(user_feedback,rating));

                // Navigate to the home page
                startActivity(new Intent(Feedback.this, ViewFeedback.class));
                finish();
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }

    // Handle toolbar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            startActivity(new Intent(Feedback.this, Forms.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

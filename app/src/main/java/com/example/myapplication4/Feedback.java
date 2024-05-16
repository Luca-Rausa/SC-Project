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

                // Save data to SQLite database
                saveData(rating, user_feedback);

                // Navigate to the home page
                startActivity(new Intent(Feedback.this, ViewFeedback.class));
                finish();
            }
        });
    }

    private void saveData(String rating, String userFeedback) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_RATING, rating);
        values.put(DatabaseHelper.COL_FEEDBACK, userFeedback);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DatabaseHelper.TABLE_FEEDBACK, null, values);

        // You can handle the result of the insertion if needed
        if (newRowId != -1) {
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
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

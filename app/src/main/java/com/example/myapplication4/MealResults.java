package com.example.myapplication4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MealResults extends AppCompatActivity  {
    private DatabaseHelper dbHelper;
    private TextView firstResultTextView, lastResultTextView, date1TextView, date2TextView, mealsAttendingTextView,
            groupTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_results);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enabling the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        firstResultTextView = findViewById(R.id.firstResult);
        lastResultTextView = findViewById(R.id.lastResult);
        date1TextView = findViewById(R.id.date1);
        date2TextView = findViewById(R.id.date2);
        mealsAttendingTextView = findViewById(R.id.attendance);
        groupTextView = findViewById(R.id.group);

        Button submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser();
            }
        });
    }

    // Handle toolbar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            startActivity(new Intent(MealResults.this, Forms.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchUser() {
        SearchView searchView = findViewById(R.id.search);
        String fullName = searchView.getQuery().toString();

        // Split the full name into first name and last name
        String[] names = fullName.split(" ");
        if (names.length != 2) {
            Toast.makeText(this, "Please enter first name followed by last name", Toast.LENGTH_SHORT).show();
            return;
        }

        String firstName = names[0];
        String lastName = names[1];

        // Query the database for the user
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(DatabaseHelper.TABLE_MEALS);
        Cursor cursor = database.rawQuery("SELECT * FROM meals WHERE firstName = ? AND lastName = ?", new String[]{firstName, lastName});

        if (cursor.moveToFirst()) {
            // User found, retrieve travel details
            String date1 = cursor.getString(cursor.getColumnIndex("startDate"));
            String date2 = cursor.getString(cursor.getColumnIndex("endDate"));
            String program = cursor.getString(cursor.getColumnIndex("programOfStudy"));
            String meals = cursor.getString(cursor.getColumnIndex("mealTimings"));

            // Update TextViews with retrieved information
            firstResultTextView.setText(firstName);
            lastResultTextView.setText(lastName);
            mealsAttendingTextView.setText(meals);
            groupTextView.setText(program);
            date1TextView.setText(date1);
            date2TextView.setText(date2);
        } else {
            // User not found
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        database.close();
    }
}

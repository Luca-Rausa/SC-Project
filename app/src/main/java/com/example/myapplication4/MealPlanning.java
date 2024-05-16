package com.example.myapplication4;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.res.ColorStateList;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class MealPlanning extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private Button csButton, pgsButton, archButton, otherButton;
    private String programStudy;
    private DatabaseHelper dbHelper;
    private EditText startDateEditText, endDateEditText;
    private RadioButton breakfast, lunch, dinner;
    private Button submitButton;
    private EditText activeDateField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_planning);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enabling the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        emailEditText = findViewById(R.id.editTextEmailAddress);

        // Initialize Buttons
        csButton = findViewById(R.id.cs);
        pgsButton = findViewById(R.id.pgsButton);
        archButton = findViewById(R.id.archButton);
        otherButton = findViewById(R.id.otherButton);

        // Initialize EditText fields for dates
        startDateEditText = findViewById(R.id.editStartDate);
        endDateEditText = findViewById(R.id.editEndDate);

        breakfast = findViewById(R.id.radioButton);
        lunch = findViewById(R.id.radioButton2);
        dinner = findViewById(R.id.radioButton3);

        // Initialize buttons
        submitButton = findViewById(R.id.submitButton);

        // Set onClickListener for the date fields
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateEditText);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateEditText);
            }
        });
        // Configure RadioButtons to change background color on selection
        configureButtons();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs from EditText fields
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String startDate = startDateEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();
                String mealTimings = "";
                if (breakfast.isChecked()) {
                    mealTimings += "Breakfast ";
                }
                if (lunch.isChecked()) {
                    mealTimings += "Lunch ";
                }
                if (dinner.isChecked()) {
                    mealTimings += "Dinner ";
                }

                // Trim any extra space at the end
                mealTimings = mealTimings.trim();

                // Save data to SQLite database
                saveData(firstName, lastName, email, startDate, endDate, programStudy, mealTimings);

                // Navigate to the home page
                startActivity(new Intent(MealPlanning.this, Forms.class));
                finish();
            }
        });
    }
    private void showDatePickerDialog(EditText editText) {
        activeDateField = editText; // Set the active EditText
        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                this,
//                this,
//                Calendar.getInstance().get(Calendar.YEAR),
//                Calendar.getInstance().get(Calendar.MONTH),
//                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> onDateSet(view, year, month, dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Format the picked date and set it on the active EditText field
        String date = String.format(Locale.getDefault(), "%d/%02d/%02d", year, month + 1, dayOfMonth);
        activeDateField.setText(date);
    }
    private void saveData(String firstName, String lastName, String email, String startDate,
                          String endDate, String programStudy, String mealTimings) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_FIRSTNAME, firstName);
        values.put(DatabaseHelper.COL_LASTNAME, lastName);
        values.put(DatabaseHelper.COL_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_START_DATE, startDate);
        values.put(DatabaseHelper.COLUMN_END_DATE, endDate);
        values.put(DatabaseHelper.COLUMN_PROGRAM_OF_STUDY, programStudy);
        values.put(DatabaseHelper.COLUMN_MEAL_TIMINGS, mealTimings);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DatabaseHelper.TABLE_MEALS, null, values);
        System.out.println(DatabaseHelper.TABLE_MEALS);
        // You can handle the result of the insertion if needed
        if (newRowId != -1) {
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private void configureButtons() {
        csButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MealPlanning.this, R.color.dark_blue)));
                csButton.setTextColor(ContextCompat.getColor(MealPlanning.this, R.color.white));
                resetOtherButtons(csButton);
                programStudy = "CS";
            }
        });

        pgsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgsButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MealPlanning.this, R.color.dark_blue)));
                pgsButton.setTextColor(ContextCompat.getColor(MealPlanning.this, R.color.white));
                resetOtherButtons(pgsButton);
                programStudy = "Postgraduate Studies";
            }
        });

        archButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MealPlanning.this, R.color.dark_blue)));
                archButton.setTextColor(ContextCompat.getColor(MealPlanning.this, R.color.white));
                resetOtherButtons(archButton);
                programStudy = "Architecture";
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MealPlanning.this, R.color.dark_blue)));
                otherButton.setTextColor(ContextCompat.getColor(MealPlanning.this, R.color.white));
                resetOtherButtons(otherButton);
                programStudy = "Other";
            }
        });
    }

    private void resetOtherButtons(Button activeButton) {
        Button[] buttons = {csButton, pgsButton, archButton, otherButton};
        for (Button btn : buttons) {
            if (btn != activeButton) {
                btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MealPlanning.this, android.R.color.transparent)));
                btn.setTextColor(ContextCompat.getColor(MealPlanning.this, R.color.black));
            }
        }
    }

    // Handle toolbar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            startActivity(new Intent(MealPlanning.this, Forms.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
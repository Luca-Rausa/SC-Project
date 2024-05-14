package com.example.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;

public class MealPlanning extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private Button csButton, pgsButton, archButton, otherButton;
    private String programStudy;
    private EditText startDateEditText, endDateEditText;
    private Spinner breakfastSpinner, lunchSpinner, dinnerSpinner;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_planning);

        // Initialize EditText fields
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);

        // Initialize Buttons
        csButton = findViewById(R.id.cs);
        pgsButton = findViewById(R.id.pgsButton);
        archButton = findViewById(R.id.archButton);
        otherButton = findViewById(R.id.otherButton);

        // Initialize EditText fields for dates
        startDateEditText = findViewById(R.id.editStartDate);
        endDateEditText = findViewById(R.id.editEndDate);

        // Initialize Spinners
        breakfastSpinner = findViewById(R.id.breakfastSpinner);
        lunchSpinner = findViewById(R.id.lunchSpinner);
        dinnerSpinner = findViewById(R.id.dinnerSpinner);

        // Initialize buttons
        submitButton = findViewById(R.id.submitButton);

        // Add TextWatchers to date EditTexts
        TextWatcher dateTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateDateSpinners();
            }
        };

        startDateEditText.addTextChangedListener(dateTextWatcher);
        endDateEditText.addTextChangedListener(dateTextWatcher);

        // Configure RadioButtons to change background color on selection
        configureButtons();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start sign in activity
                startActivity(new Intent(MealPlanning.this, Forms.class));
            }
        });
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

    private void updateDateSpinners() {
        String startDateString = startDateEditText.getText().toString();
        String endDateString = endDateEditText.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(startDateString);
            endDate = dateFormat.parse(endDateString);
            if (startDate != null && endDate != null && !startDate.after(endDate)) {
                List<String> daysOfWeek = generateDates(startDate, endDate);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysOfWeek);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                breakfastSpinner.setAdapter(adapter);
                lunchSpinner.setAdapter(adapter);
                dinnerSpinner.setAdapter(adapter);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Optionally handle incorrect date format or show some error to the user
        }
    }

    // Generate list of dates between start and end dates
    private List<String> generateDates(Date startDate, Date endDate) {
        List<String> daysOfWeek = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (!calendar.getTime().after(endDate)) {
            Date currentDate = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            String dateString = dateFormat.format(currentDate);
            daysOfWeek.add(dateString);
            calendar.add(Calendar.DATE, 1);
        }
        return daysOfWeek;
    }

    // TODO: Connect to database
}

package com.example.myapplication4;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealPlanning extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private Button csButton, pgsButton, archButton, otherButton;
    private String programStudy;
    private EditText startDateEditText, endDateEditText;
    private Spinner breakfastSpinner, lunchSpinner, dinnerSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_planning);

        // Initialize EditText fields
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);

        // Initialize RadioButtons
        csButton = findViewById(R.id.cs);
        pgsButton = findViewById(R.id.pgsButton);
        archButton = findViewById(R.id.archButton);
        otherButton = findViewById(R.id.otherButton);

        // Initialize EditText fields
        startDateEditText = findViewById(R.id.editTextDate2);
        endDateEditText = findViewById(R.id.editTextDate3);

        // Initialize Spinners
        breakfastSpinner = findViewById(R.id.breakfastSpinner);
        lunchSpinner = findViewById(R.id.lunchSpinner);
        dinnerSpinner = findViewById(R.id.dinnerSpinner);

        // Configure RadioButtons to change background color on selection
        configureButtons();

        // Handle submit button click
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Retrieve user input
                String startDateString = startDateEditText.getText().toString();
                String endDateString = endDateEditText.getText().toString();

                // Convert start and end dates to Date objects
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = dateFormat.parse(startDateString);
                    endDate = dateFormat.parse(endDateString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Generate list of dates between start and end dates
                List<String> daysOfWeek = generateDates(startDate, endDate);

                // Populate Spinners with options
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MealPlanning.this, android.R.layout.simple_spinner_item, daysOfWeek);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                breakfastSpinner.setAdapter(adapter);
                lunchSpinner.setAdapter(adapter);
                dinnerSpinner.setAdapter(adapter);
            }
        });
    }

    private void configureButtons() {
        csButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csButton.setBackgroundResource(R.color.purple);
                pgsButton.setBackgroundResource(android.R.color.transparent);
                archButton.setBackgroundResource(android.R.color.transparent);
                otherButton.setBackgroundResource(android.R.color.transparent);
                programStudy = "CS";
            }
        });

        pgsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgsButton.setBackgroundResource(R.color.purple);
                csButton.setBackgroundResource(android.R.color.transparent);
                archButton.setBackgroundResource(android.R.color.transparent);
                otherButton.setBackgroundResource(android.R.color.transparent);
                programStudy = "Postgraduate Studies";
            }
        });

        archButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archButton.setBackgroundResource(R.color.purple);
                csButton.setBackgroundResource(android.R.color.transparent);
                pgsButton.setBackgroundResource(android.R.color.transparent);
                otherButton.setBackgroundResource(android.R.color.transparent);
                programStudy = "Architecture";
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherButton.setBackgroundResource(R.color.purple);
                csButton.setBackgroundResource(android.R.color.transparent);
                pgsButton.setBackgroundResource(android.R.color.transparent);
                archButton.setBackgroundResource(android.R.color.transparent);
                programStudy = "Other";
            }
        });
    }

    // Generate list of dates between start and end dates
    private List<String> generateDates(Date startDate, Date endDate) {
        List<String> daysOfWeek = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (!calendar.getTime().after(endDate)) {
            Date currentDate = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            String dateString = dateFormat.format(currentDate);
            daysOfWeek.add(dateString);
            calendar.add(Calendar.DATE, 1);
        }
        return daysOfWeek;
    }

    // TODO: Connect to database
}
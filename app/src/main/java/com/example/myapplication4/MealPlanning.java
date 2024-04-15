package com.example.myapplication4;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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
    private RadioButton csButton, pgsButton, archButton, otherButton;
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
//
//        // Initialize EditText fields
//        startDateEditText = findViewById(R.id.editTextDate2);
//        endDateEditText = findViewById(R.id.editTextDate3);
//
//        // Initialize Spinners
//        breakfastSpinner = findViewById(R.id.breakfastSpinner);
//        lunchSpinner = findViewById(R.id.lunchSpinner);
//        dinnerSpinner = findViewById(R.id.dinnerSpinner);
//
//        // Configure RadioButtons to change background color on selection
//        configureRadioButtons();
//
//        // Handle submit button click
//        Button submitButton = findViewById(R.id.submitButton);
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Retrieve user input
//                String firstName = firstNameEditText.getText().toString();
//                String lastName = lastNameEditText.getText().toString();
//                String email = emailEditText.getText().toString();
//
//                // Retrieve selected program of study
//                String programOfStudy = getProgramOfStudy();
//
//                // Retrieve user input
//                String startDateString = startDateEditText.getText().toString();
//                String endDateString = endDateEditText.getText().toString();
//
//                // Convert start and end dates to Date objects
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
//                Date startDate = null;
//                Date endDate = null;
//                try {
//                    startDate = dateFormat.parse(startDateString);
//                    endDate = dateFormat.parse(endDateString);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                // Generate list of dates between start and end dates
//                List<String> daysOfWeek = generateDates(startDate, endDate);
//
//                // Populate Spinners with options
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(MealPlanning.this, android.R.layout.simple_spinner_item, daysOfWeek);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                breakfastSpinner.setAdapter(adapter);
//                lunchSpinner.setAdapter(adapter);
//                dinnerSpinner.setAdapter(adapter);
//            }
//        });
    }

    // Configure RadioButtons to change background color on selection
    private void configureRadioButtons() {
        csButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    csButton.setBackgroundResource(R.color.purple);
                } else {
                    csButton.setBackgroundResource(android.R.color.transparent);
                }
            }
        });

        pgsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pgsButton.setBackgroundResource(R.color.purple);
                } else {
                    pgsButton.setBackgroundResource(android.R.color.transparent);
                }
            }
        });

        archButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    archButton.setBackgroundResource(R.color.purple);
                } else {
                    archButton.setBackgroundResource(android.R.color.transparent);
                }
            }
        });

        otherButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    otherButton.setBackgroundResource(R.color.purple);
                } else {
                    otherButton.setBackgroundResource(android.R.color.transparent);
                }
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

    // Retrieve selected program of study
    private String getProgramOfStudy() {
        if (csButton.isChecked()) {
            return "Computer Science";
        } else if (pgsButton.isChecked()) {
            return "Postgraduate Studies";
        } else if (archButton.isChecked()) {
            return "Architecture";
        } else if (otherButton.isChecked()) {
            return "Other";
        } else {
            return "";
        }
    }

    // TODO: Connect to database
}
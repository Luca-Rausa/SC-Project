package com.example.myapplication4;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MealPlanning extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText;
    private RadioButton csButton, pgsButton, archButton, otherButton;
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
        csButton = findViewById(R.id.csButton);
        pgsButton = findViewById(R.id.pgsButton);
        archButton = findViewById(R.id.archButton);
        otherButton = findViewById(R.id.otherButton);

        // Initialize Spinners
        breakfastSpinner = findViewById(R.id.breakfastSpinner);
        lunchSpinner = findViewById(R.id.lunchSpinner);
        dinnerSpinner = findViewById(R.id.dinnerSpinner);

        // Configure RadioButtons to change background color on selection
        configureRadioButtons();

        // Populate Spinners with options
        populateSpinners();

        // Handle submit button click
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Retrieve selected program of study
                String programOfStudy = getProgramOfStudy();

                // Retrieve selected days for breakfast, lunch, and dinner
                List<String> breakfastDays = getSelectedDays(breakfastSpinner);
                List<String> lunchDays = getSelectedDays(lunchSpinner);
                List<String> dinnerDays = getSelectedDays(dinnerSpinner);
            }
        });
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

    // Populate Spinners with options
    private void populateSpinners() {
        // Dummy data for spinner options
        List<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("Monday");
        daysOfWeek.add("Tuesday");
        daysOfWeek.add("Wednesday");
        daysOfWeek.add("Thursday");
        daysOfWeek.add("Friday");
        daysOfWeek.add("Saturday");
        daysOfWeek.add("Sunday");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daysOfWeek);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        breakfastSpinner.setAdapter(adapter);
        lunchSpinner.setAdapter(adapter);
        dinnerSpinner.setAdapter(adapter);
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

    // Retrieve selected days from a spinner
    private List<String> getSelectedDays(Spinner spinner) {
        List<String> selectedDays = new ArrayList<>();
        String selectedDay = spinner.getSelectedItem().toString();
        selectedDays.add(selectedDay);
        // Add logic here if you want to handle multiple selections
        return selectedDays;
    }

    // TODO: Connect to database
}
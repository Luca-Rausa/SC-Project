package com.example.myapplication4;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.Locale;
import androidx.appcompat.widget.Toolbar;

public class TravelItinerary extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText firstNameEditText, lastNameEditText, emailEditText, editTextDate, endDateEditText, itineraryDescEditText;
    private String travelGroup;
    private RadioGroup radioGroup;
    private Spinner spinner2;
    private Button submitButton;
    private TravelHelper dbHelper;
    private EditText activeDateField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_itinerary);

        dbHelper = new TravelHelper(this);

        // Initialize UI elements
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        emailEditText = findViewById(R.id.editTextEmailAddress);
        editTextDate = findViewById(R.id.editTextDate);
        endDateEditText = findViewById(R.id.endDate);
        itineraryDescEditText = findViewById(R.id.itineraryDesc);
        spinner2 = findViewById(R.id.spinner2);
        submitButton = findViewById(R.id.submit);
        radioGroup = findViewById(R.id.radioGroup);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enabling the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set onClickListener for the date fields
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeDateField = editTextDate;
                showDatePickerDialog(editTextDate);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeDateField = endDateEditText;
                showDatePickerDialog(endDateEditText);
            }
        });

        // Initialize spinner2 with options
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.groups, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        // Listen for spinner2 item selection
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travelGroup = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set onClickListener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs from EditText fields
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String startDate = editTextDate.getText().toString();
                String endDate = endDateEditText.getText().toString();
                String itineraryDesc = itineraryDescEditText.getText().toString();
                String groupTravel = "";
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId == R.id.yes) {
                    groupTravel = "Yes";
                } else if (selectedRadioButtonId == R.id.no) {
                    groupTravel = "No";
                }
                String selectedGroup = travelGroup;

                // Save data to SQLite database
                saveData(firstName, lastName, email, startDate, endDate, itineraryDesc, groupTravel, selectedGroup);

                // Navigate to the home page
                startActivity(new Intent(TravelItinerary.this, Forms.class));
                finish();
            }
        });
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog(EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Callback method for DatePickerDialog
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Set the selected date to the EditText field
        String date = String.format(Locale.getDefault(), "%d/%02d/%02d", year, month + 1, dayOfMonth);
        activeDateField.setText(date); // You can set the end date EditText as well if needed
    }

    private void saveData(String firstName, String lastName, String email, String startDate,
                          String endDate, String itineraryDesc, String groupTravel, String travelGroup) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TravelHelper.COLUMN_FIRST_NAME, firstName);
        values.put(TravelHelper.COLUMN_LAST_NAME, lastName);
        values.put(TravelHelper.COLUMN_EMAIL, email);
        values.put(TravelHelper.COLUMN_START_DATE, startDate);
        values.put(TravelHelper.COLUMN_END_DATE, endDate);
        values.put(TravelHelper.COLUMN_DESCRIPTION, itineraryDesc);
        values.put(TravelHelper.COLUMN_TRAVEL_GROUP, travelGroup);
        values.put(TravelHelper.COLUMN_PROGRAM_OF_STUDY, groupTravel);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TravelHelper.TABLE_NAME, null, values);
        System.out.println(TravelHelper.TABLE_NAME);
        // You can handle the result of the insertion if needed
        if (newRowId != -1) {
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle toolbar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            startActivity(new Intent(TravelItinerary.this, Forms.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
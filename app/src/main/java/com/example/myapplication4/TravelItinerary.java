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

public class TravelItinerary extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText firstNameEditText, lastNameEditText, emailEditText, editTextDate, endDateEditText, itineraryDescEditText;
    private String travelGroup;
    private RadioGroup radioGroup;
    private Spinner spinner2;
    private Button submitButton;
    private DatabaseHelper dbHelper;
    private EditText activeDateField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_itinerary);

        dbHelper = new DatabaseHelper(this);

        // Initialize elements
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

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.groups, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

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

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || startDate.isEmpty()
                        || endDate.isEmpty() || itineraryDesc.isEmpty() || groupTravel.isEmpty()) {
                    Toast.makeText(TravelItinerary.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save data to database
                saveData(firstName, lastName, email, startDate, endDate, itineraryDesc, groupTravel, selectedGroup);

                // Navigate to the home page
                startActivity(new Intent(TravelItinerary.this, Forms.class));
                finish();
            }
        });
    }

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Set the selected date to the EditText field
        String date = String.format(Locale.getDefault(), "%d/%02d/%02d", year, month + 1, dayOfMonth);
        activeDateField.setText(date);
    }

    private void saveData(String firstName, String lastName, String email, String startDate,
                          String endDate, String itineraryDesc, String groupTravel, String travelGroup) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_FIRSTNAME, firstName);
        values.put(DatabaseHelper.COL_LASTNAME, lastName);
        values.put(DatabaseHelper.COL_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_START_DATE, startDate);
        values.put(DatabaseHelper.COLUMN_END_DATE, endDate);
        values.put(DatabaseHelper.COLUMN_TRAVEL_DESCRIPTION, itineraryDesc);
        values.put(DatabaseHelper.COLUMN_TRAVEL_GROUP, groupTravel);
        values.put(DatabaseHelper.COLUMN_PROGRAM_OF_STUDY_GROUP, travelGroup);

        long newRowId = db.insert(DatabaseHelper.TABLE_ITINERARY, null, values);
        System.out.println(DatabaseHelper.TABLE_ITINERARY);
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
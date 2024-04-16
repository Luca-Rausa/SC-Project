package com.example.myapplication4;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class TravelItinerary extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText, editTextDate, endDateEditText, itineraryDescEditText;
    private String travelDestination, travelGroup;
    private Spinner spinner, spinner2;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_itinerary);

        // Initialize UI elements
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        editTextDate = findViewById(R.id.editTextDate);
        endDateEditText = findViewById(R.id.endDate);
        itineraryDescEditText = findViewById(R.id.itineraryDesc);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        submitButton = findViewById(R.id.submit);

        // Initialize spinner with options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.destinations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Listen for spinner item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                travelDestination = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Initialize spinner2 with options
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.groups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

                // TODO: Save data

                // Navigate to the home page
                startActivity(new Intent(TravelItinerary.this, Home.class));
                finish();
            }
        });
    }
}

package com.example.myapplication4;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewEvent extends AppCompatActivity {
    private EditText eventTitle;
    private EditText eventDescription;
    private Spinner eventTypeSpinner;
    private EditText eventAttendees;
    private EditText eventDate;
    private EditText eventStartTime;
    private EditText eventDuration;
    private ArrayAdapter<String> fileListAdapter;
    private LinksAdapter linksListAdapter;
    private EditText eventLinkName;
    private EditText eventLinkUrl;
    private Button addEventButton;
    private EventDatabaseHelper eventDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);

        eventTitle = findViewById(R.id.edTxtEventTitle);
        eventDescription = findViewById(R.id.edTxtEventDescription);
        eventTypeSpinner = findViewById(R.id.spinnerEventType);
        eventAttendees = findViewById(R.id.edTxtEventAttendees);
        eventDate = findViewById(R.id.edTxtEventDate);
        eventStartTime = findViewById(R.id.edTxtEventStartTime);
        eventDuration = findViewById(R.id.edTxtEventDuration);
        eventLinkName = findViewById(R.id.edTxtEventLinkName);
        eventLinkUrl = findViewById(R.id.edTxtEventLinkURL);

        eventDatabaseHelper = new EventDatabaseHelper(this);

        EventType[] eventTypes = EventType.values();
        EventTypeAdapter eventTypeAdapter = new EventTypeAdapter(this, R.layout.event_spinner_item, eventTypes);
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(eventTypeAdapter);

        eventDate.setOnClickListener(v -> showDatePickerDialog());
        eventStartTime.setOnClickListener(v -> showTimePickerDialog());

        List<Bitmap> fileList = new ArrayList<>();
        List<Uri> uriList = new ArrayList<>();
        ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        uriList.add(result);
                        fileList.add(convertUriToBitmap(result));
                        List<String> fileNames = new ArrayList<>();
                        for (Uri uri : uriList) {
                            String fileName = getFileNameFromUri(uri);
                            fileNames.add(fileName);
                        }
                        fileListAdapter.clear();
                        fileListAdapter.addAll(fileNames);
                        fileListAdapter.notifyDataSetChanged();
                    }
                });
        FloatingActionButton btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUploadImage.setOnClickListener(view -> filePickerLauncher.launch("image/*"));

        ListView imagesListView = findViewById(R.id.imagesListView);
        fileListAdapter = new ArrayAdapter<>(this, R.layout.event_spinner_item);
        imagesListView.setAdapter(fileListAdapter);

        List<Pair<String,String>> linksList = new ArrayList<>();
        FloatingActionButton btnUploadLink = findViewById(R.id.btnUploadLink);
        btnUploadLink.setOnClickListener(view -> {
            String strLinkName = eventLinkName.getText().toString();
            String strLinkUrl = eventLinkUrl.getText().toString();
            if(!strLinkName.isEmpty() && isValidUrl(strLinkUrl)) {
                linksList.add(new Pair<>(strLinkName, strLinkUrl));
                eventLinkName.setText("");
                eventLinkUrl.setText("");

                linksListAdapter.notifyDataSetChanged();
            }
        });

        ListView linksListView = findViewById(R.id.linkListView);
        linksListAdapter = new LinksAdapter(this, linksList);
        linksListView.setAdapter(linksListAdapter);


        addEventButton = findViewById(R.id.btnAddEvent);
        addEventButton.setEnabled(false);
        eventTitle.addTextChangedListener(textWatcher);
        eventDescription.addTextChangedListener(textWatcher);
        eventTypeSpinner.setOnItemSelectedListener(spinnerListener);
        eventDate.addTextChangedListener(textWatcher);
        eventStartTime.addTextChangedListener(textWatcher);
        eventDuration.addTextChangedListener(textWatcher);
        addEventButton.setOnClickListener(view -> {
            String strDate = eventDate.getText().toString() + " " + eventStartTime.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date();
            try {
                date = sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Event event = new Event(
                    eventTitle.getText().toString(),
                    eventDescription.getText().toString(),
                    EventType.valueOf(eventTypeSpinner.getSelectedItem().toString()),
                    date,
                    Integer.parseInt(eventDuration.getText().toString()),
                    "Luca Rausa"
            );

            if(!eventAttendees.getText().toString().isEmpty())
                event.setAttendees(Integer.parseInt(eventAttendees.getText().toString()));
            if(fileList.size() != 0)
                event.setImages(fileList);
            if(linksList.size() != 0)
                event.setLinks(linksList);

            eventDatabaseHelper.addEvent(event);
            startActivity(new Intent(NewEvent.this, Home.class));
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String dayString = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                    String monthString = ((monthOfYear + 1) < 10) ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                    String selectedDate = dayString + "/" + monthString + "/" + year;
                    eventDate.setText(selectedDate);
                }, yearNow, monthNow, dayOfMonthNow);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteNow = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String strHours = (hourOfDay < 10) ? "0"+String.valueOf(hourOfDay) : String.valueOf(hourOfDay);
                    String strMinutes = (minute < 10) ? "0"+String.valueOf(minute) : String.valueOf(minute);
                    String time = strHours + ":" + strMinutes;
                    eventStartTime.setText(time);
                }, hourNow, minuteNow, false);
        timePickerDialog.show();
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        fileName = cursor.getString(displayNameIndex);
                    } else {
                        fileName = uri.getLastPathSegment();
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }

    private static boolean isValidUrl(String input) {
        String regex = "^(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ;,./?%&=]*)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) { enableNewEventButton(); }
    };

    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { enableNewEventButton(); }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private void enableNewEventButton() {
        boolean allFieldsFilled = !eventTitle.getText().toString().isEmpty() &&
                !eventDescription.getText().toString().isEmpty() &&
                !(eventTypeSpinner.getSelectedItem()).toString().isEmpty() &&
                !eventDate.getText().toString().isEmpty() &&
                !eventStartTime.getText().toString().isEmpty() &&
                !eventDuration.getText().toString().isEmpty();
        if(allFieldsFilled)
            addEventButton.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.dark_blue));
        else
            addEventButton.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.light_gray));
        addEventButton.setEnabled(allFieldsFilled);
    }

    private Bitmap convertUriToBitmap(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
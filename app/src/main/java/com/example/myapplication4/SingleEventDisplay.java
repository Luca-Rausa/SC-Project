package com.example.myapplication4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SingleEventDisplay  extends AppCompatActivity{
    private TextView eventTitle;
    private ViewPager eventImageViewer;
    private TextView eventDescription;
    private TextView eventType;
    private TextView eventCreator;
    private TextView eventPlaces;
    private TextView eventDate;
    private TextView eventDuration;
    private ListView eventLinks;
    private Button btnAttendEvent;
    private ListView attendeesListView;

    private TextView attendeesTextView;
    private List<String> attendingUsers;
    private ArrayAdapter<String> attendeesAdapter;
    private LinksAdapter linksListAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_event_display);
        databaseHelper = new DatabaseHelper(this);

        eventTitle = findViewById(R.id.displayEventTitle);
        eventDescription = findViewById(R.id.displayEventDescription);
        eventType = findViewById(R.id.displayEventType);
        eventCreator = findViewById(R.id.displayEventCreator);
        eventDate = findViewById(R.id.displayEventDate);
        eventPlaces = findViewById(R.id.displayEventMaxAttendees);
        eventDuration = findViewById(R.id.displayEventDuration);
        eventImageViewer = findViewById(R.id.displayViewPager);
        eventLinks = findViewById(R.id.displayEventLinks);
        btnAttendEvent = findViewById(R.id.btnAttendEvent);
        attendeesListView = findViewById(R.id.attendeesListView);
        attendeesTextView = findViewById(R.id.attendeesTextView);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enabling the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        long eventId = intent.getLongExtra("event", -1);
        if(eventId != -1) {
            Event event = databaseHelper.getEvent(eventId, true);
            eventTitle.setText(event.getTitle());
            eventDescription.setText(event.getDescription());
            eventType.setText(event.getType().getStringValue());
            eventCreator.setText(event.getCreatorUsername());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            eventDate.setText(dateFormat.format(event.getDate()));
            eventPlaces.setText(String.valueOf(event.getAttendees()));
            eventDuration.setText(getString(R.string.event_single_event_duration, String.valueOf(event.getDuration())));
            if(event.getLinks() != null && !event.getLinks().isEmpty()) {
                linksListAdapter = new LinksAdapter(this, event.getLinks());
                eventLinks.setAdapter(linksListAdapter);
            }

            List<Bitmap> images = event.getImages();
            ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(images);
            eventImageViewer.setAdapter(pagerAdapter);

            boolean isUserAttendingEvent = databaseHelper.isUserAttendingEvent(MainActivity.user.getId(), eventId);
            boolean isUserEventCreator = databaseHelper.isUserEventCreator(MainActivity.user.getEmail(), eventId);
            boolean isEventFull = databaseHelper.isEventFull(eventId);
            if(!isUserEventCreator) {
                attendeesListView.setVisibility(View.GONE);
                attendeesTextView.setVisibility(View.GONE);
                btnAttendEvent.setVisibility(View.VISIBLE);

                if (!isUserAttendingEvent && !isEventFull)
                    btnAttendEvent.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.dark_blue));
                else
                    btnAttendEvent.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.light_gray));
                btnAttendEvent.setEnabled(!isUserAttendingEvent && !isEventFull);

                btnAttendEvent.setOnClickListener(v -> {
                    databaseHelper.attendEvent(MainActivity.user.getId(), eventId);
                    finish();
                });
            } else {
                attendeesListView.setVisibility(View.VISIBLE);
                attendeesTextView.setVisibility(View.VISIBLE);
                btnAttendEvent.setVisibility(View.GONE);

                attendingUsers = databaseHelper.getAttendingUsersMail(eventId);
                attendeesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, attendingUsers);
                attendeesListView.setAdapter(attendeesAdapter);
            }
        }
    }

    // Handle toolbar item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            startActivity(new Intent(SingleEventDisplay.this, EventHub.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
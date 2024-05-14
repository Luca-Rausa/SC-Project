package com.example.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MyEvents extends AppCompatActivity implements EventListAdapter.OnImageViewClickListener{
    private List<Event> events;
    private List<Event> attendedEvents;
    private ListView eventListView;
    private ListView attendListView;
    private EventListAdapter eventListAdapter;
    private EventListAdapter attendeListAdapter;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events);

        databaseHelper = new DatabaseHelper(this);
        events = databaseHelper.getAllEventsByCreator(MainActivity.user.getEmail());
        attendedEvents = databaseHelper.getAttendedEvents(MainActivity.user.getId());
        eventListView = findViewById(R.id.eventListView);
        attendListView = findViewById(R.id.attendeeListView);
        eventListAdapter = new EventListAdapter(this, events, R.layout.my_event_list_item);
        eventListView.setAdapter(eventListAdapter);
        attendeListAdapter = new EventListAdapter(this, attendedEvents, R.layout.event_list_item);
        attendListView.setAdapter(attendeListAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventListAdapter.setSelectedItem(position);
                Event selectedEvent = events.get(position);
                Intent intent = new Intent(MyEvents.this, SingleEventDisplay.class);
                if (selectedEvent != null) {
                    intent.putExtra("event", selectedEvent.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(MyEvents.this, "An error occurred while opening the event", Toast.LENGTH_SHORT).show();
                }
            }
        });

        eventListAdapter.setOnImageViewClickListener((this));

        attendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                attendeListAdapter.setSelectedItem(position);
                Event selectedEvent = attendedEvents.get(position);
                Intent intent = new Intent(MyEvents.this, SingleEventDisplay.class);
                if (selectedEvent != null) {
                    intent.putExtra("event", selectedEvent.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(MyEvents.this, "An error occurred while opening the event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onImageViewClick(int position) {
            Event eventToRemove = events.get(position);
            if (eventToRemove != null) {
                databaseHelper.removeEvent(eventToRemove);
                events.remove(position);
                eventListAdapter.notifyDataSetChanged();
                Toast.makeText(MyEvents.this, "Event removed successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyEvents.this, "An error occurred while removing the event", Toast.LENGTH_SHORT).show();
            }
    }
}

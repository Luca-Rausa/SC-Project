package com.example.myapplication4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EventHub extends AppCompatActivity {
    private List<Event> events;
    private ListView eventListView;
    private EventListAdapter eventListAdapter;
    private EventDatabaseHelper eventDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_hub);

        Button btnNewEvent = findViewById(R.id.btnNewEvent);
        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventHub.this, NewEvent.class));
            }
        });

        Button btnMyEvents = findViewById(R.id.btnMyEvents);
        btnMyEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventHub.this, MyEvents.class));
            }
        });

        eventDatabaseHelper = new EventDatabaseHelper(this);
        events = eventDatabaseHelper.getAllEvents();
        eventListView = findViewById(R.id.eventListView);
        eventListAdapter = new EventListAdapter(this, events, R.layout.event_list_item);
        eventListView.setAdapter(eventListAdapter);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventListAdapter.setSelectedItem(position);
                Event selectedEvent = events.get(position);
                Intent intent = new Intent(EventHub.this, SingleEventDisplay.class);
                intent.putExtra("event", selectedEvent.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventListAdapter.clear();
        eventListAdapter.addAll(eventDatabaseHelper.getAllEvents());
        eventListAdapter.notifyDataSetChanged();
    }
}
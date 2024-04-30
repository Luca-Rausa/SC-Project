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
    private ArrayAdapter<String> eventListAdapter;
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
                // TODO
            }
        });

        eventDatabaseHelper = new EventDatabaseHelper(this);
        eventListView = findViewById(R.id.eventListView);
        eventListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        eventListView.setAdapter(eventListAdapter);
        loadEvents();

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = events.get(position);
                if(selectedEvent.getImages() == null)
                    selectedEvent.setImages(new ArrayList<>());
                if(selectedEvent.getLinks() == null)
                    selectedEvent.setLinks(new ArrayList<>());
                Intent intent = new Intent(EventHub.this, SingleEventDisplay.class);
                intent.putExtra("event", selectedEvent);
                startActivity(intent);
            }
        });
    }

    private void loadEvents() {
        events = eventDatabaseHelper.getAllEvents();
        List<String> eventTitles = new ArrayList<>();
        for (Event event : events) {
            eventTitles.add(event.getTitle());
        }
        eventListAdapter.clear();
        eventListAdapter.addAll(eventTitles);
        eventListAdapter.notifyDataSetChanged();
    }
}
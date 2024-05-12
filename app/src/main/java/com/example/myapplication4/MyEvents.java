package com.example.myapplication4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MyEvents extends AppCompatActivity implements EventListAdapter.OnImageViewClickListener{
    private List<Event> events;
    private ListView eventListView;
    private EventListAdapter eventListAdapter;
    private EventDatabaseHelper eventDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events);

        eventDatabaseHelper = new EventDatabaseHelper(this);
        events = eventDatabaseHelper.getAllEvents();
        eventListView = findViewById(R.id.eventListView);
        eventListAdapter = new EventListAdapter(this, events, R.layout.my_event_list_item);
        eventListView.setAdapter(eventListAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventListAdapter.setSelectedItem(position);
                Event selectedEvent = events.get(position);
                Intent intent = new Intent(MyEvents.this, SingleEventDisplay.class);
                intent.putExtra("event", selectedEvent.getId());
                startActivity(intent);
            }
        });

        eventListAdapter.setOnImageViewClickListener((this));
    }

    @Override
    public void onImageViewClick(int position) {
            Event eventToRemove = events.get(position);
            if (eventToRemove != null) {
                eventDatabaseHelper.removeEvent(eventToRemove);
                events.remove(position);
                eventListAdapter.notifyDataSetChanged();
            }
    }
}

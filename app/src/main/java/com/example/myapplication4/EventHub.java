package com.example.myapplication4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventHub extends AppCompatActivity {

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

        /*EventDatabaseHelper dbHelper = new EventDatabaseHelper(this);
        List<Event> eventList = dbHelper.getAllEvents();
        List<String> eventTitles = new ArrayList<>();
        for(Event event:eventList)
            eventTitles.add(event.getTitle());
        ArrayAdapter<String> titlesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventTitles);
        ListView listView = findViewById(R.id.eventsListView);
        listView.setAdapter(titlesAdapter);*/
    }
}
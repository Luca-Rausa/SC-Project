package com.example.myapplication4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EventHub extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
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
        //Navigation Bar
        bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.events);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (itemId == R.id.forms) {
                    startActivity(new Intent(getApplicationContext(), Forms.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (itemId == R.id.events) {
//                    startActivity(new Intent(getApplicationContext(), EventHub.class));
//                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (itemId == R.id.feedback) {
                    startActivity(new Intent(getApplicationContext(), Feedback.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                else if (itemId == R.id.chatbot) {
                    startActivity(new Intent(getApplicationContext(), Chatbot.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }
}
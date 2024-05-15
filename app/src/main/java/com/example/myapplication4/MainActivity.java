package com.example.myapplication4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
//import com.example.myapplication4.databinding.NavigationBarBinding;
//import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //NavigationBarBinding binding;
    //BottomNavigationView bottomNavigationView;
    public static boolean isLoggedIn = false;
    public static boolean isStaff = false;
    public static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        //setContentView(binding.getRoot());

       // bottomNavigationView = findViewById(R.id.bottomNavView);
        // Navigation bar configs
        //binding = NavigationBarBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());


        //binding.bottomNavView.setSelectedItemId(R.id.home);

//        binding.bottomNavView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.home) {
//                startActivity(new Intent(MainActivity.this, Home.class));
//            } else if (itemId == R.id.forms) {
//                startActivity(new Intent(MainActivity.this, Forms.class));
//            } else if (itemId == R.id.events) {
//                startActivity(new Intent(MainActivity.this, EventHub.class));
//            } else if (itemId == R.id.feedback) {
//                startActivity(new Intent(MainActivity.this, Feedback.class));
//            } else if (itemId == R.id.chatbot) {
//                startActivity(new Intent(MainActivity.this, Chatbot.class));
//            }
//            return true;
//        });

        // If the user is not logged in, start the Welcome activity
        if (!isLoggedIn) {
            startActivity(new Intent(this, Welcome.class));
            finish();
        }
        else {
            startActivity(new Intent(this, Home.class));
            finish();
        }
    }
}

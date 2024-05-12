package com.example.myapplication4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    public static boolean isLoggedIn = false;
    public static boolean isStaff = false;
    public static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

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

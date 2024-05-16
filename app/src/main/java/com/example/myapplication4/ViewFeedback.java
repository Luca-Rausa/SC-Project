package com.example.myapplication4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewFeedback extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private FeedbackAdapter feedbackAdapter;
    private ListView listView;
    public List<FeedBackData> FeedBackDataList;
    Button backbtn;
    Button feedbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        FeedBackDataList= new ArrayList<>();
        setContentView(R.layout.feedback_see_feedback);
        listView= findViewById(R.id.feedlist);
        FeedBackDataList=dbHelper.GetFeedbacks();
        feedbackAdapter= new FeedbackAdapter(this,FeedBackDataList);
        listView.setAdapter(feedbackAdapter);
        backbtn= findViewById(R.id.backbtn);
        feedbtn= findViewById(R.id.feedbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewFeedback.this, Home.class));
            }
        });
        feedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewFeedback.this, Feedback.class));
            }
        });
    }

}

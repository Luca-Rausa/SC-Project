package com.example.myapplication4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class Chatbot extends AppCompatActivity {
    private Button buttonSend;
    private EditText editTextMessage;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    private void sendMessageToAPI(String message) {
        String url = "https://f2f3-213-180-189-122.ngrok-free.app/chat";

        // Create JSON object with user input
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_input", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a POST request to the Flask API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        // Handle response from Flask API
                        String botResponse = response.getString("bot_response");
                        System.out.println("Bot response: " + botResponse);

                        // Update UI with bot's response
                        addMessageToRecyclerView(new ChatMessage(botResponse, false));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    System.out.println("Error: " + error.toString());
                });
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot);

        // Initialize views and RecyclerView adapter here
        buttonSend = findViewById(R.id.buttonSend);
        editTextMessage = findViewById(R.id.editTextMessage);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    // Add user message to RecyclerView
                    addMessageToRecyclerView(new ChatMessage(message, true));
                    // Send message to Flask API
                    sendMessageToAPI(message);
                    // Clear input field
                    editTextMessage.setText("");
                }
            }
        });
    }

    private void addMessageToRecyclerView(ChatMessage message) {
        // Add the message to the adapter
        chatAdapter.addMessage(message);
        // Scroll to the bottom of the RecyclerView
        recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }
}

package com.example.myapplication4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<ChatMessage> messages;

    public ChatAdapter() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.item_chat_message_user, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_chat_message_bot, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isSentByUser() ? 0 : 1;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }

        void bind(ChatMessage message) {
            textViewMessage.setText(message.getMessage());
        }
    }
}

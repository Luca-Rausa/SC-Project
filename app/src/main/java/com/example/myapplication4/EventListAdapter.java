package com.example.myapplication4;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventListAdapter extends ArrayAdapter<Event> {

    public EventListAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_item, parent, false);
        }
        int backgroundColor = position % 2 == 0 ? Color.rgb(255,255,255) : Color.rgb(220,220,220);
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.event_list_item_background);
        if (drawable != null) {
            drawable.setColor(backgroundColor);
            convertView.setBackground(drawable);
        }
        TextView textViewEventTitle = convertView.findViewById(R.id.textViewEventTitle);
        TextView textViewEventDate = convertView.findViewById(R.id.textViewEventDate);
        TextView textViewEventType = convertView.findViewById(R.id.textViewEventType);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        if (event != null) {
            textViewEventTitle.setText(event.getTitle());
            textViewEventDate.setText(dateFormat.format(event.getDate()));
            textViewEventType.setText(event.getType().getStringValue());
        }

        return convertView;
    }
}

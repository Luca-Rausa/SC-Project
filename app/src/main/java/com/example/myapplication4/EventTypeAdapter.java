package com.example.myapplication4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventTypeAdapter extends ArrayAdapter<EventType> {

    private final LayoutInflater mInflater;
    private final EventType[] mValues;

    public EventTypeAdapter(Context context, int textViewResourceId, EventType[] values) {
        super(context, textViewResourceId, values);
        mInflater = LayoutInflater.from(context);
        mValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(mValues[position].getStringValue());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) mInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        view.setText(mValues[position].getStringValue());
        return view;
    }
}

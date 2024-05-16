package com.example.myapplication4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventListAdapter extends ArrayAdapter<Event> {
    private int selected_item = -1;
    private static final long CLICK_DELAY = 10;
    private final int layout;

    public EventListAdapter(Context context, List<Event> events, int layout) {
        super(context, 0, events);
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }
        int backgroundColor = position % 2 == 0 ? Color.rgb(255,255,255) : Color.rgb(220,220,220);
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.event_list_item_background);
        if(position==selected_item) {
            float[] hsv = new float[3];
            Color.colorToHSV(backgroundColor, hsv);
            hsv[2] *= 0.8f;
            backgroundColor = Color.HSVToColor(hsv);
            convertView.postDelayed(() -> {
                selected_item = -1;
                notifyDataSetChanged();
            }, CLICK_DELAY);
        }
        if(drawable != null) {
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setColor(backgroundColor);
            drawable.setCornerRadius(8);
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

        ImageView imageView = convertView.findViewById(R.id.imageViewRedCross);
        if(imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageViewClickListener != null) {
                        imageViewClickListener.onImageViewClick(position);
                    }
                }
            });
        }

        return convertView;
    }

    public void setSelectedItem(int position) {
        selected_item = position;
        notifyDataSetChanged();
    }

    public interface OnImageViewClickListener {
        void onImageViewClick(int position);
    }

    private OnImageViewClickListener imageViewClickListener;

    public void setOnImageViewClickListener(OnImageViewClickListener listener) {
        if (layout == R.layout.my_event_list_item)
            imageViewClickListener = listener;
    }
}
package com.example.myapplication4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FeedbackAdapter extends ArrayAdapter<FeedBackData> {

    public FeedbackAdapter(Context context, List<FeedBackData> resource) {
        super(context, 0,resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FeedBackData feedBackData= getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.feedlistitem, parent, false);
        }
        TextView textViewFeedbackTitle = convertView.findViewById(R.id.feedbacktitle);
        TextView textViewRatingRTitle = convertView.findViewById(R.id.tratingtitle);

        if ( feedBackData!= null) {
            textViewFeedbackTitle.setText(feedBackData.Feed);
            textViewRatingRTitle.setText(feedBackData.rating);
        }return convertView;

    }
}

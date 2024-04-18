package com.example.myapplication4;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class LinksAdapter extends ArrayAdapter<Pair<String, String>> {
    private final List<Pair<String, String>> linkList;
    private final LayoutInflater inflater;

    public LinksAdapter(Context context, List<Pair<String, String>> linkList) {
        super(context, 0, linkList);
        this.linkList = linkList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_link_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = convertView.findViewById(R.id.textViewName);
            viewHolder.textViewUrl = convertView.findViewById(R.id.textViewUrl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Pair<String, String> pair = linkList.get(position);
        viewHolder.textViewName.setText(pair.first);
        viewHolder.textViewUrl.setText(pair.second);

        return convertView;
    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewUrl;
    }
}
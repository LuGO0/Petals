package com.example.userspart.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.userspart.CustomTypes.Event;
import com.example.userspart.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_event, parent, false);
        }

        TextView eventNameTextView = (TextView) convertView.findViewById(R.id.item_event_name);
        TextView timeTextView=(TextView)convertView.findViewById(R.id.item_time);
        TextView dateTextView=(TextView)convertView.findViewById(R.id.item_date);
        TextView locationTextView=(TextView)convertView.findViewById(R.id.item_location);



        Event event = getItem(position);

        eventNameTextView.setText(event.getEventName());
        timeTextView.setText(event.getTime());
        dateTextView.setText(event.getDate());
        locationTextView.setText(event.getLocation());

        return convertView;
    }
}

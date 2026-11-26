package com.example.bulletinboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.database.entity.Event;

import java.util.List;

public class EventAdaptor extends RecyclerView.Adapter<EventAdaptor.CustomViewHolder>
{
    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView locationView;
        TextView typeView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tv_eventListTitle);
            locationView = itemView.findViewById(R.id.tv_eventListLocation);
            typeView = itemView.findViewById(R.id.tv_eventListType);
        }
    }

    List<Event> events;

    public EventAdaptor(List<Event> events)
    {
        this.events = events;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_listevent, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Event currEvent = events.get(position);

        holder.nameView.setText("" + currEvent.Name);
        holder.locationView.setText("" + currEvent.Location);

        TextView typeText = holder.typeView;

        int type = currEvent.Type;

        if(type == EventType.CLUB.getCode())
        {
            // Purple

            typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_purple));
        }
        else if(type == EventType.FOOD.getCode())
        {
            // Red

            typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_red));
        }
        else if(type == EventType.SPORT.getCode())
        {
            // Lime Green

            typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lime_green));
        }
        else if(type == EventType.SOCIAL.getCode())
        {
            // Orange

            typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_orange));
        }
        else if(type == EventType.STUDENT_GOV.getCode())
        {
            // Blue

            typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_blue));
        }
        else if(type == EventType.OTHER.getCode())
        {
            // Grey

            typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_grey));
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}

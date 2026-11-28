package com.example.bulletinboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.models.Event;

import java.util.List;

public class EventAdaptor extends RecyclerView.Adapter<EventAdaptor.CustomViewHolder> {

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

    private List<Event> events;

    public EventAdaptor(List<Event> events) {
        this.events = events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
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

        holder.nameView.setText(currEvent.getName() != null ? currEvent.getName() : "");
        holder.locationView.setText(currEvent.getLocationLabel() != null ? currEvent.getLocationLabel() : "");

        TextView typeText = holder.typeView;
        String category = currEvent.getCategory();

        if (category == null) {
            category = "OTHER";
        }

        typeText.setText(category);

        switch (category.toUpperCase()) {
            case "CLUB":
                typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_purple));
                break;
            case "FOOD":
                typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_red));
                break;
            case "SPORT":
                typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lime_green));
                break;
            case "SOCIAL":
                typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_orange));
                break;
            case "STUDENT GOV":
                typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_blue));
                break;
            default: // OTHER
                typeText.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.bright_grey));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }
}
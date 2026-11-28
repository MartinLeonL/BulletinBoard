package com.example.bulletinboard;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bulletinboard.models.Event;

import java.util.List;

public class EventAdaptor extends RecyclerView.Adapter<EventAdaptor.CustomViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

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
    private OnItemClickListener listener;

    public EventAdaptor(List<Event> events) {
        this.events = events;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        String rawCategory = currEvent.getCategory();
        String displayCategory = (rawCategory == null || rawCategory.isEmpty()) ? "OTHER" : rawCategory;

        typeText.setText(displayCategory);

        int color = getColorForCategory(displayCategory);
        GradientDrawable badgeShape = new GradientDrawable();
        badgeShape.setShape(GradientDrawable.RECTANGLE);
        badgeShape.setCornerRadius(20f);
        badgeShape.setColor(color);
        typeText.setBackground(badgeShape);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currEvent);
            }
        });
    }

    private int getColorForCategory(String category) {
        switch (category.toUpperCase()) {
            case "CLUB":
                return Color.parseColor("#8A2BE2"); // Purple
            case "FOOD":
                return Color.parseColor("#D32F2F"); // Red
            case "SPORT":
                return Color.parseColor("#388E3C"); // Green
            case "SOCIAL":
                return Color.parseColor("#F57C00"); // Orange
            case "STUDENT GOV":
                return Color.parseColor("#1976D2"); // Blue
            default:
                return Color.parseColor("#616161"); // Gray
        }
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }
}
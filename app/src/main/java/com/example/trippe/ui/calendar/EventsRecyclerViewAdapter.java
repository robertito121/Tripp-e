package com.example.trippe.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trippe.R;
import com.example.trippe.model.Event;

import java.util.List;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    private List<? extends Event> events;

    public EventsRecyclerViewAdapter(List<Event> events){
        this.events = events;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        EventsViewHolder eventsViewHolder = new EventsViewHolder(view);
        return eventsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        try {
            Event event = events.get(position);
            holder.getName().setText(event.getName());
            holder.getTime().setText(event.getTime());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

package com.example.trippe.ui.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trippe.R;
import com.example.trippe.model.Event;

public class EventsViewHolder extends RecyclerView.ViewHolder {

    private CardView eventsCardView;
    private TextView name;
    private TextView time;


    public EventsViewHolder(View itemView) {
        super(itemView);
        setEventsCardView((CardView) itemView.findViewById(R.id.eventCard));
        setName((TextView) itemView.findViewById(R.id.eventName));
        setTime((TextView)itemView.findViewById(R.id.eventTime));
    }

    public CardView getEventsCardView() {
        return eventsCardView;
    }

    public void setEventsCardView(CardView eventsCardView) {
        this.eventsCardView = eventsCardView;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public void bind(final Event event, final EventsRecyclerViewAdapter.EventClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(event);
            }
        });
    }
}
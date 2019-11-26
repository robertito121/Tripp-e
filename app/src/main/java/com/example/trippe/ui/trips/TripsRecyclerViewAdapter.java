package com.example.trippe.ui.trips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trippe.R;
import com.example.trippe.model.Trip;

import java.util.List;

public class TripsRecyclerViewAdapter extends RecyclerView.Adapter<TripsViewHolder> {

    private List<? extends Trip> trips;

    public TripsRecyclerViewAdapter(List<Trip> trips){
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_card, parent, false);
        TripsViewHolder tripsViewHolder = new TripsViewHolder(view);
        return tripsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripsViewHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.getDestinationFlag().setImageResource(trips.get(position).getTripFlagIndicator());
        holder.getDestination().setText("Trip to " + trip.nationalTripToString());
        holder.getFromDate().setText(trips.get(position).getStartDate());
        holder.getToDate().setText(trips.get(position).getEndDate());
        holder.getMilesAwayFromHome().setText(String.valueOf(trips.get(position).getMilesAwayFromHome()));
        holder.getTimeZone().setText(trips.get(position).getTimeZone().getID());
        holder.getCurrency().setText(trips.get(position).getCurrency());
        holder.getLanguages().setText(trips.get(position).foreignLanguagesToString());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

package com.example.trippe.ui.trips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trippe.R;
import com.example.trippe.model.InternationalTrip;
import com.example.trippe.model.NationalTrip;
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
        //InternationalTrip internationalTrip = trips.get(position) instanceof InternationalTrip ? (InternationalTrip) trips.get(position) : null;
        holder.getDestinationFlag().setImageResource(trips.get(position).getTripFlagIndicator());
        holder.getDestination().setText("Trip to " + trip.toString());
        holder.getFromDate().setText(trips.get(position).getStartDate());
        holder.getToDate().setText(trips.get(position).getEndDate());
        holder.getMilesAwayFromHome().setText(String.valueOf(trips.get(position).getMilesAwayFromHome()));
        //holder.getCurrency().setText(internationalTrip.getCurrency());
        //holder.getLanguages().setText(internationalTrip.foreignLanguagesToString());

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

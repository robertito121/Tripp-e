package com.example.trippe.ui.trips;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trippe.MainActivity;
import com.example.trippe.R;
import com.example.trippe.dao.TripDao;
import com.example.trippe.model.Trip;

import java.util.ArrayList;

public class TripsFragment extends Fragment {

    private RecyclerView tripsRecyclerView;
    private View root;
    private ArrayList<Trip> trips;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_trips, container, false);
        tripsRecyclerView = (RecyclerView) root.findViewById(R.id.tripsList);
        tripsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        tripsRecyclerView.setLayoutManager(layoutManager);
        TripDao tripDao = new TripDao();
        trips = tripDao.getTrips();
        TripsRecyclerViewAdapter tripsRecyclerViewAdapter = new TripsRecyclerViewAdapter(trips);
        tripsRecyclerView.setAdapter(tripsRecyclerViewAdapter);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.trips_action_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_addTrip) {
            Intent intent = new Intent(this.getContext(), AddTripView.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
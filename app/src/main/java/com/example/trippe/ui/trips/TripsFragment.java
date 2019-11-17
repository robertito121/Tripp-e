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
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.trippe.MainActivity;
import com.example.trippe.R;

public class TripsFragment extends Fragment {

    private TripsViewModel tripsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        tripsViewModel =
                ViewModelProviders.of(this).get(TripsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trips, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        tripsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
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
package com.example.trippe.ui.trips;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
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

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_trips, container, false);
        tripsRecyclerView = (RecyclerView) root.findViewById(R.id.tripsList);
        tripsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        tripsRecyclerView.setLayoutManager(layoutManager);
        final TripDao tripDao = new TripDao();
        trips = tripDao.getTrips();
        final TripsRecyclerViewAdapter tripsRecyclerViewAdapter = new TripsRecyclerViewAdapter(trips);
        tripsRecyclerView.setAdapter(tripsRecyclerViewAdapter);
        ItemTouchHelper.SimpleCallback itemTouchCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    Trip tripToBeRemoved = tripsRecyclerViewAdapter.removeTripAtPosition(viewHolder.getAdapterPosition());
                    tripDao.removeTrip(tripToBeRemoved);
                    tripsRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final ColorDrawable background = new ColorDrawable(Color.RED);
                background.setBounds(viewHolder.itemView.getRight() + (int) dX , viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                background.draw(c);
                Drawable trashIcon  = ContextCompat.getDrawable(TripsFragment.super.getContext(), R.drawable.trash_icon_white);
                int iconSize = trashIcon.getIntrinsicHeight();
                int halfIcon = iconSize / 2;
                int imgLeft = viewHolder.itemView.getRight() + 30 + (int)dX;
                int top = viewHolder.itemView.getTop() + (viewHolder.itemView.getBottom() - viewHolder.itemView.getTop()) / 2 - halfIcon;
                trashIcon.setBounds(imgLeft, top, imgLeft + trashIcon.getIntrinsicWidth(), top + trashIcon.getIntrinsicHeight());
                trashIcon.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallBack);
        itemTouchHelper.attachToRecyclerView(tripsRecyclerView);
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
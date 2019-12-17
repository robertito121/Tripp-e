package com.example.trippe.ui.calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.trippe.R;
import com.example.trippe.model.Event;
import com.example.trippe.dao.CalendarDao;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class EventView extends AppCompatActivity {

    private TextView name;
    private TextView date;
    private TextView location;
    private TextView time;
    private TextView trip;
    private Button backToCalendarButton;
    private Event event;
    private CalendarFragment calendarFragment;
    private CalendarDao calendarDao = new CalendarDao();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        setTitle("Event");
        event = calendarDao.getEvent(getIntent().getIntExtra("EventPosition", 0));
        final Activity activity_event_view = this;

        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.location);
        time = (TextView) findViewById(R.id.time);
        trip = (TextView) findViewById(R.id.trip);
        backToCalendarButton = (Button) findViewById(R.id.backToCalendarButton);
        try {
            name.setText(event.getName());
            date.setText(event.getDate());
            location.setText(event.getLocation());
            time.setText(event.getTime());
            trip.setText(event.getTripID());
        } catch (Exception e) {
            e.printStackTrace();
        }

        backToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NavController navController = Navigation.findNavController(activity_event_view, R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_dashboard);

                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}

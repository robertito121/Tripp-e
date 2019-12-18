package com.example.trippe.ui.calendar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trippe.R;
import com.example.trippe.dao.CalendarDao;
import com.example.trippe.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;

    private SQLiteDatabase database;

    private static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;
    private TextView eventDate;
    private View view;
    private RecyclerView eventsRecyclerView;
    private ArrayList<Event> events;
    private Event activeEvent;
    private ArrayList<Event> listOfEvents;

    public CalendarFragment() {
        database = getConnection();
    }

    private SQLiteDatabase getConnection() {
        return SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mCalendarView = view.findViewById(R.id.calendarView);
        eventDate = view.findViewById(R.id.dateText);
        String date = getFormattedDate(mCalendarView.getDate());
        eventDate.setText(date);

        fillEventTable(view, date);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                String date = getFormattedDate(month, dayOfMonth, year);
                System.out.println(date);
                eventDate.setText(date);
                eventDate.setTextColor(Color.BLACK);
                fillEventTable(view, date);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calendar_action_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_addEvent) {
            Intent intent = new Intent(this.getContext(), AddEventView.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void fillEventTable(final View view, String selectedDate) {
        eventsRecyclerView = view.findViewById(R.id.eventsList);
        eventsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        eventsRecyclerView.setLayoutManager(layoutManager);
        final CalendarDao calendarDao = new CalendarDao();
        events = calendarDao.getEvents(selectedDate);
        EventsRecyclerViewAdapter eventsRecyclerViewAdapter = new EventsRecyclerViewAdapter(events, new EventsRecyclerViewAdapter.EventClickListener() {
            @Override
            public void onItemClick(Event event) {
                listOfEvents = calendarDao.getEvents();
                int eventPostion = 0;
                for(int a = 0; a < listOfEvents.size(); a ++) {
                    if(event.getEventID().equals(listOfEvents.get(a).getEventID())) {
                        eventPostion = a;
                    }
                }
                Intent intent = new Intent(view.getContext(), EventView.class);
                intent.putExtra("EventPosition", eventPostion);
                startActivity(intent);
            }
        });

        eventsRecyclerView.setAdapter(eventsRecyclerViewAdapter);
    }

    private String getFormattedDate(long longDate) {
        Date date=new Date(longDate);
        SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yy");
        return df2.format(date);
    }

    private String getFormattedDate(int month, int dayOfMonth, int year) {
        String formattedYear = Integer.toString(year).substring(2,4);
        String formattedMonth;
        if(month < 9) {
            formattedMonth = "0" + (month + 1);
        } else {
            formattedMonth = Integer.toString(month + 1);
        }
        String formattedDay;
        if(dayOfMonth < 10) {
            formattedDay = "0" + dayOfMonth;
        } else {
            formattedDay = Integer.toString(dayOfMonth);
        }
        return formattedMonth +  "/" + formattedDay + "/" + formattedYear;
    }

}

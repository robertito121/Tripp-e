package com.example.trippe.ui.calendar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.trippe.model.Event;

import com.example.trippe.R;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;
    private static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;
    private TableLayout eventTable;
    private TextView eventDate;
    private TableRow tableHeading;
    private TextView labelTime;
    private TextView labelName;
    private View view;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        eventDate = (TextView) view.findViewById(R.id.dateText);
        eventTable = (TableLayout) view.findViewById(R.id.eventTable);

        tableHeading = new TableRow(view.getContext());
        tableHeading.setId(10);
        tableHeading.setBackgroundColor(Color.GRAY);
        tableHeading.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        labelTime = new TextView(view.getContext());
        labelTime.setId(20);
        labelTime.setText("Time");
        labelTime.setTextColor(Color.WHITE);
        labelTime.setPadding(5, 5, 5, 5);
        tableHeading.addView(labelTime);// add the column to the table row here
        labelName = new TextView(view.getContext());
        labelName.setId(21);// define id that must be unique
        labelName.setText("Event"); // set the text for the header
        labelName.setTextColor(Color.WHITE); // set the color
        labelName.setPadding(5, 5, 5, 5); // set the padding (if required)
        tableHeading.addView(labelName); // add the column to the table row here
        eventTable.addView(tableHeading, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        fillEventTable(view, eventTable,mCalendarView.getDate());

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                String date = year + "/" + month + "/" + dayOfMonth;
                eventDate.setText(date);
                fillEventTable(view, eventTable,mCalendarView.getDate());
                //Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd:" + date);

                /*Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);*/
            }
        });

        return view;
    }

    public void fillEventTable(View view, TableLayout eventTable, long selectedDate) {
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(new Event(selectedDate, "9:00 am", "Go Swimming"));
        eventList.add(new Event(selectedDate, "10:00 am", "Go Hiking"));
        eventList.add(new Event(selectedDate, "11:00 am", "Lunch"));
        int count=0;
        /*while (cursor.moveToNext()) */for(int a = 0; a < eventList.size(); a++) {
            if (eventList.get(a).getDate() == selectedDate) {
                //String eventTime = cursor.getString(2);// get the first variable
                //String eventName = cursor.getDouble(4);// get the second variable
                String eventTime = eventList.get(a).getTime();
                String eventName = eventList.get(a).getTime();
// Create the table row
                TableRow tr = new TableRow(view.getContext());
                if (count % 2 != 0) tr.setBackgroundColor(Color.GRAY);
                tr.setId(100 + count);
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

//Create two columns to add as table data
                // Create a TextView to add date
                TextView labelTIME = new TextView(view.getContext());
                labelTIME.setId(200 + count);
                labelTIME.setText(eventTime);
                labelTIME.setPadding(2, 0, 5, 0);
                labelTIME.setTextColor(Color.WHITE);
                tr.addView(labelTIME);
                TextView labelNAME = new TextView(view.getContext());
                labelNAME.setId(200 + count);
                labelNAME.setText(eventName);
                labelNAME.setTextColor(Color.WHITE);
                tr.addView(labelNAME);
// finally add this to the table row
                eventTable.addView(tr, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                count++;
            }
        }
    }
}
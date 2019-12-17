package com.example.trippe.ui.calendar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.trippe.MainActivity;
import com.example.trippe.R;
import com.example.trippe.dao.CalendarDao;
import com.example.trippe.model.Event;
import com.example.trippe.model.Location;
import com.example.trippe.model.Trip;
import com.example.trippe.util.Utility;

import java.util.ArrayList;
import java.util.TimeZone;

public class AddEventView extends AppCompatActivity {

    private Spinner tripSpinner;
    private EditText eventNameField;
    private EditText eventDateField;
    private EditText eventLocationField;
    private EditText eventStartTimeField;
    private EditText eventEndTimeField;
    private Button addNewEvent;
    private Button cancelButton;
    private CheckBox allDayCheckBox;
    private SQLiteDatabase db;
    private Activity activity_add_event = this;


    public AddEventView() {
        db = getConnection();
    }

    private SQLiteDatabase getConnection() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);
        return db;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
        setTitle("Add Event");

        tripSpinner = (Spinner) findViewById(R.id.newEventTripDropDown);
        ArrayAdapter<String> tripsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getTripNames());
        tripsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripSpinner.setAdapter(tripsAdapter);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.addNewEvent);

        cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NavController navController = Navigation.findNavController(activity_add_event, R.id.nav_host_fragment);
                    navController.navigate(R.id.navigation_dashboard);

                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        addNewEvent = (Button) findViewById(R.id.addEventButton);
        eventNameField = (EditText) findViewById(R.id.newEventNameTxtField);
        eventDateField = (EditText) findViewById(R.id.newEventDateTxtField);
        eventStartTimeField = (EditText) findViewById(R.id.newEventStartTimeField);
        eventEndTimeField = (EditText) findViewById(R.id.newEventEndTimeField);
        eventLocationField = (EditText) findViewById(R.id.newEventLocationTxtField);
        allDayCheckBox = (CheckBox) findViewById(R.id.allDayEventCheckBox);

        addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CalendarDao calendarDao = new CalendarDao();
                    String eventID = Integer.toString(Utility.generateEventId() + calendarDao.getEvents().size());
                    System.out.println(eventID);
                    String eventName = eventNameField.getText().toString();
                    String eventDate = eventDateField.getText().toString();
                    String eventStartTime = eventStartTimeField.getText().toString();
                    String eventEndTime = eventEndTimeField.getText().toString();
                    String eventLocation = eventLocationField.getText().toString();
                    String eventTrip = tripSpinner.getSelectedItem().toString();
                    Event newEvent = new Event(eventID, eventDate, eventStartTime, eventEndTime, eventName, eventLocation, eventTrip);
                    boolean isAddedToDatabase = calendarDao.addEvent(newEvent);
                    if (isAddedToDatabase == true) {
                        System.out.println("Event: " + eventName);
                        NavController navController = Navigation.findNavController(activity_add_event, R.id.nav_host_fragment);
                        navController.navigate(R.id.navigation_dashboard);
                    }
                }
                catch (NullPointerException e) {
                    e.printStackTrace();

                    if (tripSpinner == null) {
                        TextView spinnerError = (TextView) tripSpinner.getSelectedView();
                        spinnerError.setError("Please select a Trip");
                    }
                    if (eventNameField.getText().toString().isEmpty()) {
                        eventNameField.setError("The name of your event is required");
                    }
                    else {
                        eventNameField.setError(null);
                    }

                    if (eventDateField.getText().toString().isEmpty()) {
                        eventDateField.setError("The date of your event is required");
                    }
                    else {
                        eventDateField.setError(null);
                    }
                    if (eventStartTimeField.getText().toString().isEmpty()) {
                        eventStartTimeField.setError("The start time of your event is required");
                    }
                    else {
                        eventStartTimeField.setError(null);
                    }
                    if (eventEndTimeField.getText().toString().isEmpty()) {
                        eventEndTimeField.setError("The end time of your event is required");
                    }
                    else {
                        eventEndTimeField.setError(null);
                    }
                    if (eventLocationField.getText().toString().isEmpty()) {
                        eventLocationField.setError("The location of your event is required");
                    }
                    else {
                        eventLocationField.setError(null);
                    }
                }
            }
        });
    }

    public void onCheckboxClicked(View view) {
        boolean allDayChecked = ((CheckBox) view).isChecked();
        eventStartTimeField = (EditText) findViewById(R.id.newEventStartTimeField);
        eventEndTimeField = (EditText) findViewById(R.id.newEventEndTimeField);
        if(allDayChecked) {
            eventStartTimeField.setText("12:00 AM");
            eventEndTimeField.setText("11:59 PM");
        } else {
            eventStartTimeField.setText("");
            eventEndTimeField.setText("");
        }
    }

    public ArrayList<String> getTripNames() {
        ArrayList<Trip> trips = new ArrayList<>();
        ArrayList<String> tripNames = new ArrayList<>();
        tripNames.add("Select a Trip");

        try {
            Cursor cursor = db.rawQuery("select * from Trips", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String tripId = cursor.getString(cursor.getColumnIndex("tripId"));
                int tripFlagIndicator = cursor.getInt(cursor.getColumnIndex("tripFlagIndicator"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                String city = cursor.getString(cursor.getColumnIndex("destinationCity"));
                String state = cursor.getString(cursor.getColumnIndex("destinationState"));
                int zipCode = cursor.getInt(cursor.getColumnIndex("destinationZipCode"));
                String country = cursor.getString(cursor.getColumnIndex("destinationCountry"));
                int milesAwayFromHome = cursor.getInt(cursor.getColumnIndex("milesAwayFromHome"));
                TimeZone timeZone = TimeZone.getTimeZone(cursor.getString(cursor.getColumnIndex("timeZone")));
                Location location = new Location(city, state,zipCode,country);
                String currency = cursor.getString(cursor.getColumnIndex("currency"));
                String languagesString = cursor.getString(cursor.getColumnIndex("languages"));
                String[] languages = languagesString.split(",");
                Trip trip = new Trip(tripId, tripFlagIndicator, startDate, endDate, location, milesAwayFromHome,timeZone,currency,languages);
                trips.add(trip);
                cursor.moveToNext();
            }

            for(int a = 0; a < trips.size(); a++) {
                if(trips.get(a).getTripId().contains("NT")) {
                    tripNames.add(trips.get(a).nationalTripToString());
                } else {
                    tripNames.add(trips.get(a).internationalTripToString());
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("Database Reading Error", e.toString());
        }
        finally {
           // db.close();
        }

        return tripNames;
    }
}

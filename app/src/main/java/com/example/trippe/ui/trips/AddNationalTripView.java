package com.example.trippe.ui.trips;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trippe.MainActivity;
import com.example.trippe.R;
import com.example.trippe.dao.CalendarDao;
import com.example.trippe.dao.TripDao;
import com.example.trippe.model.Event;
import com.example.trippe.model.Location;
import com.example.trippe.model.Trip;
import com.example.trippe.util.Utility;

import java.util.Calendar;
import java.util.TimeZone;

public class AddNationalTripView extends AppCompatActivity {

    private Spinner stateSpinner;
    private EditText cityTextField;
    private EditText startDateTextField;
    private EditText endDateTextField;
    private CalendarDao calendarDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_national_trip);
        setTitle("Add National Trip");
        stateSpinner = findViewById(R.id.stateDropDownSpinner);
        cityTextField = findViewById(R.id.nationalCityTextField);
        startDateTextField = findViewById(R.id.nationalStartDateTextField);
        endDateTextField = findViewById(R.id.nationalEndDateTextField);
        startDateTextField.setShowSoftInputOnFocus(false);
        endDateTextField.setShowSoftInputOnFocus(false);
    }

    //TODO: make method support national trip from other countries dynamically right now is set up only for US
    //TODO: add checker for dates to make sure they are correct timeframe wise
    //TODO: implement city spinner depending on the State chosen
    public void addNewNationalTrip(View view) {
        if (view.getId() == R.id.nationalAddTripBtn) {
            String tripId = Utility.generateTripId("NT");
            String city = cityTextField.getText().toString();
            String state = stateSpinner.getSelectedItemPosition() > 0 ? stateSpinner.getSelectedItem().toString() : null;
            String startDate = startDateTextField.getText().toString();
            String endDate = endDateTextField.getText().toString();

            if (city.isEmpty() || state == null || startDate.isEmpty() || endDate.isEmpty()) {
                if (city.isEmpty()) {
                    cityTextField.setError("The city of your trip is required");
                }
                if (state == null) {
                    TextView spinnerError = (TextView) stateSpinner.getSelectedView();
                    spinnerError.setError("Please select a State for your Trip");
                }
                if (startDate.isEmpty()) {
                    startDateTextField.setError("The start date of your trip is required");
                }
                if (endDate.isEmpty()) {
                    endDateTextField.setError("The end date of your trip is required");
                }
            }
            else {
                Location nationalLocation = new Location(city, state, "united states");
                String[] languages = {"English", "Spanish"};
                Trip newTrip = new Trip(tripId,startDate, endDate, nationalLocation, 0, TimeZone.getTimeZone("EST"), "USD", languages);
                TripDao tripDao = new TripDao();
                //addTripEvents(startDate, endDate, newTrip);
                boolean isAddedToDatabase =  tripDao.addTrip(newTrip);
                if (isAddedToDatabase == true) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void showDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int month = calendar.get(calendar.MONTH);
        int year = calendar.get(calendar.YEAR);
        final int textFieldId = view.getId();

        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (textFieldId == R.id.nationalStartDateTextField) {
                    startDateTextField.setText((month + 1) + "/" + (dayOfMonth) + "/" + year);
                    startDateTextField.setError(null);
                }
                else {
                    endDateTextField.setText((month + 1) + "/" + (dayOfMonth) + "/" + year);
                    endDateTextField.setError(null);
                }

            }
        }, year, month, day);
        datePicker.show();
    }

    private void addTripEvents(String startDate, String endDate, Trip trip) {
        String date = startDate;
        do {
            String eventID = Integer.toString(Utility.generateEventId() + calendarDao.getEvents().size());
            calendarDao.addEvent(new Event(eventID, date, "12:00 AM", "11:59 PM",
                    trip.internationalTripToString() + " Trip", trip.internationalTripToString(), trip.internationalTripToString()));
            calendarDao.nextDay(date);
        } while(date != endDate);
    }
}

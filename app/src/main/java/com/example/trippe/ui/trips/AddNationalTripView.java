package com.example.trippe.ui.trips;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trippe.MainActivity;
import com.example.trippe.R;
import com.example.trippe.dao.TripDao;
import com.example.trippe.model.Location;
import com.example.trippe.model.NationalTrip;
import com.example.trippe.util.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddNationalTripView extends AppCompatActivity {

    private Spinner stateSpinner;
    private EditText cityTextField;
    private EditText startDateTextField;
    private EditText endDateTextField;
    private EditText zipcodeTextField;

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
        zipcodeTextField = findViewById(R.id.nationalZipCodeField);


    }

    public void addNewNationalTrip(View view) {
        if (view.getId() == R.id.nationalAddTripBtn) {
            String tripId = Utility.generateTripId("NT");
            int tripFlagIndicator = R.drawable.usd;
            String city = cityTextField.getText().toString();
            if (city.isEmpty()) {
                cityTextField.setError("The city of your trip is required");
            }
            String state = stateSpinner.getSelectedItemPosition() > 0 ? stateSpinner.getSelectedItem().toString() : null;

            //TODO: Implement an API to be able to pull zipcode automatically fromm the user given an address
            int zipCode = Integer.parseInt(zipcodeTextField.getText().toString());
            if (zipcodeTextField.getText().toString().isEmpty()) {
                zipcodeTextField.setError("The zip code of your trip is required");
            }

            Location nationalLocation = new Location(city, state,zipCode, "USA");
            String startDate = null;
            String endDate = null;
            try {
                startDate = startDateTextField.getText().toString();
                endDate = endDateTextField.getText().toString();
                NationalTrip newTrip = new NationalTrip(tripId, tripFlagIndicator,startDate, endDate, nationalLocation, 0, TimeZone.getTimeZone("EST"));
                TripDao tripDao = new TripDao();
                boolean isAddedToDatabase =  tripDao.addTrip(newTrip);
                if (isAddedToDatabase == true) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            }
            catch (NullPointerException e) {
                e.printStackTrace();

                if (state == null) {
                    TextView spinnerError = (TextView) stateSpinner.getSelectedView();
                    spinnerError.setError("Please select a State for your Trip");
                }
                if (startDateTextField.getText().toString().isEmpty()) {
                    startDateTextField.setError("The start date of your trip is required");
                    System.out.println("hit startDate");
                }
                else {
                    startDateTextField.setError(null);
                }

                if (endDateTextField.getText().toString().isEmpty()) {
                    endDateTextField.setError("The end date of your trip is required");
                    System.out.println("hit endDate");
                }
                else {
                    endDateTextField.setError(null);
                }
            }
        }
    }

    public void showDatePicker(View view) {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int month = calendar.get(calendar.MONTH);
        int year = calendar.get(calendar.YEAR);
        int textFieldId = view.getId();

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
}

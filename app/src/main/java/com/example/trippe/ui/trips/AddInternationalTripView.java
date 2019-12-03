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
import com.example.trippe.dao.TripDao;
import com.example.trippe.model.Location;
import com.example.trippe.model.Trip;
import com.example.trippe.util.Utility;

import java.util.Calendar;
import java.util.TimeZone;

public class AddInternationalTripView extends AppCompatActivity {

    private EditText cityTextField;
    private EditText startDateTextField;
    private EditText endDateTextField;
    private Spinner countrySpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_international_trip);
        setTitle("Add International Trip");
        countrySpinner = findViewById(R.id.countryDropDownSpinner);
        cityTextField = findViewById(R.id.internationalCityTextField);
        startDateTextField = findViewById(R.id.internationalStartDateTextField);
        endDateTextField = findViewById(R.id.internationalEndDateTextField);
        startDateTextField.setShowSoftInputOnFocus(false);
        endDateTextField.setShowSoftInputOnFocus(false);
    }

    public void addNewInternationalTrip(View view) {
        if (view.getId() == R.id.internationalAddTripBtn) {
            String tripId = Utility.generateTripId("IT");
            String country = countrySpinner.getSelectedItemPosition() > 0 ? countrySpinner.getSelectedItem().toString() : null;
            String city = cityTextField.getText().toString();
            String startDate = startDateTextField.getText().toString();
            String endDate = endDateTextField.getText().toString();

            if (country == null || city.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                if (country == null) {
                    TextView spinnerError = (TextView) countrySpinner.getSelectedView();
                    spinnerError.setError("Please select a State for your Trip");
                }
                if (city.isEmpty()) {
                    cityTextField.setError("The city of your trip is required");
                }
                if (startDate.isEmpty()) {
                    startDateTextField.setError("The start date of your trip is required");
                }
                if (endDate.isEmpty()) {
                    endDateTextField.setError("The end date of your trip is required");
                }
            }
            else {
                Location internationalLocation = new Location(city, country);
                String[] languages = {"English", "Spanish"};
                Trip newTrip = new Trip(tripId, startDate, endDate, internationalLocation, 0, TimeZone.getTimeZone("EST"), "USD", languages);
                TripDao tripDao = new TripDao();
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
                if (textFieldId == R.id.internationalStartDateTextField) {
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

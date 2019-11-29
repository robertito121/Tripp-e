package com.example.trippe.ui.trips;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trippe.R;
import com.example.trippe.util.Utility;

public class AddInternationalTrip extends AppCompatActivity {

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
            if(view.getId() == R.id.internationalAddTripBtn) {
                String tripId = Utility.generateTripId("IT");
                String city = cityTextField.getText().toString();
                int tripFlagIndicator = Utility.getResourceIndicatorByString(city, R.drawable.class);


            }
    }
}

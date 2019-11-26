package com.example.trippe.ui.trips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trippe.R;

public class AddTripView extends AppCompatActivity {

    private boolean isNationalTripRadioButtonChecked = false;
    private boolean isInternationalTripRadioButtonChecked = false;
    private RadioButton nationalRadioButton;
    private RadioButton internationalRadioButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setTitle("Add Trip");
        nationalRadioButton = (RadioButton) findViewById(R.id.nationalRadioButton);
        internationalRadioButton =  (RadioButton) findViewById(R.id.internationalRadioButton);

    }

    public void displayAddTripForm(View view) {

        Intent intent = null;

        if (isNationalTripRadioButtonChecked) {
            intent = new Intent(this, AddNationalTripView.class );
        }
        else {
            intent = new Intent(this, AddInternationalTrip.class);
        }
        startActivity(intent);
    }

    public void nationalRadioButtonOnClick(View view) {
        isNationalTripRadioButtonChecked = ((RadioButton) view).isChecked();
        internationalRadioButton.setChecked(false);
        isInternationalTripRadioButtonChecked = false;

    }

    public void internationalRadioButtonOnClick(View view) {
        isInternationalTripRadioButtonChecked = ((RadioButton) view).isChecked();
        nationalRadioButton.setChecked(false);
        isNationalTripRadioButtonChecked = false;
    }
}

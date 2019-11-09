package com.example.trippe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        // create dropdown for source currency selection
        Spinner dropFromCurrency = (Spinner) findViewById(R.id.dropFromCurrency);

        // arrayadaptor is used to add to the spinner dropdown
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter
                .createFromResource(this, R.array.currency_names,
                        android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //currencyAdapter
        //       .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //dropFromCurrency.setAdapter(currencyAdapter);

        // Apply the adapter to the spinner
        //currencyAdapter.setAutofillOptions(currencyAdapter);
    }
}

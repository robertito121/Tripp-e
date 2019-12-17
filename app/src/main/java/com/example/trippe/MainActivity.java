package com.example.trippe;

import android.content.ClipData;
import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.database.sqlite.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO Remove this and implement a threaded query for web stuff
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // No permissions lets request them
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    this.PERMISSION_REQUEST_CODE);
            Log.i("MainActivity", "Got INTERNET Permissions");
        } else {
            Log.i("MainActivity", "Already have INTERNET Permissions");
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            // No permissions lets request them
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    this.PERMISSION_REQUEST_CODE);
            Log.i("MainActivity", "Got ACCESS_NETWORK_STATE Permissions");
        } else {
            Log.i("MainActivity", "Already have ACCESS_NETWORK_STATE Permissions");
        }

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

        //creating database when application starts
        SQLiteDatabase trippeDatabase = null;

        try {
            trippeDatabase = openOrCreateDatabase("TrippeDatabase", MODE_PRIVATE, null);
            //trippeDatabase.execSQL("DROP TABLE IF EXISTS Trips");
            trippeDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "Trips (" +
                    "tripId VARCHAR(255) NOT NULL, " +
                    "tripFlagIndicator INT(255) NOT NULL, " +
                    "destinationCity VARCHAR(255) NOT NULL, " +
                    "destinationState VARCHAR(255), " +
                    "destinationZipCode INT(255), " +
                    "destinationCountry VARCHAR(255) NOT NULL, " +
                    "startDate VARCHAR(255) NOT NULL, " +
                    "endDate VARCHAR(255) NOT NULL, " +
                    "milesAwayFromHome INT NOT NULL, " +
                    "timeZone VARCHAR(255) NOT NULL, " +
                    "currency VARCHAR(255) NOT NULL, " +
                    "languages VARCHAR(255) NOT NULL, " +
                    "PRIMARY KEY (tripId));");
            trippeDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "Events (" +
                    "eventId VARCHAR(255) NOT NULL, " +
                    "eventDate VARCHAR(255) NOT NULL, " +
                    "eventStartTime VARCHAR(255) NOT NULL, " +
                    "eventEndTime VARCHAR(255) NOT NULL, " +
                    "eventName VARCHAR(255) NOT NULL, " +
                    "eventLocation VARCHAR(255) NOT NULL, " +
                    "tripId VARCHAR(255) NOT NULL, " +
                    "PRIMARY KEY (eventId));");
        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        } finally {
            trippeDatabase.close();
        }
    }
}

package com.example.trippe.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.trippe.model.Location;
import com.example.trippe.model.Trip;
import java.sql.Date;
import java.util.ArrayList;
import java.util.TimeZone;


public class TripDao {

    private SQLiteDatabase db;

    public TripDao() {
        db = getConnection();
    }

    private SQLiteDatabase getConnection() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);
        return db;
    }

    public boolean addTrip(Trip newTrip) {
        try {
            String tripId = newTrip.getTripId();
            int tripFlagIndicator = newTrip.getTripFlagIndicator();
            String startDate = newTrip.getStartDate();
            String endDate = newTrip.getEndDate();
            String city = newTrip.getDestination().getCity();
            String state = newTrip.getDestination().getState();
            int zipCode = newTrip.getDestination().getZipCode();
            String country = newTrip.getDestination().getCountry();
            int milesAwayFromHome = newTrip.getMilesAwayFromHome();
            String timeZone = newTrip.getTimeZone().getID();
            ContentValues contentValues = new ContentValues();
            contentValues.put("tripId", tripId);
            contentValues.put("tripFlagIndicator", tripFlagIndicator);
            contentValues.put("startDate", startDate.toString());
            contentValues.put("endDate", endDate.toString());
            contentValues.put("destinationCity", city);
            contentValues.put("destinationState", state);
            contentValues.put("destinationCountry", country);
            contentValues.put("destinationZipCode", zipCode);
            contentValues.put("milesAwayFromHome", milesAwayFromHome);
            contentValues.put("timeZone", timeZone);
            db.insert("Trips", null, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Database Writing Error", e.toString());
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<Trip> getTrips() {

        ArrayList<Trip> trips = new ArrayList<>();

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
                Trip trip = new Trip(tripId, tripFlagIndicator, startDate, endDate, location, milesAwayFromHome,timeZone);
                trips.add(trip);
                cursor.moveToNext();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("Database Reading Error", e.toString());
        }
        finally {
            db.close();
        }
        return trips;
    }
}

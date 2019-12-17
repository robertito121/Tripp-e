package com.example.trippe.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.trippe.model.Location;
import com.example.trippe.model.Trip;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;


public class TripDao {

    private SQLiteDatabase db;

    public TripDao() {
    }

    private SQLiteDatabase getConnection() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);
        return db;
    }

    public boolean addTrip(Trip newTrip) throws SQLiteConstraintException {
        try {
            db = getConnection();
            String tripId = newTrip.getTripId();
            String startDate = newTrip.getStartDate();
            String endDate = newTrip.getEndDate();
            String city = newTrip.getDestination().getCity();
            String state = newTrip.getDestination().getState();
            String country = newTrip.getDestination().getCountry();
            int milesAwayFromHome = newTrip.getMilesAwayFromHome();
            String timeZone = newTrip.getTimeZone().getID();
            String currency = newTrip.getCurrency();
            String languages = newTrip.foreignLanguagesToString();
            ContentValues contentValues = new ContentValues();
            contentValues.put("tripId", tripId);
            contentValues.put("startDate", startDate);
            contentValues.put("endDate", endDate);
            contentValues.put("destinationCity", city);
            contentValues.put("destinationState", state);
            contentValues.put("destinationCountry", country);
            contentValues.put("milesAwayFromHome", milesAwayFromHome);
            contentValues.put("timeZone", timeZone);
            contentValues.put("currency", currency);
            contentValues.put("languages", languages);
            db.insert("Trips", null, contentValues);
            return true;
        }
        catch (SQLiteException e) {
            e.printStackTrace();
            Log.d("Database Writing Error", e.toString());
            return false;

        }
        finally {
            db.close();
        }
    }

    public ArrayList<Trip> getTrips() {

        ArrayList<Trip> trips = new ArrayList<>();

        try {
            db = getConnection();
            Cursor cursor = db.rawQuery("select * from Trips", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String tripId = cursor.getString(cursor.getColumnIndex("tripId"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                String city = cursor.getString(cursor.getColumnIndex("destinationCity"));
                String state = cursor.getString(cursor.getColumnIndex("destinationState"));
                String country = cursor.getString(cursor.getColumnIndex("destinationCountry"));
                int milesAwayFromHome = cursor.getInt(cursor.getColumnIndex("milesAwayFromHome"));
                TimeZone timeZone = TimeZone.getTimeZone(cursor.getString(cursor.getColumnIndex("timeZone")));
                Location location = new Location(city, state,country);
                String currency = cursor.getString(cursor.getColumnIndex("currency"));
                String languagesString = cursor.getString(cursor.getColumnIndex("languages"));
                String[] languages = languagesString.split(",");
                Trip trip = new Trip(tripId, startDate, endDate, location, milesAwayFromHome,timeZone,currency,languages);
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

    public boolean isTripIdExistent(String tripId) {
        boolean tripIdExists = true;
        try {
            db = getConnection();
            Cursor cursor =  db.rawQuery("select * from Trips where tripId = \"" + tripId + "\"", null);
            if (cursor.moveToFirst() == false) {
                tripIdExists = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("Database Reading Error", e.toString());
        }
        finally {
            db.close();
        }
        return tripIdExists;
    }

    public boolean removeTrip(Trip tripToBeRemoved) {
        boolean isRemoved = false;
        try {
            db = getConnection();
            int success =  db.delete("Trips", "tripId=?", new String[] {tripToBeRemoved.getTripId()});
            if (success == 1) {
                isRemoved = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
        return isRemoved;


    }
}

package com.example.trippe.util;

import android.util.Log;

import com.example.trippe.dao.TripDao;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Utility {


    /**
     * generates a random Trip Id using a given prefix pattern (either NT for nationalTrip or
     * IT for internationalTrip)
     * @param prefixPattern
     * @return the tripId as String
     */
    public static String generateTripId(String prefixPattern) {

        boolean tripIdExistent;
        String tripId;
        do {
            Integer randomInteger = ThreadLocalRandom.current().nextInt();
            if (randomInteger.toString().contains("-")) {
                tripId = prefixPattern + randomInteger;
            }
            else {
                tripId = prefixPattern + "-" + randomInteger;
            }
            //checking whether tripId is already in use otherwise generate another tripId
            TripDao tripDao = new TripDao();
            tripIdExistent = tripDao.isTripIdExistent(tripId);

        }
        while (tripIdExistent == true);

        return tripId;
    }



    /**
     * this method is used to get any resource indicator using a string
     * in order to facilitate the pulling of resources into multiple classes
     * @param resourceName
     * @param anyClass
     * @return the resource id as int
     */
    public static int getResourceIndicatorByString(String resourceName, Class<?> anyClass) {
        try {
            Field idField = anyClass.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            Log.e("setFlag", e.toString(), e);
            return -1;
        }
    }


    /**
     * this method is used to get the current date as a string in the format yyyy-MM-dd
     * in order to interface with the database or CurrencyDao and/or WebAPIRequest classes
     * @return today's date as a String
     */
    public static String getTodaysDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Long now = System.currentTimeMillis(); // current time in ms
        String strNow = "";
        try {
            strNow = formatter.format(new Date(now));
        } catch (Exception e) {
            Log.e("getTodaysDate", e.toString(), e);
        }
        return strNow;
    }


    /**
     * this method is used to get the current date 'daysAgo' number of days ago as a
     * string in the format yyyy-MM-dd
     * in order to interface with the database or CurrencyDao and/or WebAPIRequest classes
     * @param daysAgo
     * @return date 'daysAgo' as a String
     */
    public static String getDateAgo(int daysAgo){
        long DAY_IN_MILLIS = 86400000; // one day in milliseconds
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Long now = System.currentTimeMillis(); // current time in ms
        long ago = now - daysAgo * DAY_IN_MILLIS; // calculate date 10 days ago
        String strAgo = "";
        try {
            strAgo = formatter.format(new Date(ago));
        } catch (Exception e) {
            Log.e("getDateAgo", e.toString(), e);
        }
        return strAgo;
    }
}

package com.example.trippe.util;

import android.util.Log;

import com.example.trippe.dao.TripDao;

import java.lang.reflect.Field;
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
    //TODO: need to implement another helper method that would change the resource name from a string that contains spaces to a string that has underscores in ordere to accomodate country names that are two words.
    public static int getResourceIndicatorByString(String resourceName, Class<?> anyClass) {
        try {
            Field idField = anyClass.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            Log.e("setFlag", e.toString(), e);
            return -1;
        }
    }
}

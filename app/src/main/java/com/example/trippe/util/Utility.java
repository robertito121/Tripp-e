package com.example.trippe.util;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Utility {


    public static String generateTripId(String prefixPattern) {

        String tripId;
        Integer randomInteger = ThreadLocalRandom.current().nextInt();
        if (randomInteger.toString().contains("-")) {
            tripId = prefixPattern + randomInteger;
        }
        else {
            tripId = prefixPattern + "-" + randomInteger;
        }
        return tripId;
    }
}

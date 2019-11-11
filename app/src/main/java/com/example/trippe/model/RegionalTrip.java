package com.example.trippe.model;

import java.util.Date;
import java.util.TimeZone;

public class RegionalTrip extends Trip {

    private Address destinationAddress;

    public RegionalTrip(String tripId, Date startDate, Date endDate, int milesAwayFromHome, TimeZone timeZone, Address destinationAddress) {
        super(tripId, startDate, endDate, milesAwayFromHome, timeZone);
        this.setDestinationAddress(destinationAddress);
    }


    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
    }
}

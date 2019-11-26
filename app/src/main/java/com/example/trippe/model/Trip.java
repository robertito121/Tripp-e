package com.example.trippe.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Trip {

    private String tripId;
    private int tripFlagIndicator;
    private String startDate;
    private String endDate;
    protected Location destination;
    private int milesAwayFromHome;
    private TimeZone timeZone;
    private String currency;
    private String[] languages;


    public Trip(String tripId, int tripFlagIndicator, String startDate, String endDate, Location destination, int milesAwayFromHome, TimeZone timeZone, String currency, String[] languages) {
        this.tripId = tripId;
        this.tripFlagIndicator = tripFlagIndicator;
        this.startDate = startDate;
        this.endDate = endDate;
        this.destination = destination;
        this.milesAwayFromHome = milesAwayFromHome;
        this.timeZone = timeZone;
        this.currency = currency;
        this.languages = languages;
    }

    public String nationalTripToString() {
        return destination.getCity() + ", " + destination.getState();
    }

    public String internationalTripToString() {
        return destination.getCity() + "," + destination.getCountry();
    }

    public String foreignLanguagesToString() {
        StringBuilder strBuider = new StringBuilder();
        for (String language : languages) {
            strBuider.append(language + ", ");
        }
        return strBuider.toString();
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getMilesAwayFromHome() {
        return milesAwayFromHome;
    }

    public void setMilesAwayFromHome(int milesAwayFromHome) {
        this.milesAwayFromHome = milesAwayFromHome;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public int getTripFlagIndicator() {
        return tripFlagIndicator;
    }

    public void setTripFlagIndicator(int tripFlagIndicator) {
        this.tripFlagIndicator = tripFlagIndicator;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }
}

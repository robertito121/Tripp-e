package com.example.trippe.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class InternationalTrip extends Trip {

    private String currency;
    private ArrayList<String> foreignLanguages;

    public InternationalTrip(String tripId, int tripFlagIndicator, String startDate, String endDate, Location destination, int milesAwayFromHome, TimeZone timeZone, String currency, ArrayList<String> foreignLanguages) {
        super(tripId, tripFlagIndicator,startDate, endDate, destination, milesAwayFromHome, timeZone);
        this.currency = currency;
        this.foreignLanguages = foreignLanguages;
    }

    @NonNull
    @Override
    public String toString() {
        return destination.getCity() + destination.getCountry();
    }

    public String foreignLanguagesToString() {
        StringBuilder strBuider = new StringBuilder();
        for (String language : foreignLanguages) {
            strBuider.append(language + ", ");
        }
        return strBuider.toString();
    }

    public ArrayList<String> getForeignLanguages() {
        return foreignLanguages;
    }

    public void setForeignLanguages(ArrayList<String> foreignLanguages) {
        this.foreignLanguages = foreignLanguages;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

package com.example.trippe.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class InternationalTrip extends Trip {

    private Address destinationCityAndCountry;
    private ArrayList<String> foreignCurrencies;
    private ArrayList<String> foreignLanguages;

    public InternationalTrip(String tripId, Date startDate, Date endDate, int milesAwayFromHome, TimeZone timeZone, Address destinationCityAndCountry, ArrayList<String> foreignCurrencies, ArrayList<String> foreignLanguages) {
        super(tripId, startDate, endDate, milesAwayFromHome, timeZone);
        this.setDestinationCityAndCountry(destinationCityAndCountry);
        this.setForeignCurrencies(foreignCurrencies);
        this.setForeignLanguages(foreignLanguages);
    }


    public Address getDestinationCityAndCountry() {
        return destinationCityAndCountry;
    }

    public void setDestinationCityAndCountry(Address destinationCityAndCountry) {
        this.destinationCityAndCountry = destinationCityAndCountry;
    }

    public ArrayList<String> getForeignCurrencies() {
        return foreignCurrencies;
    }

    public void setForeignCurrencies(ArrayList<String> foreignCurrencies) {
        this.foreignCurrencies = foreignCurrencies;
    }

    public ArrayList<String> getForeignLanguages() {
        return foreignLanguages;
    }

    public void setForeignLanguages(ArrayList<String> foreignLanguages) {
        this.foreignLanguages = foreignLanguages;
    }
}

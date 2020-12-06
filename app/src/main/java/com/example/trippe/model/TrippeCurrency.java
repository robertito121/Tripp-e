package com.example.trippe.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TrippeCurrency {
    private Map<Date, Double> rates;
    private long flagResourceid;
    private String abbreviation;
    private String name;
    private String country;

    public TrippeCurrency() {
        this.rates = new HashMap();
    }

    public TrippeCurrency(String abbreviation ) {
        this.abbreviation = abbreviation;
        this.rates = new HashMap();

    }

    public TrippeCurrency(String abbreviation, String name) {
        this.abbreviation = abbreviation;
        this.name = name;
        this.rates = new HashMap();

    }

    public String getCurrencyName() {
        return this.name;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    public String getCountry() {
        return this.country;
    }

    public Iterator<Date> keys() {
        return this.rates.keySet().iterator();
    }

    public double getRate(Date date) {
        double rate = 0;
        if (rates.containsKey(date)) {
            return rates.get(date);
        }
        return rate;
    }

    public Integer getSize() {
        return this.rates.size();
    }

    public long getFlagResourceId(){
        return this.flagResourceid;
    }

    public void addRate(Date date, Double rate) {
        this.rates.put(date, rate);
    }

    public Map<Date, Double> getRates() {
        return rates;
    }

    public void setAbbreviation(String abbrev){
        this.abbreviation = abbrev;
    }


}

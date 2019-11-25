package com.example.trippe.model;

public class Location {

    private String city;
    private String state;
    private String country;
    private int zipCode;

    public Location(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public Location(String city, String state, int zipCode, String country) {
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}

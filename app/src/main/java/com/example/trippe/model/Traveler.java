package com.example.trippe.model;

import java.util.Set;

public class Traveler {

    private String firstName;
    private String lastName;
    private int age;
    private Location location;
    private Set<NationalTrip> nationalTripsTraveled;
    private Set<InternationalTrip> internationalTripsTraveled;

    public Traveler(String firstName, String lastName, int age, Location location, Set<NationalTrip> nationalTripsTraveled, Set<InternationalTrip> internationalTripsTraveled) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.setLocation(location);
        this.setNationalTripsTraveled(nationalTripsTraveled);
        this.setInternationalTripsTraveled(internationalTripsTraveled);
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<NationalTrip> getNationalTripsTraveled() {
        return nationalTripsTraveled;
    }

    public void setNationalTripsTraveled(Set<NationalTrip> nationalTripsTraveled) {
        this.nationalTripsTraveled = nationalTripsTraveled;
    }

    public Set<InternationalTrip> getInternationalTripsTraveled() {
        return internationalTripsTraveled;
    }

    public void setInternationalTripsTraveled(Set<InternationalTrip> internationalTripsTraveled) {
        this.internationalTripsTraveled = internationalTripsTraveled;
    }
}

package com.example.trippe.model;

import java.util.Set;

public class Traveler {

    private String firstName;
    private String lastName;
    private int age;
    private Address address;
    private Set<RegionalTrip> regionalTripsTraveled;
    private Set<InternationalTrip> internationalTripsTraveled;

    public Traveler(String firstName, String lastName, int age, Address address, Set<RegionalTrip> regionalTripsTraveled, Set<InternationalTrip> internationalTripsTraveled) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.setAddress(address);
        this.setRegionalTripsTraveled(regionalTripsTraveled);
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<RegionalTrip> getRegionalTripsTraveled() {
        return regionalTripsTraveled;
    }

    public void setRegionalTripsTraveled(Set<RegionalTrip> regionalTripsTraveled) {
        this.regionalTripsTraveled = regionalTripsTraveled;
    }

    public Set<InternationalTrip> getInternationalTripsTraveled() {
        return internationalTripsTraveled;
    }

    public void setInternationalTripsTraveled(Set<InternationalTrip> internationalTripsTraveled) {
        this.internationalTripsTraveled = internationalTripsTraveled;
    }
}

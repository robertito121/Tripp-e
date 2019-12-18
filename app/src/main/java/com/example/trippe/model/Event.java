package com.example.trippe.model;

public class Event {
    String eventID;
    String date;
    String startTime;
    String endTime;
    String name;
    String location;
    String tripID;

    public Event(String eventID, String date, String startTime, String endTime, String name, String location, String tripID) {
        this.eventID = eventID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.location = location;
        this.tripID = tripID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(Trip trip) {
        this.tripID = trip.getTripId();
    }

    public String getTime () {
        if (startTime.equals("12:00 AM") && endTime.equals("11:59 PM")) {
            return "All Day";
        } else {
            return startTime + " - " + endTime;
        }
    }
}

package com.example.trippe.model;

import java.util.Date;
import java.util.TimeZone;

public class Trip {

    private String tripId;
    private Date startDate;
    private Date endDate;
    private int milesAwayFromHome;
    private TimeZone timeZone;

    public Trip(String tripId, Date startDate, Date endDate, int milesAwayFromHome, TimeZone timeZone) {
        this.tripId = tripId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.milesAwayFromHome = milesAwayFromHome;
        this.timeZone = timeZone;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
}

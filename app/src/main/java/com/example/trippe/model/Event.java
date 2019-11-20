package com.example.trippe.model;

public class Event {
    long date;
    String time;
    String name;
    //Trip trip;

    public Event(long date, String time, String name) {
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}

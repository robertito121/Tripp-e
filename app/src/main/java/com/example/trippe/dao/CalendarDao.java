package com.example.trippe.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.trippe.model.Event;

import java.util.ArrayList;

public class CalendarDao {

    private SQLiteDatabase database;

    public CalendarDao() {
        database = getConnection();
    }

    private SQLiteDatabase getConnection() {
        SQLiteDatabase database = SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);
        return database;
    }

    public boolean addEvent(Event newEvent) {
        try {
            String eventId = newEvent.getEventID();
            String eventDate = newEvent.getDate();
            String eventStartTime = newEvent.getStartTime();
            String eventEndTime = newEvent.getEndTime();
            String eventName = newEvent.getName();
            String eventLocation = newEvent.getLocation();
            String tripId = newEvent.getTripID();

            ContentValues contentValues = new ContentValues();
            contentValues.put("eventId", eventId);
            contentValues.put("eventDate", eventDate);
            contentValues.put("eventStartTime", eventStartTime);
            contentValues.put("eventEndTime", eventEndTime);
            contentValues.put("eventName", eventName);
            contentValues.put("eventLocation", eventLocation);
            contentValues.put("tripId", tripId);
            database.insert("Events", null, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Database Writing Error", e.toString());
            return false;
        } finally {
            //database.close();
        }
    }

    public ArrayList<Event> getEvents() {

        ArrayList<Event> events = new ArrayList<>();

        try {
            Cursor cursor = database.rawQuery("select * from Events", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String eventId = cursor.getString(cursor.getColumnIndex("eventId"));
                String eventDate = cursor.getString(cursor.getColumnIndex("eventDate"));
                String eventStartTime = cursor.getString(cursor.getColumnIndex("eventStartTime"));
                String eventEndTime = cursor.getString(cursor.getColumnIndex("eventEndTime"));
                String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
                String eventLocation = cursor.getString(cursor.getColumnIndex("eventLocation"));
                String tripId = cursor.getString(cursor.getColumnIndex("tripId"));
                Event event = new Event(eventId, eventDate, eventStartTime, eventEndTime, eventName, eventLocation,tripId);
                events.add(event);
                cursor.moveToNext();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("Database Reading Error", e.toString());
        }
        finally {
            //database.close();
        }
        return events;
    }

    public ArrayList<Event> getEvents(String selectedDate) {
        ArrayList<Event> events = new ArrayList<>();

        try {
            Cursor cursor = database.rawQuery("select * from Events", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String eventId = cursor.getString(cursor.getColumnIndex("eventId"));
                String eventDate = cursor.getString(cursor.getColumnIndex("eventDate"));
                String eventStartTime = cursor.getString(cursor.getColumnIndex("eventStartTime"));
                String eventEndTime = cursor.getString(cursor.getColumnIndex("eventEndTime"));
                String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
                String eventLocation = cursor.getString(cursor.getColumnIndex("eventLocation"));
                String tripId = cursor.getString(cursor.getColumnIndex("tripId"));
                Event event = new Event(eventId, eventDate, eventStartTime, eventEndTime, eventName, eventLocation,tripId);
                if(eventDate.equals(selectedDate)) {
                    events.add(event);
                }
                cursor.moveToNext();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("Database Reading Error", e.toString());
        }
        finally {
            //database.close();
        }
        return events;
    }

    public Event getEvent(int position) {
        return getEvents().get(position);
    }

    public String nextDay(String date) {
        int month = Integer.parseInt(date.substring(0,2));
        int day = Integer.parseInt(date.substring(3,5));
        int year = Integer.parseInt(date.substring(6,8));
        String newMonth;
        String newDay;
        if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            if(day == 31) {
                day = 1;
                month++;
            } else {
                day++;
            }
        } else if (month == 4 || month == 6 || month == 9  || month == 11) {
            if(day == 30) {
                day = 1;
                month++;
            } else {
                day++;
            }
        } else if (month == 2 && year%4 == 0) {
            if(day == 29) {
                day = 1;
                month++;
            } else {
                day++;
            }
        } else {
            if(day == 28) {
                day = 1;
                month++;
            } else {
                day++;
            }
        }

        if (month > 11) {
            month = 1;
            year++;
        } else {
            month++;
        }

        if(day < 10) {
            newDay = "0" + String.valueOf(day);
        } else {
            newDay = String.valueOf(day);
        }

        if(month < 10) {
            newMonth = "0" + String.valueOf(month);
        } else {
            newMonth = String.valueOf(month);
        }

        String nextDay = newMonth + "/" + newDay + "/" + year;

        return nextDay;
    }

}

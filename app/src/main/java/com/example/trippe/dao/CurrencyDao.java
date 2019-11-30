package com.example.trippe.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.trippe.model.Trip;
import com.example.trippe.model.TrippeCurrency;
import com.jjoe64.graphview.series.DataPoint;

public class CurrencyDao {
    public CurrencyDao() {

    }
    public DataPoint[] getCurrencyHistory(String startDate, String endDate, String toCurrency, String fromCurrency) {
        /* query db for all entries between start and end date for both currencies unless one is usd
        store results in array of TrippeCurrency
        count number of entries
        sort entries
        figure out missing dates
        get request api for missing dates, if failure, fill with empty data
        return datapoint array
         */
        SQLiteDatabase db;
        TrippeCurrency[] fromCurrencyArray;
        TrippeCurrency[] toCurrencyArray;
        TrippeCurrency _toCurrency = new TrippeCurrency(toCurrency);
        TrippeCurrency _fromCurrency = new TrippeCurrency(fromCurrency);
        DataPoint[] dataPoints = {};

        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM tbl_daily_rate WHERE tbl_daily_rate.currency_abbrev = '" + toCurrency + "';", null);
                Log.i("getCurrencyHistory", "Got " + String.valueOf(cursor.getCount()) + " entries");
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Log.i("getCurrencyHistory:", cursor.getString(cursor.getColumnIndex("date")) + " | " +
                            cursor.getString(cursor.getColumnIndex("currency_abbrev")) + " | " +
                            cursor.getString(cursor.getColumnIndex("exchange_rate")));
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                Log.e("getCurrencyHistory sql query", e.toString(), e);
                if (db.isOpen()) { // probably redundant
                    db.close();
                }
            } finally {
                db.close();
            }
            return dataPoints;
        } catch (Exception e) {
            Log.e("getCurrencyHistory openDatabase", e.toString(), e);

        }
        return dataPoints;
    }

    public void insertRate(String currency, double rate, String date) {
        /* query db for all entries between start and end date for both currencies unless one is usd
        store results in array of TrippeCurrency
        count number of entries
        sort entries
        figure out missing dates
        get request api for missing dates, if failure, fill with empty data
        return datapoint array
         */
        SQLiteDatabase db;
        long rowId = 0;
        ContentValues tblRow = new ContentValues();
        tblRow.put("currency_abbrev", currency);
        tblRow.put("exchange_rate", rate);
        tblRow.put("date", date);

        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);

            try {
                rowId = db.insert("tbl_daily_rate", "", tblRow);
                Log.i("insertRate", "Inserted tblRow into tbl_daily_rate");
            } catch (Exception e) {
                Log.e("insertRate", e.toString(), e);
                if (db.isOpen()) { // probably redundant
                    db.close();
                }
            } finally {
                Log.i("insertRate", "Row ID: " + String.valueOf(rowId));
                db.close();
            }
        } catch (Exception e) {
            Log.e("insertRate", e.toString(), e);

        }
    }
        //String get currency abbrev by country name (string country name)
}

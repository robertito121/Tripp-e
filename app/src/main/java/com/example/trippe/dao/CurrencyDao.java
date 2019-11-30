package com.example.trippe.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.sqlite.*;

import com.example.trippe.model.Trip;
import com.example.trippe.model.TrippeCurrency;
import com.jjoe64.graphview.series.DataPoint;

import static android.content.Context.MODE_PRIVATE;

public class CurrencyDao {
    public CurrencyDao() {

    }
    //TODO CHANGE TO ARRAY MAYBE AND ACTUALLY RETURN DATA
    public DataPoint[] getCurrencyHistory(String startDate, String endDate, String toCurrency, String fromCurrency) {
        /* query db for all entries between start and end date for both currencies unless one is usd
        store results in array of TrippeCurrency
        count number of entries
        sort entries
        figure out missing dates
        get request api for missing dates, if failure, fill with empty data
        return datapoint array
         */
        SQLiteDatabase db = null;
        TrippeCurrency[] fromCurrencyArray;
        TrippeCurrency[] toCurrencyArray;
        TrippeCurrency _toCurrency = new TrippeCurrency(toCurrency);
        TrippeCurrency _fromCurrency = new TrippeCurrency(fromCurrency);
        DataPoint[] dataPoints = {};

        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM tbl_daily_rate WHERE tbl_daily_rate.currency_abbrev = '" +
                        toCurrency +
                        "' AND tbl_daily_rate.date BETWEEN '" +
                        startDate +
                        "' AND '" +
                        endDate + "' ORDER BY tbl_daily_rate.date ASC;", null);
                //Log.i("getCurrencyHistory", "Query: '" + )
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
        SQLiteDatabase db  = null;
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
            } finally {
                Log.i("insertRate", "Row ID: " + String.valueOf(rowId));
                db.close();
            }
        } catch (Exception e) {
            Log.e("insertRate", e.toString(), e);

        }
    }
        //String get currency abbrev by country name (string country name)

    public void updateHistory() {
        /* check latest db dates
        compare to now
        get missing days
        insert missing days
        fill in days with no data
        update previous missing days
         */
    }

    public void makeTableRates(boolean overwrite) {
        // delete and recreate currency table
        SQLiteDatabase db = null;

        // create tbl_daily_rate
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.trippe/databases/TrippeDatabase", null, 0);
            try {
                if (overwrite) {
                    db.execSQL("DROP TABLE IF EXISTS tbl_daily_rate");
                    Log.i("makeTableRates", "Deleting old table");
                }
                db.execSQL("CREATE TABLE IF NOT EXISTS " + "tbl_daily_rate (" +
                        "date VARCHAR(11) NOT NULL," +
                        "currency_abbrev VARCHAR(3) NOT NULL," +
                        "exchange_rate REAL NOT NULL," +
                        "PRIMARY KEY (date, currency_abbrev));");
                Log.i("makeTableRates", "Creating new table");
            } catch (Exception e) {
                Log.e("makeTableRates", e.toString(), e);
            } finally {
                db.close();
            }
        }
        catch(Exception e){
            Log.e("makeTableRates", e.getMessage(), e);
            db = null;
        }
        finally {
            if (db != null) {
                db.close();
            }
        }
    }
    public void replaceEntry(){

    }
}

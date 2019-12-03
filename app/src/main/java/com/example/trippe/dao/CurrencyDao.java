package com.example.trippe.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.trippe.model.TrippeCurrency;
import com.example.trippe.ui.currency.WebAPIRequest;
import com.example.trippe.util.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class CurrencyDao {
    private final String dbPath = "/data/data/com.example.trippe/databases/TrippeDatabase";
    public CurrencyDao() {
    }

    public TrippeCurrency getCurrencyHistory(String startDate, String endDate, String currency) {
        /* query db for all entries between start and end date for both currencies unless one is usd
        store results in array of TrippeCurrency
        count number of entries
        figure out missing dates
        get request api for missing dates, if failure, fill with empty data
         */
        SQLiteDatabase db = null;
        TrippeCurrency _currency = new TrippeCurrency(currency);
        Date date;


        try {
            db = SQLiteDatabase.openDatabase(this.dbPath, null, 0);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM tbl_daily_rate WHERE tbl_daily_rate.currency_abbrev = '" +
                        currency +
                        "' AND tbl_daily_rate.date BETWEEN '" +
                        startDate +
                        "' AND '" +
                        endDate + "' ORDER BY tbl_daily_rate.date ASC;", null);
                Log.i("getCurrencyHistory", "Got " + cursor.getCount() + " entries for " + currency);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Log.i("getCurrencyHistory:", cursor.getString(cursor.getColumnIndex("date")) + " | " +
                            cursor.getString(cursor.getColumnIndex("currency_abbrev")) + " | " +
                            cursor.getString(cursor.getColumnIndex("exchange_rate")));
                            // Now to do the actual work:
                            date = this.getDateFromString(cursor.getString(cursor.getColumnIndex("date")));
                            _currency.addRate(date, cursor.getDouble(cursor.getColumnIndex("exchange_rate")));
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
            return _currency;
        } catch (Exception e) {
            Log.e("getCurrencyHistory openDatabase", e.toString(), e);

        }
        return _currency;
    }

    /*public double convertCurrency(String fromCurrency, String toCurrency) {
        /query db for all entries between start and end date for both currencies unless one is usd
        store results in array of TrippeCurrency
        count number of entries
        figure out missing dates
        get request api for missing dates, if failure, fill with empty data

        SQLiteDatabase db = null;
        TrippeCurrency _currency = new TrippeCurrency(currency);
        Date date;


        try {
            db = SQLiteDatabase.openDatabase(this.dbPath, null, 0);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM tbl_daily_rate WHERE tbl_daily_rate.currency_abbrev = '" +
                        currency +
                        "' AND tbl_daily_rate.date = " +
                        WebAPIRequest.getTodaysDate() +
                        ";", null);
                Log.i("getCurrencyHistory", "Got " + cursor.getCount() + " entries for " + currency);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Log.i("getCurrencyHistory:", cursor.getString(cursor.getColumnIndex("date")) + " | " +
                            cursor.getString(cursor.getColumnIndex("currency_abbrev")) + " | " +
                            cursor.getString(cursor.getColumnIndex("exchange_rate")));
                    // Now to do the actual work:
                    date = this.getDateFromString(cursor.getString(cursor.getColumnIndex("date")));
                    _currency.addRate(date, cursor.getDouble(cursor.getColumnIndex("exchange_rate")));
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
            return _currency;
        } catch (Exception e) {
            Log.e("getCurrencyHistory openDatabase", e.toString(), e);

        }
        return _currency;
    }
*/
    public double selectCurrencyRate(String date, String currency){
        SQLiteDatabase db = null;
        double rate = 0.0;
        int daysAgo = 0;
        try {
            db = SQLiteDatabase.openDatabase(this.dbPath, null, 0);
            if (WebAPIRequest.invalidDate(date)) {
                date = Utility.getTodaysDate();           }

            while (rate == 0) {
                String query = "SELECT * FROM tbl_daily_rate WHERE tbl_daily_rate.currency_abbrev = '" +
                        currency +
                        "' AND tbl_daily_rate.date = '" +
                        date +
                        "';";
                try {
                    Log.i("selectCurrencyRate", "Query: " + query);
                    Cursor cursor = db.rawQuery(query, null);
                    Log.i("selectCurrencyRate", "Got " + cursor.getCount() + " entries for " + currency);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        Log.i("selectCurrencyRate", cursor.getString(cursor.getColumnIndex("date")) + " | " +
                                cursor.getString(cursor.getColumnIndex("currency_abbrev")) + " | " +
                                cursor.getString(cursor.getColumnIndex("exchange_rate")));
                        // now do the actual work:
                        rate = cursor.getDouble(cursor.getColumnIndex("exchange_rate"));
                        cursor.moveToNext();
                    }
                } catch (Exception e) {
                    Log.e("selectCurrencyRate", e.toString(), e);
                    if (db.isOpen()) { // probably redundant
                        db.close();
                    }
                }
                daysAgo++;
                date = Utility.getDateAgo(daysAgo);
            }
            if (db.isOpen()) {
                db.close();
            }
            Log.i("selectCurrencyRate", "days Ago: " + (daysAgo - 1));
            return rate;
        } catch (Exception e) {
            Log.e("selectCurrencyRate", e.toString(), e);

        }
        return rate;
    }


    public void insertRate(String currency, double rate, String date) {
        SQLiteDatabase db  = null;
        long rowId = 0;
        ContentValues tblRow = new ContentValues();
        tblRow.put("currency_abbrev", currency);
        tblRow.put("exchange_rate", rate);
        tblRow.put("date", date);

        try {
            db = SQLiteDatabase.openDatabase(this.dbPath, null, 0);

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

    public void updateHistory(int daysAgo) {
        /* check latest db dates
        compare to now
        get missing days
        insert missing days
        fill in days with no data
        update previous missing days
         */
        WebAPIRequest request = new WebAPIRequest();
        request.setUrl("USD", Utility.getDateAgo(daysAgo), Utility.getTodaysDate());
        Log.i("updateHistory", "Calling getHistoryAsMap");
        Map<String, TrippeCurrency> currencyMap;
        currencyMap = request.getHistoryAsMap();
        for (String currency : currencyMap.keySet()) {
            Map<Date, Double> rates = currencyMap.get(currency).getRates();
            for (Date date : rates.keySet()){
                Log.i("updateHistory", "Inserting: " + currency + " " + rates.get(date) + " " + this.getStringFromDate(date));
                this.insertRate(currency, rates.get(date), this.getStringFromDate(date));
            }
        }
    }

    public void makeTableRates(boolean overwrite) {
        // delete and recreate currency table
        SQLiteDatabase db = null;

        // create tbl_daily_rate
        try {
            db = SQLiteDatabase.openDatabase(this.dbPath, null, 0);
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

    public Date getDateFromString(String inDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(inDate);
            return date;
        } catch (ParseException e) {
            Log.w("getDateFromString", "Date: " + inDate + " is invalid");
            date = new Date();
            return date;
        }
    }

    public String getStringFromDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strDate;
        strDate = df.format(date);
        return strDate;
    }
}

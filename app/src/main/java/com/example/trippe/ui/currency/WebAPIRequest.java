package com.example.trippe.ui.currency;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class WebAPIRequest {
    private String strUrl;

    public WebAPIRequest() {
    }

    // set custom url string
    public void setUrl(String url) {
        this.strUrl = url;
        Log.i("setUrl custom string", this.strUrl);
    }

    // get multiple days of rates for a single currency
    public void setUrl(String baseCurrency, String toCurrency, String startDate, String endDate) {
        if (this.invalidDate(startDate)) {
            return;
        } else if (this.invalidDate(endDate)) {
            return;
        } else if (this.invalidCurrency(baseCurrency)) {
            return;
        } else if (this.invalidCurrency(toCurrency)) {
            return;
        } else {
            this.strUrl = "https://api.exchangeratesapi.io/history?base=" +
                    baseCurrency +
                    "&start_at=" +
                    startDate +
                    "&end_at=" +
                    endDate +
                    "&symbols=" +
                    toCurrency;
            Log.i("setUrl historical/single currency", this.strUrl);
        }
    }

    // get single currency conversion rate for today
    public void setUrl(String baseCurrency, String toCurrency) { // for a single query of today's rates from base to toCurrency
        if (this.invalidCurrency(baseCurrency)) {
            return;
        } else if (this.invalidCurrency(toCurrency)){
            return;
        } else {
            this.strUrl = "https://api.exchangeratesapi.io/latest?base=" +
                    baseCurrency +
                    "&symbols=" +
                    toCurrency;
            Log.i("setUrl Single/latest", this.strUrl);
        }
    }

    // get historical rates of all currencies
    public void setUrl(String baseCurrency, String startDate, String endDate) {
        if (this.invalidDate(startDate)) {
            return;
        } else if (this.invalidDate(endDate)) {
            return;
        } else if (this.invalidCurrency(baseCurrency)) {
            return;
        } else {
            this.strUrl = "https://api.exchangeratesapi.io/history?base=" +
                    baseCurrency +
                    "&start_at=" +
                    startDate +
                    "&end_at=" +
                    endDate;
            Log.i("setUrl historical/all", this.strUrl);
        }
    }

    // reset url string
    public void clearUrl() {
        this.setUrl("");
        Log.i("clearUrl", this.strUrl);
    }

    // get url string
    public String getStrUrl() {
        return this.strUrl;
    }

    // check that a currency is a valid format and privided in our api
    public boolean invalidCurrency(String currency) {
        // TODO access database for list of currencies
        String currencies[] = {
                "USD", "EUR", "AUD", "BGN",
                "BRL", "CAD", "CHF", "CNY",
                "CZK", "DKK", "GBP", "HKD",
                "HRK", "HUF", "IDR", "ILS",
                "INR", "ISK", "JPY", "KRW",
                "MXN", "NOK", "NZD", "PHP",
                "PLN", "SEK", "SGD", "THB",
                "TRY", "RON", "RUB", "ZAR"
        };
        int x = 0;
        for (x = 0; x < currencies.length; x++) {
            if (currency.equals(currencies[x])) {
                return false;
            }
        }
        // no match, currency is invalid
        return true;
    }

    // checks that a string date is a valid format for the api
    public boolean invalidDate(String inDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(inDate);
        } catch (ParseException e) {
            Log.w("invalidDate", "Date: " + inDate + " is invalid");
            return true;
        }
            return false;
    }

    public Date getDateFromString(String inDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(inDate);
            return date;
        } catch (ParseException e) {
            Log.w("invalidDate", "Date: " + inDate + " is invalid");
            date = new Date();
            return date;
        }
    }


    public String getRatesAsString(){
        return this.httpGet();
    }

    // returns an unprocessed jsonobject from our query
    public JSONObject getRatesAsJSON() {
        JSONObject jsonGetResult;
        String httpGetResult = this.httpGet();

        try {
            jsonGetResult = new JSONObject(httpGetResult); // convert our string into a JSONObject
            return jsonGetResult;
        } catch (JSONException e) {
            return new JSONObject();
        }
    }


    public double getLatestRate() {
        JSONObject jsonGetResult = this.getRatesAsJSON();
        double rate = 0;
        try {
            JSONObject rates = jsonGetResult.getJSONObject("rates"); // neck down the data to the rates fields
            JSONArray keys = rates.names();
            Log.i("getLatestRate", String.valueOf(keys.length()));
            for (int x = 0; x < keys.length(); x++) {
                Log.i("getLatestRate", keys.getString(x));
            }
            rate = rates.getDouble(keys.getString(0)); // pull out our dest rate from json
            return rate;
            // TODO Actually use this data and then save to db. Parse out relevant fields*/
        } catch (Exception e) {
            return rate;
        }
    }
    /*
    public TrippeCurrency getLatestAsTrippeCurrency(){
        JSONObject jsonGetResult = this.getRatesAsJSON();
        JSONObject rate;
        TrippeCurrency currency = new TrippeCurrency();
        try {
            JSONObject rates = jsonGetResult.getJSONObject("rates"); // neck down the data to the rates fields
            JSONArray keys = rates.names();
            Date date = this.getDateFromString(rates.getString("date"));
            for (int x = 0; x < keys.length(); x++) {
                currency.setAbbreviation(keys.getString(x)); // set our currency abbrev
                rate = rates.getJSONObject(keys.getString(x));
               // currency.addRate(date, rate);
            }
        } catch(JSONException e){
            return new TrippeCurrency();
        }
        return currency;
    }
*/

   /* // return an array of currency objects
    public TrippeCurrency[] getHistoricalAsTrippeCurrency(){
            TrippeCurrency currencies[];

            return currencies;
        }
     */
    // perform http get request and return a string of json data
    public String httpGet() {
        URL url;
        HttpURLConnection request;
        String httpGetRequestData;

        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            return "";
        }
        try {
            request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET"); // GET request is needed for this api
            InputStream inputStream = request.getInputStream(); // connect and get a stream of data

            // build our stream readers in typical stupid java fashion to read a simple text response
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            httpGetRequestData = bufferedReader.readLine();
            request.disconnect(); // make sure our connection closes
            return httpGetRequestData;
        } catch (ProtocolException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }

/*
    private void sortDates() {

    }*/
}

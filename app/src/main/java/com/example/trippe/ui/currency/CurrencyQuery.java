package com.example.trippe.ui.currency;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.trippe.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CurrencyQuery {
    private String sourceCurrency = "";
    private String destCurrency = "";
    private float sourceAmount = 0;
    private float destAmount = 0;

    // We will query all source and destination currencies against USD as our base so we store
        // the exchange rates of each relative to USD for later conversion
    private float destExchangeRate = 0;
    private float sourceExchangeRate = 0;
    private Context context;

    public CurrencyQuery(Context context) {
        this.context = context;
    }
    public CurrencyQuery(Context context, String dest) {
        this.context = context;
        setDestCurrency(dest);
    }

    public CurrencyQuery(Context context, String dest, String source) {
        this.context = context;
        setDestCurrency(dest);
        setSourceCurrency(source);
    }

    public CurrencyQuery(Context context, String dest, float amount) {
        this.context = context;
        setDestCurrency(dest);
        setSourceAmount(amount);
    }

    public CurrencyQuery(Context context, String dest, String source, float amount) {
        this.context = context;
        setDestCurrency(dest);
        setSourceCurrency(source);
        setSourceAmount(amount);
    }

    public String getSourceCurrency() { return sourceCurrency; }
    public String getDestCurrency() { return destCurrency; }
    public float getSourceAmount() { return sourceAmount; }
    public float getDestAmount() { return destAmount; }

    public void setSourceCurrency(String source) { // set our local sourceCurrency if its a valid entry
        if (validCurrency(source) && !source.equals(this.destCurrency)) {
            this.sourceCurrency = source;
            Log.w("setSourceCurrency", "Set source currency to: " + this.sourceCurrency);
        }
    }

    public void setDestCurrency(String dest) {
        if (validCurrency(dest) && !dest.equals(this.sourceCurrency)) {
            this.destCurrency = dest;
            Log.w("setDestCurrency", "Set dest currency to: " + this.destCurrency);
        }
    }

    public void setSourceAmount(float amount) { // standard setter, ensures amount is > 0
        if (amount > 0 ) {
            this.sourceAmount = amount;
        }
    }

    public void getRequest(String date) { // TODO Change from void to something else
        /* For API information visit:
        https://exchangeratesapi.io/

        Query results are via HTTP Get method and return a JSON String that looks something like:
                            {
                              "base": "EUR",
                              "date": "2018-04-08",
                              "rates": {
                                "CAD": 1.565,
                                "CHF": 1.1798,
                                "GBP": 0.87295,
                                "SEK": 10.2983,
                                "EUR": 1.092,
                                "USD": 1.2234,
                                ...
                              }
                            }
        We mostly care about the relevant entries under "rates" and "date"
        */
        
        // Set up our URL
        String strUrl = "https://api.exchangeratesapi.io";
        
        // symbols in the api are the currency abbreviations we are using for each currency : EX EUR is the symbol for Euros
        String symbols = "&symbols=" + this.sourceCurrency + "," + this.destCurrency;
        
        // date format must be yyyy-mm-dd or "latest" for today's rate
        if (date.length() == 0) {// TODO add validity checks for date format
            date = "/latest"; // TODO change date to past 5 days for history
        }
        strUrl += date + "?base=USD"; // append our date base
        strUrl += symbols; // append our symbols

        try {
            URL url = new URL(strUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET"); // GET request is needed for this api
            InputStream inputStream = request.getInputStream(); // connect and get a stream of data

            // build our stream readers in typical stupid java fashion to read a simple text response
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String httpGetRequestData = bufferedReader.readLine();
            request.disconnect(); // make sure our connection closes

            // Convert to JSON
            Log.w("GET RESULT", httpGetRequestData); // log some shit

            JSONObject reader = new JSONObject(httpGetRequestData); // convert our string into a JSONObject
            JSONObject rates = reader.getJSONObject("rates"); // neck down the data to the rates fields
            this.destExchangeRate = Float.parseFloat(rates.getString(this.destCurrency)); // pull out our dest rate from json
            this.sourceExchangeRate = Float.parseFloat(rates.getString(this.sourceCurrency)); // pull out or source rage from json

            // FINALLY WE GET TO CONVERT STUFF
            this.destAmount = this.sourceAmount / this.sourceExchangeRate * this.destExchangeRate;
            // TODO Actually use this data and then save to db. Parse out relevant fields
        } catch (Exception e) {
            Log.e("CurrencyQuery", e.toString(), e);
            Toast toast = Toast.makeText(context.getApplicationContext(), "Woops, Something broke!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private boolean validCurrency(String currency) {
        if (currency.length() != 3) {
            return false;
        }

        String currencyOptions[]; // will store the values of our resource string array in strings.xml
        currencyOptions = context.getResources().getStringArray(R.array.currency_array);
        // iterate through our currency list to see if we passed a valid currency
        for (String currencyEntry : currencyOptions) {
            if (currencyEntry.substring(0, 3).equals(currency)) {
                return true;
            }
        }
        return false;
    }
}

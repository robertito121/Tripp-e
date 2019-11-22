package com.example.trippe.ui.currency;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.trippe.R;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class CurrencyQuery {
    // TODO add a history variable (likely a dictionary of tuples or something)
    private String sourceCurrency = "";
    private String destCurrency = "";
    private double sourceAmount = 0;
    private double destAmount = 0;

    // We will query all source and destination currencies against USD as our base so we store
        // the exchange rates of each relative to USD for later conversion
    private double destExchangeRate = 0;
    private double sourceExchangeRate = 0;
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

    public CurrencyQuery(Context context, String dest, double amount) {
        this.context = context;
        setDestCurrency(dest);
        setSourceAmount(amount);
    }

    public CurrencyQuery(Context context, String dest, String source, double amount) {
        this.context = context;
        setDestCurrency(dest);
        setSourceCurrency(source);
        setSourceAmount(amount);
    }

    public String getSourceCurrency() { return this.sourceCurrency; }
    public String getDestCurrency() { return this.destCurrency; }
    public double getSourceAmount() { return this.sourceAmount; }
    public double getDestAmount() { return this.destAmount; }

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

    public void setSourceAmount(double amount) { // standard setter, ensures amount is > 0
        if (amount > 0 ) {
            this.sourceAmount = amount;
        }
    }

    // TODO make sure results are ordered and reflect the difference in rate from the source and dest currency
    public DataPoint[] getRequestHistory(String startDate, String endDate) {
        // symbols in the api are the currency abbreviations we are using for each currency : EX EUR is the symbol for Euros
        String symbols = "&symbols=" +
                this.destCurrency;

        String strUrl = "https://api.exchangeratesapi.io/history?base=" +
                this.sourceCurrency +
                "&start_at=" +
                startDate +
                "&end_at=" +
                endDate +
                symbols;
        Log.w("URL REQUEST:", strUrl);

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
            JSONArray keys = rates.names();

            DataPoint points[] = new DataPoint[10];
            DataPoint point;

            int x;
            Log.w("Names", rates.names().toString());
            JSONObject rate;
            Log.w("Result Length", String.valueOf(keys.length()));
            /*int length = 9; // TODO this is a bad bandaid solution to not getting enough results
            for (x = length; x >=0; x--) {
                /*Log.w("LOOP History", "Key: " +
                        keys.getString(x) +
                        " Value: " +
                        rates.getString(keys.getString(x)));*/
            for (x = 0; x < 10; x++) {
                try {
                    rate = rates.getJSONObject(keys.getString(x));
                    Log.w("Loop", String.valueOf(x) + ", " + rate.getString(this.destCurrency));
                    point = new DataPoint(x, rate.getDouble(this.destCurrency));
                    points[x] = point;
                } catch (Exception e) {
                    Log.w("Loop:", String.valueOf(x) + ", 0");
                   // Log.e("API Error", e.toString(), e);
                    point = new DataPoint(x, 0);
                    points[x] = point;
                }

            }

            //LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);

            //return series;
            return points;
            //this.destExchangeRate = Double.parseDouble(rates.getString(this.destCurrency)); // pull out our dest rate from json
            //this.sourceExchangeRate = Double.parseDouble(rates.getString(this.sourceCurrency)); // pull out or source rage from json

            // FINALLY WE GET TO CONVERT STUFF
            //this.destAmount = this.sourceAmount / this.sourceExchangeRate * this.destExchangeRate;
            // TODO Actually use this data and then save to db. Parse out relevant fields
        } catch (Exception e) {
            Log.e("CurrencyQuery", e.toString(), e);
            Toast toast = Toast.makeText(context.getApplicationContext(), "Woops, Something broke!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        //LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
        DataPoint points[] = new DataPoint[] {
                new DataPoint(-9, 1),
                new DataPoint(-8, 1),
                new DataPoint(-7, 1),
                new DataPoint(-6, 1),
                new DataPoint(-5, 1),
                new DataPoint(-4, 1),
                new DataPoint(-3, 1),
                new DataPoint(-2, 1),
                new DataPoint(-1, 1),
                new DataPoint(0, 1)
        };
        return points;
        //return series;
    }

    public void getRequestLatest() { // TODO Change from void to something else
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
        We mostly care about the relevant entries under "rates" and "date" */

        // symbols in the api are the currency abbreviations we are using for each currency : EX EUR is the symbol for Euros
        String symbols = "&symbols=" + this.sourceCurrency + "," + this.destCurrency;

        // Set up our URL
        String strUrl = "https://api.exchangeratesapi.io/latest?base=USD" + symbols;

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
            this.destExchangeRate = Double.parseDouble(rates.getString(this.destCurrency)); // pull out our dest rate from json
            this.sourceExchangeRate = Double.parseDouble(rates.getString(this.sourceCurrency)); // pull out or source rage from json

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

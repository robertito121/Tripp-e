package com.example.trippe.ui.currency;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.trippe.R;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CurrencyQuery {
    private String sourceCurrency = "";
    private String destCurrency = "";
    private float sourceAmount = 0;
    private float destAmount = 0;
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
        if (validCurrency(source) && source != this.destCurrency) {
            this.sourceCurrency = source;
        }
    }

    public void setDestCurrency(String dest) {
        if (validCurrency(dest) && dest != this.sourceCurrency) {
            this.destCurrency = dest;
        }
    }

    public void setSourceAmount(float amount) { // standard setter, ensures amount is > 0
        if (amount > 0 ) {
            this.sourceAmount = amount;
        }
    }

    public void getRequest(String date) { // TODO Change from void to something else
        // Set up our URL
        String strUrl = "https://api.exchangeratesapi.io";
        if (date.length() == 0) {// TODO add validity checks for date format
            date = "/latest";
        }
        strUrl += date + "?base=USD";

        try {
            URL url = new URL(strUrl);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            // Convert to JSON
            JSONObject reader = new JSONObject(request.getContent().toString());
            Log.w("JSON RESULT", reader.getJSONObject("rates").toString());

        } catch (Exception e) {
            Log.e("CurrencyQuery", e.toString(), e);
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
            if (currencyEntry == currency) {
                return true;
            }
        }
        return false;
    }
}

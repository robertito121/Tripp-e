package com.example.trippe.ui.currency;

import android.content.Context;
import android.os.AsyncTask;
import android.text.PrecomputedText;
import android.util.Log;
import android.widget.Toast;

import com.example.trippe.dao.CurrencyDao;

import androidx.appcompat.app.AppCompatActivity;

public class AsyncRateRequest  extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        CurrencyDao currencyDao = new CurrencyDao();
        Log.i("AsyncRateRequest", "Starting async task");
        currencyDao.updateHistory(10);
        Log.i("AsyncRateRequest", "Finished async task");
        return null;
    }
}

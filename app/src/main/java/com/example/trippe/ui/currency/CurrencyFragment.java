package com.example.trippe.ui.currency;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.trippe.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CurrencyFragment extends Fragment implements OnClickListener {

    private CurrencyViewModel currencyViewModel;
    private View view; // need this as an inflated view to make using the fragment_currency.xml file easy

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnConvert) {
            // get our UI elements to pull data
            Spinner dropFromCurrency = (Spinner) view.findViewById(R.id.dropFromCurrency); // not sure why this cant resolve now, it works at runtime
            Spinner dropToCurrency = (Spinner) view.findViewById(R.id.dropToCurrency); // same here
            EditText txtFromAmount = (EditText) view.findViewById(R.id.txtFromAmount); // get our source currency amount
            TextView txtResult = (TextView) view.findViewById(R.id.txtResult); // notice the inflated view and not v

            // set our containers to hold ui element data
            String fromCurrency;
            String toCurrency;
            Float fromAmount;

            int red = Color.rgb(252, 159, 159);

            try {
                // retrieve the data from the ui elements
                fromCurrency = dropFromCurrency.getSelectedItem().toString();
                toCurrency = dropToCurrency.getSelectedItem().toString();
                fromAmount = Float.valueOf(txtFromAmount.getText().toString());
                Log.w("SHIT", "'" + toCurrency.length() + "'");
                // Do our checks to make sure we have valid inputs before accessing the web API or database
                if (fromAmount <= 0) { // make sure we got a positive amount of money to start
                    Toast toast = Toast.makeText(getContext(), "Please enter a positive amount of money greater than zero", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    txtFromAmount.setBackgroundColor(red);
                    txtResult.setText("");
                } else if (fromCurrency.length() == 0) { // make sure they selected a source currency
                    txtFromAmount.setBackgroundColor(Color.WHITE); // reset our text color in case it was changed due to errors
                    Toast toast = Toast.makeText(getContext(), "Please select a source currency", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    txtResult.setText("");
                } else if (toCurrency.length() == 0) { // make sure they selected a destination currency
                    txtFromAmount.setBackgroundColor(Color.WHITE); // reset our text color in case it was changed due to errors
                    Toast toast = Toast.makeText(getContext(), "Please select a currency to convert to", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    txtResult.setText("");
                } else if (fromCurrency == toCurrency) { // make sure the source and destination arent the same
                    txtFromAmount.setBackgroundColor(Color.WHITE); // reset our text color in case it was changed due to errors
                    Toast toast = Toast.makeText(getContext(), "The Target Currency is the Source Currency!\n Please Select a Different Currency", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    txtResult.setText("");
                } else {
                    //TODO WEB API REQUEST/DB QUERY
                    txtFromAmount.setTextColor(Color.WHITE); // reset our text color in case it was changed due to errors
                    txtResult.setText("Success!");
                }
            } catch (Exception e) {
                Log.e("CurrencyFragment", e.toString(), e);
                txtFromAmount.setText("");
                txtFromAmount.setBackgroundColor(red);
                Toast toast = Toast.makeText(getContext(), "Please enter a positive amount of money greater than zero", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                txtResult.setText("");
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel.class);
        view = inflater.inflate(R.layout.fragment_currency, container, false);

        // need this within android fragments to pass the right context to stuff
        Context context = getContext();

        // find our FROM dropdown in the UI
        Spinner dropFromCurrency = (Spinner) view.findViewById(R.id.dropFromCurrency); // not sure why this cant resolve now, it works at runtime
        // find our TO dropdown in the UI
        Spinner  dropToCurrency = (Spinner) view.findViewById(R.id.dropToCurrency); // same here


        Button btnConvert = (Button) view.findViewById(R.id.btnConvert); // hopefully we can get rid of this crap
        btnConvert.setOnClickListener(this);


        //------------------------------------------
        // The following block sets up our list of items to populate into the dropdown menus
        //------------------------------------------
        String currencyOptions[]; // will store the values of our resource string array in strings.xml

        currencyOptions = getResources().getStringArray(R.array.currency_array);

        // we need this to pass to our array adaptor
        List<String> options = new ArrayList<String>();

        // iterate over our currency options and add them to a List of strings
        int i;
        for (i = 0; i < currencyOptions.length; i++) {
            options.add(currencyOptions[i]);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, options);

        // Drop down layout style - makes it look pretty
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //------------------------------------------
        // The following block actually adds the values to each dropdown menu (TO and From)
        //------------------------------------------
        // attach our adaptor to our FROM dropdown and populate the options
        dropFromCurrency.setAdapter(currencyAdapter);

        // attach our adaptor to our TO dropdown and populate the options
        dropToCurrency.setAdapter(currencyAdapter);

        //-------------------------------------------
        // Testing a graph of currency histories
        //-------------------------------------------
        GraphView currencyGraph = (GraphView) view.findViewById(R.id.currencyGraph);
        currencyGraph.setTitle("5 Day History");
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        currencyGraph.addSeries(series);


       currencyViewModel.getText().observe(this, new Observer<String>() {
           @Override
           public void onChanged(@Nullable String s) {
           }
        });
        return view;
    }
}
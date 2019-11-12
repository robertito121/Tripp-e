package com.example.trippe.ui.currency;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class CurrencyFragment extends Fragment {

    private CurrencyViewModel currencyViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        currencyViewModel =
                ViewModelProviders.of(this).get(CurrencyViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_currency, container, false);
        currencyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // find our FROM dropdown in the UI
                Spinner dropFromCurrency = (Spinner) root.findViewById(R.id.dropFromCurrency); // not sure why this cant resolve now, it works at runtime
                // find our TO dropdown in the UI
                Spinner  dropToCurrency = (Spinner) root.findViewById(R.id.dropToCurrency); // same here


                Button btnConvert = (Button) root.findViewById(R.id.btnConvert); // hopefully we can get rid of this crap


                //------------------------------------------
                // The following block sets up our list of items to populate into the dropdown menus
                //------------------------------------------
                // need this within android fragments to pass the right context to stuff
                Context context = getContext();

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
                GraphView currencyGraph = (GraphView) root.findViewById(R.id.currencyGraph);
                currencyGraph.setTitle("5 Day History");
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                });

                currencyGraph.addSeries(series);

            }
        });
        return root;
    }
}
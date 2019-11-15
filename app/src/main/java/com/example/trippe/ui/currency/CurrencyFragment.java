package com.example.trippe.ui.currency;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CurrencyFragment extends Fragment implements Spinner.OnItemSelectedListener { // OnClickListener

    private CurrencyViewModel currencyViewModel;
    private View view; // need this as an inflated view to make using the fragment_currency.xml file easy
    // need this within android fragments to pass the right context to stuff
    private  Context context = getContext();
    private String currencyOptions[]; // will store the values of our resource string array in strings.xml
    private Spinner dropFromCurrency; //= (Spinner) view.findViewById(R.id.dropFromCurrency);
    private Spinner dropToCurrency;// = (Spinner) view.findViewById(R.id.dropToCurrency);
    private EditText txtFromAmount; //= (EditText) view.findViewById(R.id.txtFromAmount); // get our source currency amount
    private TextView txtResult;
    private ImageView imgFromFlag;
    private ImageView imgToFlag;


    private final TextWatcher txtFromAmountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                Log.w("onTextChanged", "Calling processFormInput");
                processFormInput();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel.class);
        view = inflater.inflate(R.layout.fragment_currency, container, false);

        // find our UI elements
        this.dropFromCurrency = (Spinner) view.findViewById(R.id.dropFromCurrency);
        this.dropToCurrency = (Spinner) view.findViewById(R.id.dropToCurrency);
        this.txtFromAmount = (EditText) view.findViewById(R.id.txtFromAmount);
        this.txtFromAmount = (EditText) view.findViewById(R.id.txtFromAmount); // get our source currency amount
        this.txtResult = (TextView) view.findViewById(R.id.txtResult); // notice the inflated view and not v
        this.imgFromFlag = (ImageView) view.findViewById(R.id.imgFromFlag);
        this.imgToFlag = (ImageView) view.findViewById(R.id.imgToFlag);


        //Button btnConvert = (Button) view.findViewById(R.id.btnConvert); // hopefully we can get rid of this crap
        // Add our listeners
        this.dropFromCurrency.setOnItemSelectedListener(this);
        this.dropToCurrency.setOnItemSelectedListener(this);
        this.txtFromAmount.addTextChangedListener(txtFromAmountWatcher);


        //------------------------------------------
        // The following block sets up our list of items to populate into the dropdown menus
        //------------------------------------------
        currencyOptions = getResources().getStringArray(R.array.currency_array);

        // we need this to pass to our array adaptor
        List<String> options = new ArrayList<String>();

        // iterate over our currency options and add them to a List of strings
        int i;
        for (i = 0; i < currencyOptions.length; i++) {
            options.add(currencyOptions[i]);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, options);

        // Drop down layout style - makes it look pretty
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //------------------------------------------
        // The following block actually adds the values to each dropdown menu (TO and From)
        //------------------------------------------
        // attach our adaptor to our FROM dropdown and populate the options
        this.dropFromCurrency.setAdapter(currencyAdapter);

        // attach our adaptor to our TO dropdown and populate the options
        this.dropToCurrency.setAdapter(currencyAdapter);

        //-------------------------------------------
        // Testing a graph of currency histories
        //-------------------------------------------
        // TODO update graph realtime based on exchange rate history
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
    // this is used to get our resource id via a string so we can set our flag icons by name
    public int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            Log.e("setFlag", e.toString(), e);
            return -1;
        }
    }

    private void setFlag(ImageView imgView, String currency) {
        if (currency.equals("TRY")) { // limitation of resources, they cant use java keywords so
            currency = "tur";           // try had to be changed to tur for the flag name
        } else {
            currency = currency.toLowerCase();
        }

        // now try to set our icon
        try {
            int resourceId = getResId(currency, R.drawable.class); // use some fancy reflection to get our image name via string
            if (resourceId != -1) {
                imgView.setImageResource(resourceId);
            }
        } catch (Exception e) {
            Log.e("setFlag", e.toString(), e);
        }
    }


    private void processFormInput() {
        // set our containers to hold ui element data
        String fromCurrency = "";
        String toCurrency = "";
        float fromAmount = 0;
        String strFromAmount = this.txtFromAmount.getText().toString();
        Log.w("processFormInput", String.valueOf(strFromAmount.length()));

        try {
            fromCurrency = this.dropFromCurrency.getSelectedItem().toString().substring(0, 3);  // needed to parse out just the currency abbreviation
            setFlag(this.imgFromFlag, fromCurrency); // change our flag icon
            toCurrency = this.dropToCurrency.getSelectedItem().toString().substring(0, 3); // same here as above
            setFlag(this.imgToFlag, toCurrency);
        } catch (Exception e) {
            Log.e("processFormInput", e.toString(), e);
        }

        //int red = Color.rgb(252, 159, 159); // set a local var for red error color
        if (strFromAmount.length() > 0) {
            try {
                // retrieve the data from the ui elements
                Log.w("processFormInput", this.txtFromAmount.getText().toString());
                fromAmount = Float.valueOf(strFromAmount);
                // Do our checks to make sure we have valid inputs before accessing the web API or database
                if (fromAmount <= 0) { // make sure we got a positive amount of money to start
                    //this.txtFromAmount.setBackgroundColor(red);
                    this.txtResult.setText("");
                    this.txtFromAmount.setText("");
                } else if (fromCurrency.equals(toCurrency)) { // make sure the source and destination arent the same
                    //this.txtFromAmount.setBackgroundColor(Color.WHITE); // reset our text color in case it was changed due to errors
                    this.txtResult.setText(strFromAmount);
                } else {
                    //TODO WEB API REQUEST/DB QUERY
                    CurrencyQuery currencyQuery = new CurrencyQuery(getContext(), toCurrency, fromCurrency);
                    currencyQuery.setSourceAmount(fromAmount);
                    currencyQuery.getRequest(""); //TODO update this based on the date picker
                    this.txtFromAmount.setBackgroundColor(Color.WHITE); // reset our text color in case it was changed due to errors
                    this.txtResult.setText(String.valueOf(currencyQuery.getDestAmount()));
                }

            } catch (Exception e) {
                Log.e("CurrencyFragment", e.toString(), e);
                this.txtResult.setText("");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> aView, View v, int selectedInt, long selectedLong) {
       processFormInput();
}


    @Override
    public void onNothingSelected(AdapterView<?> aView) {

    }

}
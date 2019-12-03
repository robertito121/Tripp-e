package com.example.trippe.ui.currency;

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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.trippe.R;
import com.example.trippe.dao.CurrencyDao;
import com.example.trippe.util.Utility;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CurrencyFragment extends Fragment implements Spinner.OnItemSelectedListener { // OnClickListener

    private CurrencyViewModel currencyViewModel;
    private View view; // need this as an inflated view to make using the fragment_currency.xml file easy
    private String currencyOptions[]; // will store the values of our resource string array in strings.xml
    private Spinner dropFromCurrency;
    private Spinner dropToCurrency;
    private EditText txtFromAmount;
    private TextView txtResult;
    private ImageView imgFromFlag;
    private ImageView imgToFlag;
    private GraphView currencyGraph;
    private LineGraphSeries<DataPoint> series;


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
        this.currencyOptions = getResources().getStringArray(R.array.currency_array);

        // we need this to pass to our array adaptor
        List<String> options = new ArrayList<String>();

        // iterate over our currency options and add them to a List of strings
        int i;
        for (i = 0; i < this.currencyOptions.length; i++) {
            options.add(this.currencyOptions[i]);
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
        this.currencyGraph = (GraphView) view.findViewById(R.id.currencyGraph);
        this.currencyGraph.setTitle("10 Day History");
        this.series = new LineGraphSeries<DataPoint>();/*new DataPoint[] {
                new DataPoint(-10, 0),
                new DataPoint(-9, 0),
                new DataPoint(-8, 0),
                new DataPoint(-7, 0),
                new DataPoint(-6, 0),
                new DataPoint(-5, 0),
                new DataPoint(-4, 0),
                new DataPoint(-3, 0),
                new DataPoint(-2, 0),
                new DataPoint(-1, 0),
                new DataPoint(0, 0)
        });*/

        this.currencyGraph.addSeries(series);
        return view;
    }

    private void setFlag(ImageView imgView, String currency) {
        // TODO pull flag name based on currency from db
        TemporaryFlagMap flagMap = new TemporaryFlagMap();
        String flagName = flagMap.flags.get(currency);

        // now try to set our icon
        try {
            int resourceId = Utility.getResourceIndicatorByString(flagName, R.drawable.class); // use some fancy reflection to get our image name via string
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
        double fromAmount = 0;
        String strFromAmount = this.txtFromAmount.getText().toString();
        Log.w("processFormInput", String.valueOf(strFromAmount.length()));
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(4);
        CurrencyDao currencyDao = new CurrencyDao();

        try {
            fromCurrency = this.dropFromCurrency.getSelectedItem().toString().substring(0, 3);  // needed to parse out just the currency abbreviation
            setFlag(this.imgFromFlag, fromCurrency); // change our flag icon
            toCurrency = this.dropToCurrency.getSelectedItem().toString().substring(0, 3); // same here as above
            setFlag(this.imgToFlag, toCurrency);
        } catch (Exception e) {
            Log.e("processFormInput", e.toString(), e);
        }

        if (strFromAmount.length() > 0) {
            try {
                // retrieve the data from the ui elements
                Log.w("processFormInput", this.txtFromAmount.getText().toString());
                fromAmount = Double.valueOf(strFromAmount);
                // Do our checks to make sure we have valid inputs before accessing the web API or database
                if (fromAmount <= 0) { // make sure we got a positive amount of money to start
                    this.txtResult.setText("");
                    this.txtFromAmount.setText("");
                } else if (fromCurrency.equals(toCurrency)) { // make sure the source and destination arent the same
                    this.txtResult.setText(decimalFormat.format(fromAmount));
                } else {
                        // TODO implement graph currencyDao.getCurrencyHistory()
                        double fromRate = currencyDao.selectCurrencyRate("", fromCurrency);
                        double toRate = currencyDao.selectCurrencyRate("", toCurrency);
                        Log.i("processFormInput", "to:" + toRate + " from:" + fromRate);
                        if (toRate > 0) {
                            double rate = fromAmount * (1/fromRate) * toRate;
                            this.txtResult.setText(decimalFormat.format(rate));
                        } else {
                            this.txtResult.setText("");
                        }
                    /*
                    CurrencyQuery currencyQuery = new CurrencyQuery(getContext(), toCurrency, fromCurrency);
                    currencyQuery.setSourceAmount(fromAmount);
                    currencyQuery.getRequestLatest();*/


                    //LineGraphSeries<DataPoint> updateSeries = new LineGraphSeries<DataPoint>((DataPoint[]) currencyQuery.getRequestHistory(strAgo, strNow));
                    //this.currencyGraph.addSeries(updateSeries);
                    //this.series.appendData
                    //this.series.appendData(currencyQuery.getRequestHistory(strAgo, strNow));

                   // this.series.resetData( (DataPoint[]) currencyQuery.getRequestHistory(strAgo, strNow));
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
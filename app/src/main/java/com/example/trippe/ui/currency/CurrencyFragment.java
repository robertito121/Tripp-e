package com.example.trippe.ui.currency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.trippe.R;

public class CurrencyFragment extends Fragment {

    private CurrencyViewModel currencyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        currencyViewModel =
                ViewModelProviders.of(this).get(CurrencyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_currency, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        currencyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                //spinnerFromCurrency = (Spinner) findViewById(R.id.spinnerFromCurrency);
            }
        });
        return root;
    }
}
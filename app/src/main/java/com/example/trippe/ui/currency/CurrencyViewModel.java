package com.example.trippe.ui.currency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrencyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CurrencyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Currency fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
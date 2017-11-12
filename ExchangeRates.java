package com.example.odeh.intermediate;

/**
 * Created by odeh on 11/4/17.
 */

public class ExchangeRates {
    /*String value to store the currency*/
    private String mCurrency;

    /*Real value to store exchange rate*/
    private double mExchangeRate;

    /*Constructor to initialize variable values*/
    ExchangeRates(String currency, double exchangeRate){
        /*Initialize mCurrency variable*/
        mCurrency = currency;

        /*Initialize mExchangeRate variable*/
        mExchangeRate = exchangeRate;
    }

    /*Public method that allows access to mCurrency value*/
    public String getCurrency(){
        return mCurrency;
    }

    /*Public method that allows access to mExchangeRate value*/
    public double getExchangeRate(){
        return mExchangeRate;
    }
}

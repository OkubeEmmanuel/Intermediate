package com.example.odeh.intermediate;

/**
 * Created by odeh on 11/4/17.
 */

import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.List;

/**
 * Loads a list of exchange rates by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ExchangeRatesLoader extends AsyncTaskLoader<List<ExchangeRates>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ExchangeRatesLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link ExchangeRatesLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public ExchangeRatesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<ExchangeRates> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<ExchangeRates> exchangeRates = HelperMethods.fetchExchangeRates(mUrl);
        return exchangeRates;
    }
}


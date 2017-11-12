package com.example.odeh.intermediate;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<ExchangeRates>> {

    private static final String LOG_TAG = ExchangeRatesActivity.class.getName();

    /** URL for exchange rates data from the cryptocompare dataset */
    private  String CRC_REQUEST_URL =
            "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,EUR,CNY,GBP,JPY,CAD,SGD,PLN,AUD,HKD,RUB,CHF,INR,ZAR,USDT,KRW,NZD,SEK,ILS,NGN";

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /** Adapter for the list of earthquakes */
    private ExchangeAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /**
     * ProgressView that is displayed before list of earthquakes
     * or empty view is displayed
     */
    private ProgressBar mProgressBarView;

    /**Connectivity manager*/
    private ConnectivityManager mConnectivityManager;

    // recycler view
    RecyclerView exchangeRateRecyclerView;

    String cc = "BTC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_activity);

        mConnectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //Initialize progress bar.
        mProgressBarView =  findViewById(R.id.loading_spinner);

        //Initialize mEmptyTextView.
        mEmptyStateTextView =  findViewById(R.id.empty_view);

        // Find a reference to the {@link RecyclerView} in the layout
       exchangeRateRecyclerView = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        exchangeRateRecyclerView.setLayoutManager(llm);

        if (isConnected){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            final LoaderManager loaderManager = getLoaderManager();

            // Spinner element
            Spinner spinner = findViewById(R.id.spinner);

            // Spinner click listener
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedItemText = (String) parentView.getItemAtPosition(position);
                    if (selectedItemText.equalsIgnoreCase("btc")){
                        CRC_REQUEST_URL =
                                "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,EUR,CNY,GBP,JPY,CAD,SGD,PLN,AUD,HKD,RUB,CHF,INR,ZAR,USDT,KRW,NZD,SEK,ILS,NGN";
                        loaderManager.restartLoader(EARTHQUAKE_LOADER_ID, null, ExchangeRatesActivity.this);
                        cc = "BTC";
                    }
                    else {
                        CRC_REQUEST_URL =
                                "https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD,EUR,CNY,GBP,JPY,CAD,SGD,PLN,AUD,HKD,RUB,CHF,INR,ZAR,USDT,KRW,NZD,SEK,ILS,NGN";
                        loaderManager.restartLoader(EARTHQUAKE_LOADER_ID, null, ExchangeRatesActivity.this);
                        cc = "ETH";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    CRC_REQUEST_URL =
                            "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,EUR,CNY,GBP,JPY,CAD,SGD,PLN,AUD,HKD,RUB,CHF,INR,ZAR,USDT,KRW,NZD,SEK,ILS,NGN";
                }

            });

            // Spinner Drop down elements
            List<String> categories = new ArrayList<>();
            categories.add("BTC");
            categories.add("ETH");

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else {
            //Hide progressbar
            mProgressBarView.setVisibility(View.GONE);

            //Set emptyState textView to no internet connection.
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }
    @Override
    public Loader<List<ExchangeRates>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new ExchangeRatesLoader(this, CRC_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(final Loader<List<ExchangeRates>> loader, final List<ExchangeRates> exchangeRates) {
        // Clear the adapter of previous earthquake data

        // Create a new adapter that takes an empty list of exchange rates as input
        mAdapter = new ExchangeAdapter(exchangeRates, new CustomItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent conversionIntent = new Intent(ExchangeRatesActivity.this, ConversionActivity.class);
                conversionIntent.putExtra("one", cc );
                conversionIntent.putExtra("two", exchangeRates.get(position).getCurrency());
                String er = Double.toString(exchangeRates.get(position).getExchangeRate());
                conversionIntent.putExtra("three", er);
                startActivity(conversionIntent);
            }
        });

        // If there is a valid list of {@link ExchangeRates}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (exchangeRates != null && !exchangeRates.isEmpty()) {
            mEmptyStateTextView.setVisibility(View.INVISIBLE);
            exchangeRateRecyclerView.setAdapter(mAdapter);
        }
        else {
            // Set empty state text to display "No exchange rates found."
            mEmptyStateTextView.setText(R.string.no_exchange_rates);
            exchangeRateRecyclerView.setVisibility(View.INVISIBLE);
        }

        //hide progressbar
        mProgressBarView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<ExchangeRates>> loader) {
        // Loader reset, so we can clear out our existing data.
        loader.reset();
        //hide progressbar
        exchangeRateRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBarView.setVisibility(View.VISIBLE);
    }
}

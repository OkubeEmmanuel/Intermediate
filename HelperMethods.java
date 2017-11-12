package com.example.odeh.intermediate;

/**
 * Created by odeh on 11/4/17.
 */

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving exchange rates from cryptocompare.
 */
public final class HelperMethods {

    /** Tag for the log messages */
    private static final String LOG_TAG = HelperMethods.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link HelperMethods} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private HelperMethods() {
    }

    /**
     * Query the cryptocompare dataset and return a list of {@link ExchangeRates} objects.
     */
    public static List<ExchangeRates> fetchExchangeRates(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<ExchangeRates> exchanges = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return exchanges;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link ExchangeRates} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<ExchangeRates> extractFeatureFromJson(String exchangeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(exchangeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding exchange rates to
        List<ExchangeRates> exchanges = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            //create an array to store all currencies
            String[] currencyCodes =
                    {"USD","EUR","CNY","GBP","JPY","CAD","SGD","PLN","AUD","HKD",
                    "RUB","CHF","INR","ZAR","USDT","KRW","NZD","SEK","ILS","NGN"};

            // Create a JSONObject from the JSON response string
            JSONObject jsonResponse = new JSONObject(exchangeJSON);

            //Extract exchange rates corresponding to each currency
            for (short i = 0; i < currencyCodes.length; i++){
                double exchangeRate = jsonResponse.getDouble(currencyCodes[i]);
                // Create a new {@link ExchangeRates} object with the currency code
                // and exchangeRates rate from the JSON response.
                ExchangeRates exchangeRates = new ExchangeRates(currencyCodes[i], exchangeRate);

                // Add the new {@link ExchangeRates} to the list of exchanges.
                exchanges.add(exchangeRates);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("HelperMethods", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of exchange rates
        return exchanges;
    }

}


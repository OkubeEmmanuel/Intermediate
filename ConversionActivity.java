package com.example.odeh.intermediate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class ConversionActivity extends AppCompatActivity {

    /*Currency label*/
    private TextView currencyTV;

    /*world currency label*/
    private TextView wcl;

    /*world currency edit text view*/
    private EditText wce;

    /*crypto currency label*/
    private TextView ccl;

    /*crypto currency edit text view*/
    private EditText cce;

    /*Exchange rate variable*/
    private double er;

    /*Method to convert world currency to crypto currency*/
    public void ccToWorldCurrency(){
        String cv = cce.getText().toString();
        double cryptoValue;
        if (cv.isEmpty()){
            cryptoValue = 0;
        }
        else {
            cryptoValue = Double.parseDouble(cv);
        }
        String worldValue = Double.toString((cryptoValue*er));
        wce.setText(worldValue);
    }

    /*Method to convert world currency to crypto currency*/
    public void wcToCryptoCurrency(){
        double wcv = 1/er;
        String wv = wce.getText().toString();
        double wcValue;
        if (wv.isEmpty()){
            wcValue = 0;
        }
        else {
            wcValue = Double.parseDouble(wv);
        }
        String worldValue = Double.toString((wcValue*wcv));
        cce.setText(worldValue);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

    /*Variable that holds crypto currency*/
        String cc = intent.getStringExtra("one");

    /*Variable to hold world currency*/
         String wc = intent.getStringExtra("two");

    /*Variable to hod exchange rate between both currencies*/
        er = Double.parseDouble(intent.getStringExtra("three"));
        setContentView(R.layout.activity_conversion);

        //initialize currencyTV
        currencyTV = findViewById(R.id.conversion_label);
        currencyTV.setText("Convert "+wc+" to or from "+cc);

        //initialize world currency text view
        wcl = findViewById(R.id.world_currency_text_view);
        wcl.setText(wc);

        //initialize world currency edit text view
        wce = findViewById(R.id.world_currency_edit_text);
        wce.setText(Double.toString(er));

        //initialize world currency text view
        ccl = findViewById(R.id.crypto_text_view);
        ccl.setText(cc);

        //initialize world currency edit text view
        cce = findViewById(R.id.crypto_edit_text);
        cce.setText("1");

        cce.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( (event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER) ) {
                    ccToWorldCurrency();
                    return true;
                }
                return false;
            }
        });

        wce.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( (event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER) ) {
                    wcToCryptoCurrency();
                    return true;
                }
                return false;
            }
        });
    }

}

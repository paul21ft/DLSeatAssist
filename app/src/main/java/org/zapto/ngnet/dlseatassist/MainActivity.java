package org.zapto.ngnet.dlseatassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Set;


public class MainActivity extends ActionBarActivity {




    //private EditText eText;
    private TextView textView;
    private String message;
    private Boolean isSearching;

    private EditText flightNumber;
    private EditText flightOrig;
    private EditText flightDest;
    private EditText flightCarrier;
    private EditText flightDay;
    private Spinner flightMonth;
    private EditText flightYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.eText=(EditText) findViewById(R.id.flNum);
        this.textView = (TextView) findViewById(R.id.resultText);
        //this.textView.setMovementMethod(new ScrollingMovementMethod());

        flightMonth = (Spinner) findViewById(R.id.flightMonth);
        flightMonth.setSelection(10);

        this.flightNumber = (EditText) findViewById(R.id.flightNumber);
        this.flightOrig = (EditText) findViewById(R.id.flightOrig);
        this.flightDest = (EditText) findViewById(R.id.flightDest);
        this.flightCarrier = (EditText) findViewById(R.id.flightCarrier);
        this.flightDay = (EditText) findViewById(R.id.flightDay);
        this.flightYear = (EditText) findViewById(R.id.flightYear);

        isSearching=false;

        //Some other initilization of preferences
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);

        if (!prefs.contains(Constants.PREFS_SETTINGS_ENABLE_ALERTS)) {
            SharedPreferences.Editor editor = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(Constants.PREFS_SETTINGS_ENABLE_ALERTS,true);
            editor.apply();
        }


        updateAlertInfo();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateAlertInfo();
    }

    private void updateAlertInfo() {
        TextView aText = (TextView) findViewById(R.id.alertInfo);
        String aStr = "";

        AlertData aData =  AlertData.loadSavedAlerts(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        boolean enableAlerts = prefs.getBoolean(Constants.PREFS_SETTINGS_ENABLE_ALERTS,true);

        if(enableAlerts) {
            aStr += "Alerts ENABLED: ";
            aStr += aData.getNumAlerts().toString() + " Active Alerts";
        }else{
            aStr += "Alerts ----DISABLED---- (Enable in Settings)";
        }

        aText.setText(aStr);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public FlightInfo getFlifo() {

        Integer fnum = Integer.parseInt(flightNumber.getText().toString());
        String fOrig = flightOrig.getText().toString();
        String fDest = flightDest.getText().toString();
        String carrier = flightCarrier.getText().toString();
        String sDate = flightDay.getText().toString() + flightMonth.getSelectedItem().toString();
        String lDate = flightDay.getText().toString() + " " + flightMonth.getSelectedItem().toString().toUpperCase() + " " + flightYear.getText().toString();

        return new FlightInfo(fnum,sDate,lDate,fOrig,fDest,carrier);

    }

    public void goSearch(View view) {
        textView.setText("Searching.....");
        if (isSearching){
            textView.setText("Still Searching...");
            return;
        }
        isSearching=true;
        new Thread(new Runnable() {
            public void run() {




                DLSeatInterface DLSI = new DLSeatInterface(getFlifo());
                message = DLSI.createDisplayString();

                runOnUiThread(new Runnable() {
                    public void run() {
                        textView.setText(message);
                        isSearching=false;
                    }
                });

            }
        }).start();

    }

    public void goAddAlert(View view) {
        Intent intent = new Intent(this, SetAlerts.class);
        intent.putExtra("FLIFOData",getFlifo());
        startActivity(intent);
    }

    public void goViewAlerts(View view) {
        Intent intent = new Intent(this, GetAlerts.class);
        startActivity(intent);
    }



}

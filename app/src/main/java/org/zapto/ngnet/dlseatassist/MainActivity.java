package org.zapto.ngnet.dlseatassist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    public final static String MY_PREFS_NAME = "org.zapto.ngnet.dlseatassist";
    public final static String PREFS_ALERT_STORAGE = "JSONAlerts";


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

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        if (!prefs.contains(PREFS_ALERT_STORAGE)) {
            editor.putString(PREFS_ALERT_STORAGE, "");
            editor.commit();
        }
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
}

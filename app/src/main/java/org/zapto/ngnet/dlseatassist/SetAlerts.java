package org.zapto.ngnet.dlseatassist;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SetAlerts extends ActionBarActivity {

    private TextView flightInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alerts);

        this.flightInfoView = (TextView) findViewById(R.id.listFlightInfo);

        Intent intent = getIntent();

        FlightInfo flifo = (FlightInfo) intent.getSerializableExtra("FLIFOData");

        String flightInfoStr = "";

        flightInfoStr += flifo.carrier + flifo.flightNumber + "  ";
        flightInfoStr += flifo.flightOrig + " to " + flifo.flightDest + "\n";
        flightInfoStr += flifo.longFlightDate + "\n\n";

        flightInfoView.setText(flightInfoStr);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_alerts, menu);
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

}

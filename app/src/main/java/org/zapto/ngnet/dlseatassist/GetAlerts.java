package org.zapto.ngnet.dlseatassist;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class GetAlerts extends ActionBarActivity {

    public final static String MY_PREFS_NAME = "org.zapto.ngnet.dlseatassist";
    public final static String PREFS_ALERT_STORAGE = "JSONAlerts";


    private AlertData flightAlertData;

    private TextView activeAlertText;

    private Integer activeDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_alerts);

        this.activeDelete=0;

        this.activeAlertText = (TextView) findViewById(R.id.activeAlertText);


        try {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            if (!prefs.contains(PREFS_ALERT_STORAGE)) {
                AlertData fAData = new AlertData();
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream so = new ObjectOutputStream(bo);
                so.writeObject(flightAlertData);
                so.flush();
                editor.putString(PREFS_ALERT_STORAGE,so.toString() );
                editor.apply();
            }

            String objectString = prefs.toString();
            byte b[] = objectString.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            AlertData fAData = (AlertData) si.readObject();
            this.flightAlertData=fAData;
        } catch (Exception e) {
            return;
        }

        activeAlertText.setText(this.flightAlertData.alertListToString());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_alerts, menu);
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

    public void handleDelete(Integer numToDelete, Boolean deleteAll) {
        if (deleteAll) {
            this.flightAlertData.removeAllAlerts();
        }
        else {
            this.flightAlertData.removeAlert(numToDelete);
        }
    }

    public void displayAlerts() {
        activeAlertText.setText(this.flightAlertData.alertListToString());
    }


    public void goDeleteAlert(View view) {
        Button delBtn = (Button) findViewById(R.id.deleteAlert);
        EditText delNum = (EditText) findViewById(R.id.deleteNumber);
        Integer delNumInt=0;

        try {
            delNumInt = Integer.parseInt(delNum.getText().toString());
        } catch (Exception e) {
            return;
        }

        if (activeDelete == 0) {
            delBtn.setText("Delete " + delNumInt.toString() + "?");
            activeDelete=delNumInt;
        }else{
            delBtn.setText("Delete Alert");
            handleDelete(activeDelete,false);
            activeDelete=0;
            displayAlerts();
        }

    }

    public void goDeleteAllAlerts(View view) {
        Button delBtn = (Button) findViewById(R.id.deleteAllAlerts);

        if (activeDelete == 0) {
            delBtn.setText("Delete All??");
            activeDelete=1;
        }else{
            delBtn.setText("Delete All");
            handleDelete(activeDelete,true);
            activeDelete=0;
            displayAlerts();
        }
    }




}

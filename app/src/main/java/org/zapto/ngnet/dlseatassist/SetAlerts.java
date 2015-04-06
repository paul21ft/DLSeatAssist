package org.zapto.ngnet.dlseatassist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SetAlerts extends ActionBarActivity {

    public final static String MY_PREFS_NAME = "org.zapto.ngnet.dlseatassist";
    public final static String PREFS_ALERT_STORAGE = "JSONAlerts";

    AlertData flightAlertData;
    DLSeatInterface DLSI;

    private FlightInfo flifo;
    public List<String> seatList;
    public Boolean isSearching;

    private TextView flightInfoView;
    private TextView testResultList;

    private selectSeatParams activeSeat;

    public void loadSavedAlerts() {
        try {
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            String objectString = prefs.getString(PREFS_ALERT_STORAGE,"");
            if (objectString.length() == 0) {
                this.flightAlertData = new AlertData();
            } else {
                this.flightAlertData = new AlertData(objectString);
            }
            assert Boolean.TRUE;

        } catch (Exception e) {
            this.flightAlertData=new AlertData();
            saveAlerts();
            assert Boolean.TRUE;
            return;
        }
    }

    public boolean saveAlerts() {
        try {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(PREFS_ALERT_STORAGE, this.flightAlertData.toJSONString());
            editor.apply();
            return true;
        } catch (Exception e) {
            assert Boolean.TRUE;
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alerts);

        this.flightInfoView = (TextView) findViewById(R.id.listFlightInfo);
        this.testResultList = (TextView) findViewById(R.id.testResultList);

        Intent intent = getIntent();

        flifo = (FlightInfo) intent.getSerializableExtra("FLIFOData");

        String flightInfoStr = "";

        flightInfoStr += flifo.carrier + flifo.flightNumber + "  ";
        flightInfoStr += flifo.flightOrig + " to " + flifo.flightDest + "\n";
        flightInfoStr += flifo.longFlightDate + "\n\n";

        flightInfoView.setText(flightInfoStr);

        loadSavedAlerts();


        this.seatList=new ArrayList<String>();

        this.isSearching=false;


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


    public void loadSeatParams() {
        this.activeSeat = new selectSeatParams(this.seatList);

        boolean isWindow = ((CheckBox) findViewById(R.id.checkWindow)).isChecked();
        boolean isAisle = ((CheckBox) findViewById(R.id.checkAisle)).isChecked();
        boolean isBulkhead = ((CheckBox) findViewById(R.id.checkBulkhead)).isChecked();
        boolean notPrem = ((CheckBox) findViewById(R.id.checkNotPrem)).isChecked();
        boolean notEC = ((CheckBox) findViewById(R.id.checkNotEC)).isChecked();
        boolean notBulkhead = ((CheckBox) findViewById(R.id.checkNotBulk)).isChecked();
        boolean notNormal = false; //((CheckBox) findViewById(R.id.checkNotNorm)).isChecked();
        boolean ECOnly = ((CheckBox) findViewById(R.id.checkECOnly)).isChecked();
        boolean anySeat = ((CheckBox) findViewById(R.id.checkAnySeat)).isChecked();

        Spinner classSpin = (Spinner) findViewById(R.id.cabinClass);
        Integer classID = classSpin.getSelectedItemPosition();
        String isClass = "Y";
        if (classID == 1)
            isClass="C";
        else if (classID == 2)
            isClass="F";

        this.activeSeat.isWindow=isWindow;
        this.activeSeat.isAisle=isAisle;
        this.activeSeat.isBulkhead=isBulkhead;
        this.activeSeat.notPrem=notPrem;
        this.activeSeat.notEC=notEC;
        this.activeSeat.notBulkhead=notBulkhead;
        this.activeSeat.notNormal=notNormal;
        this.activeSeat.ECOnly=ECOnly;
        this.activeSeat.flifo=this.flifo;
        this.activeSeat.isClass=isClass;
        this.activeSeat.anySeat=anySeat;
    }


    public void goAddSeat(View view) {
        EditText newSeatText = (EditText) findViewById(R.id.addSeatList);
        String newSeatName = newSeatText.getText().toString();
        newSeatName=newSeatName.toUpperCase();

        if (!this.seatList.contains(newSeatName))
            this.seatList.add(newSeatName);

        Iterator it = this.seatList.iterator();

        String allSeatList = "";

        while (it.hasNext()) {
            String sName = (String) it.next();
            allSeatList += sName;
            if (it.hasNext())
                allSeatList += ",";
        }

        TextView seatListView = (TextView) findViewById(R.id.seatListView);
        seatListView.setText(allSeatList);

    }

    public void goAddAlert(View view) {
        this.loadSeatParams();
        if (this.flightAlertData.addAlert(this.activeSeat) >= 0)
            testResultList.setText("Added Alert");
        else
            testResultList.setText("Duplicate Found, Updated Existing");

        saveAlerts();
    }



    public static String displayTestResults(selectSeatParams testSeatParams, DLSeatInterface testDLSI, AlertData testAlertData) {
        //Integer delIdx = this.flightAlertData.addAlert(this.activeSeat);

        //Display Valid Seats

         Iterator it = testDLSI.seatLayoutMap.entrySet().iterator();

        String retstr = "";

        if (testAlertData.testAlert(testSeatParams,testDLSI))
            retstr += "Seat Already Found!\n\n";
        else
            retstr += "No Available Seats (yet)\n\n";

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Integer rowNumber = (Integer) pair.getKey();
            String layout = (String) pair.getValue();
            String bulkHead = "|";
            String newRow = "|";
            String cabinCode = "Y";
            Boolean hasBulk = false;

            for (int i=0;i<layout.length();i++) {
                String newBulkStr = " ";
                String seatName = rowNumber.toString() + layout.charAt(i);
                if (testDLSI.seatDataMap.containsKey(seatName)) {
                    seatDataClass sdc = testDLSI.seatDataMap.get(seatName);
                    sdc.assignDisplayString();
                    if (testAlertData.testSeat(testSeatParams,sdc,true))
                        newRow += sdc.dispStr;
                    else
                        newRow += '-';
                    if (sdc.noStowage) {
                        newBulkStr = "_";
                        hasBulk = true;
                    }
                    cabinCode=sdc.cabinCode;

                } else if (layout.charAt(i)=='|') {
                    newRow += '|';
                } else {
                    newRow += " ";
                }
                bulkHead += newBulkStr;
            }
            bulkHead += "|\n";
            newRow += "| " + rowNumber.toString() + "("+cabinCode+")\n";

            if (hasBulk) {
                retstr += bulkHead + newRow;
            } else {
                retstr += newRow;
            }
        }


        return retstr;

        //this.flightAlertData.removeAlert(delIdx);


    }

    public void goTestAlert(View view){
        this.loadSeatParams();

        if (DLSI == null  && !isSearching) {
            isSearching=true;
            testResultList.setText("Loading Flight Information....");
            new Thread(new Runnable() {
                public void run() {




                    DLSI = new DLSeatInterface(flifo);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            testResultList.setText("Parsing Flight Information....");
                            isSearching=false;
                            String newMsg = displayTestResults(activeSeat,DLSI,flightAlertData);
                            testResultList.setText(newMsg);
                        }
                    });

                }
            }).start();

        }else if(DLSI != null && !DLSI.successful) {
            testResultList.setText("INVALID FLIGHT INFORMATION!!!");
        }else if(DLSI != null){
            testResultList.setText(displayTestResults(activeSeat,DLSI,flightAlertData));
        }else {//Search Running
            testResultList.setText("Still Loading Flight Information....");
        }


    }

    public void goClearSeatList(View view) {
        this.seatList.removeAll(this.seatList);
        TextView seatListView = (TextView) findViewById(R.id.seatListView);
        seatListView.setText("List Cleared");
    }

}

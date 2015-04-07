package org.zapto.ngnet.dlseatassist;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by adev on 4/6/15.
 */
public class AlertAlarmService extends IntentService {

    //private DLSeatAssistApp app;

    public AlertAlarmService() {
        super("Alert Alarm Service");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        SharedPreferences prefs = getSharedPreferences(Constants.MY_PREFS_NAME, MODE_PRIVATE);
        if (!prefs.getBoolean(Constants.PREFS_SETTINGS_ENABLE_ALERTS,true))
            return;

        Log.d(Constants.TAG, "Alert Alarm Service Activated");
        //this.app = (DLSeatAssistApp) getApplication();

        //Check for alerts
        AlertData flightAlertData = AlertData.loadSavedAlerts(getApplicationContext());
        Integer numAlerts = flightAlertData.getNumAlerts();

        Log.d(Constants.TAG, "Number Active Alarms: " + numAlerts);

        for(int i=0;i<numAlerts;i++) {
            selectSeatParams sSP = flightAlertData.getSSP(i);
            if (flightAlertData.testAlert(i)) {
                Log.d(Constants.TAG, "Alert " + i + " Satisfied");
                //Alert Matches, Handle Notification
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_seat_found)
                        .setContentTitle("Seat Open Alert")
                        .setContentText(sSP.flifo.carrier + sSP.flifo.flightNumber.toString() +" "+ sSP.flifo.flightDate);
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(i, mBuilder.build());

            }
        }

    }

}

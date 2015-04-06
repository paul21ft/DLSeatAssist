package org.zapto.ngnet.dlseatassist;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by adev on 4/6/15.
 */
public class AlertAlarmService extends IntentService {

    private DLSeatAssistApp app;

    public AlertAlarmService() {
        super("Alert Alarm Service");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Log.d(Constants.TAG, "Alert Alarm Service Activated");
        this.app = (DLSeatAssistApp) getApplication();

        //Check for alerts
        AlertData flightAlertData = AlertData.loadSavedAlerts(getApplicationContext());

        Log.d(Constants.TAG, "Number Active Alarms: " + flightAlertData.getNumAlerts());

    }

}

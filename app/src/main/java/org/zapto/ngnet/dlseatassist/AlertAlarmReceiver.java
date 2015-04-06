package org.zapto.ngnet.dlseatassist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by adev on 4/5/15.
 */
public class AlertAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.d(Constants.TAG, "AlertAlarm Receiver Invoked, starting DealService in background");
        context.startService(new Intent(context, AlertAlarmService.class));

        // For our recurring task, we'll just display a message
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

    }
}

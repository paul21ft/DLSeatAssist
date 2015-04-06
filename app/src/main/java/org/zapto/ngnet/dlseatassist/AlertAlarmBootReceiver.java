package org.zapto.ngnet.dlseatassist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by adev on 4/6/15.
 */
public class AlertAlarmBootReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constants.TAG,"Bootup Detected, configuring BG Service");
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, new Intent(context, AlertAlarmReceiver.class), 0);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, Constants.ALARM_TRIGGER_AT_TIME,
                Constants.ALARM_INTERVAL, pendingIntent);

    }
}

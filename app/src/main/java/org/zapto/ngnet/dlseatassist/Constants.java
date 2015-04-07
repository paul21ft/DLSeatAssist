package org.zapto.ngnet.dlseatassist;

import android.app.AlarmManager;
import android.os.SystemClock;

/**
 * Created by adev on 4/6/15.
 */
public class Constants {
    public static final String TAG = MainActivity.class.getSimpleName();

    //Interval to trigger alarm for checking alerts--may not run all alerts then
    //public static final long ALARM_INTERVAL_HOUR = AlarmManager.INTERVAL_HOUR;
    public static final long ALARM_INTERVAL_HOUR = 60000*60;
    public static final long ALARM_INTERVAL_DAY = 3600000*24;
    //public static final long ALARM_INTERVAL = 120000; //Static 2 minutes

    //Waiting 10 seconds to start the first alarm
    public static final long ALARM_TRIGGER_AT_TIME = SystemClock.elapsedRealtime() + 10000;


    //Preference Storage
    public final static String MY_PREFS_NAME = "org.zapto.ngnet.dlseatassist";
    public final static String PREFS_ALERT_STORAGE = "JSONAlerts";
    public final static String PREFS_SETTINGS_ENABLE_ALERTS = "SETTINGS_EnableAlerts";

}

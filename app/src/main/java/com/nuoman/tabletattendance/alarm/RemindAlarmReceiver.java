package com.nuoman.tabletattendance.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 */
public class RemindAlarmReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case NuoManConstant.DOWN_SCREEN_LIGHT:

                AppTools.saveBrightness(AppConfig.getContext(), 30);

                break;
            case NuoManConstant.REBACK_SCREEN_LIGHT:
                AppTools.saveBrightness(AppConfig.getContext(), 255);

                break;

        }
    }


    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     *
     * @param context
     */
    public void setAlarm(Context context, String index, int requestCode, int HOUR_OF_DAY, int MINUTE) {

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RemindAlarmReceiver.class);
        intent.setAction(index);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, MINUTE);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long time = 0l;
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) { //小于当前时间则第二天开始执行
            time = calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY;
        } else {
            time = calendar.getTimeInMillis();
        }

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                time, AlarmManager.INTERVAL_DAY, alarmIntent);

        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, RemindBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Cancels the alarm.
     *
     * @param context
     */
    public void cancelAlarm(Context context, String index, int requestCode) {
        Intent intent = new Intent(context, RemindAlarmReceiver.class);
        intent.setAction(index);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        // If the alarm has been set, cancel it.
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, RemindBootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


}

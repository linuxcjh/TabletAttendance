package com.nuoman.tabletattendance.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.BaseUtil;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 */
public class RemindAlarmReceiver extends BroadcastReceiver {

    public AlarmManager alarmMgr;



    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case NuoManConstant.DOWN_SCREEN_LIGHT:
                Log.d("SYNC", "onReceive   ---  " + BaseUtil.getTime(BaseUtil.HH_MM) + "==" + AppConfig.getStringConfig(NuoManConstant.DOWN_SCREEN_LIGHT, "21:30") + "==" + AppConfig.getStringConfig(NuoManConstant.REBACK_SCREEN_LIGHT, "07:00"));
                if (BaseUtil.getTime(BaseUtil.HH_MM).equals(AppConfig.getStringConfig(NuoManConstant.DOWN_SCREEN_LIGHT, "21:30"))) { //锁屏
                    Log.d("SYNC", "onReceive DOWN_SCREEN_LIGHT  30   ---  " + BaseUtil.getTime(BaseUtil.HH_MM));
                    AppConfig.getActivity().sendBroadcast(new Intent(NuoManConstant.DROP_SCREEN_LIGHT));

                }
                if (BaseUtil.getTime(BaseUtil.HH_MM).equals(AppConfig.getStringConfig(NuoManConstant.REBACK_SCREEN_LIGHT, "07:00"))) {//唤醒
                    Log.d("SYNC", "onReceive REBACK_SCREEN_LIGHT  255   ---  " + BaseUtil.getTime(BaseUtil.HH_MM));
                }

                break;

        }

    }


    /**
     * Sets a repeating alarm that runs once a day at approximately 8:30 a.m. When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     *
     * @param context
     */
    public void setAlarm(Context context, String index, int requestCode) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, RemindAlarmReceiver.class);
        intent.setAction(index);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 1 * 60 * 1000, alarmIntent);

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

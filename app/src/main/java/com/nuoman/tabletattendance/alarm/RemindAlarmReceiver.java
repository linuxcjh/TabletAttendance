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
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.common.utils.BaseUtil;
import com.nuoman.tabletattendance.model.BaseTransModel;

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

                String currentTime = BaseUtil.getTime(BaseUtil.HH_MM);
                String downTime = AppConfig.getStringConfig(NuoManConstant.DOWN_SCREEN_LIGHT, "19:00");
                String downTimeS = downTime.split(":")[0] + ":" + (Integer.parseInt(downTime.split(":")[1]) + 1);


                if (currentTime.equals(downTime) || currentTime.equals(downTimeS)) { //锁屏
                    Log.d("SYNC", "onReceive DOWN_SCREEN_LIGHT  30   ---  " + BaseUtil.getTime(BaseUtil.HH_MM));
                    AppConfig.getActivity().sendBroadcast(new Intent(NuoManConstant.DROP_SCREEN_LIGHT));
                    AppTools.saveBrightness(AppConfig.getContext(), 0);//关闭背光
                }

                String upTime = AppConfig.getStringConfig(NuoManConstant.REBACK_SCREEN_LIGHT, "06:00");
                String upTimeS = upTime.split(":")[0] + ":" + (Integer.parseInt(upTime.split(":")[1]) + 1);
                String upTimeS2 = upTime.split(":")[0] + ":" + (Integer.parseInt(upTime.split(":")[1]) + 2);
                String upTimeS3 = upTime.split(":")[0] + ":" + (Integer.parseInt(upTime.split(":")[1]) + 3);
                String upTimeS4 = upTime.split(":")[0] + ":" + (Integer.parseInt(upTime.split(":")[1]) + 4);
                String upTimeS5 = upTime.split(":")[0] + ":" + (Integer.parseInt(upTime.split(":")[1]) + 5);

                if (currentTime.equals(upTime)
                        || currentTime.equals(upTimeS)
                        || currentTime.equals(upTimeS2)
                        || currentTime.equals(upTimeS3)
                        || currentTime.equals(upTimeS4)
                        || currentTime.equals(upTimeS5)
                        ) {//唤醒
                    Log.d("SYNC", "onReceive REBACK_SCREEN_LIGHT  255   ---  " + BaseUtil.getTime(BaseUtil.HH_MM));
                    AppConfig.getActivity().sendBroadcast(new Intent(NuoManConstant.LIGHT_SCREEN_LIGHT));
                    AppTools.saveBrightness(AppConfig.getContext(), 255);//开启背光
                }

                Log.d("SYNC", "TIME    currentTime:" + currentTime + " downTime:" + downTime + "  downTimeS:" + downTimeS + " upTime:" + upTime + " upTimeS:" + upTimeS);

                //判断是否上传异常信息
                int status = AppConfig.getIntConfig(NuoManConstant.DEVICE_STATUS, 0);
                if (status != 0) {
                    BaseTransModel transModel = new BaseTransModel();
                    transModel.setDvcId(AppTools.getLogInfo().getMachineId());
                    transModel.setStatus(status + "");
                    transModel.setDesc(AppConfig.getStringConfig(NuoManConstant.DEVICE_STATUS_DEC, ""));
                    AppConfig.getActivity().sendBroadcast(new Intent(NuoManConstant.UPLOAD_DEVICE_INFO).putExtra("model", transModel));
                    AppConfig.setIntConfig(NuoManConstant.DEVICE_STATUS, 0);//初始化
                    AppConfig.setStringConfig(NuoManConstant.DEVICE_STATUS_DEC, "");

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
                calendar.getTimeInMillis(), 59 * 1000, alarmIntent);

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

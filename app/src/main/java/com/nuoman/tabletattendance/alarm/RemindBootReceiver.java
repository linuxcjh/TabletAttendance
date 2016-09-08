package com.nuoman.tabletattendance.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nuoman.tabletattendance.SplashActivity;
import com.nuoman.tabletattendance.common.utils.AppTools;


/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
public class RemindBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent startIntent = new Intent(context, SplashActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);

    }
}

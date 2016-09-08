package com.nuoman.tabletattendance.common.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.nuoman.tabletattendance.LockReceiver;

/**
 * AUTHOR: Alex
 * DATE: 8/9/2016 13:20
 */
public class LockScreenUtils {


    public DevicePolicyManager policyManager;

    public ComponentName componentName;

    private static LockScreenUtils instance;

    private LockScreenUtils() {
    }

    public static synchronized LockScreenUtils getInstance() {
        if (instance == null) {
            instance = new LockScreenUtils();
        }
        return instance;
    }

    public void LockScreen(Activity activity) {
        policyManager = (DevicePolicyManager) AppConfig.getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(AppConfig.getContext(), LockReceiver.class);
        if (policyManager.isAdminActive(componentName)) {//判断是否有权限(激活了设备管理器)
            policyManager.lockNow();// 直接锁屏
        } else {
            activeManager(activity);//激活设备管理器获取权限
        }
    }

    /**
     * 判断是否已注册启动器
     * @return
     */
    public boolean isRegistered() {
        policyManager = (DevicePolicyManager) AppConfig.getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(AppConfig.getContext(), LockReceiver.class);
        if (policyManager.isAdminActive(componentName)) {//判断是否有权限(激活了设备管理器)
            return true;
        }

        return false;
    }

    // 解除绑定
    public void Bind(Activity activity) {
        if (componentName != null) {
            policyManager.removeActiveAdmin(componentName);
            activeManager(activity);
        }
    }


    public void activeManager(Activity activity) {
        //使用隐式意图调用系统方法来激活指定的设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏");
        activity.startActivity(intent);
    }

    /**
     * 唤醒屏幕
     *
     * @param context
     */
    public void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }
}




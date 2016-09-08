package com.nuoman.tabletattendance.common.utils;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

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

}




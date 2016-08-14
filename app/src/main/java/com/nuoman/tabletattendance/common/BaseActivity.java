package com.nuoman.tabletattendance.common;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * AUTHOR: Alex
 * DATE: 21/10/2015 18:55
 */
public class BaseActivity extends FragmentActivity  {

    public static List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(0, this);
    }



    /**
     * app 字体不受系统字体大小影响
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }



}

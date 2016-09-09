package com.nuoman.tabletattendance.common;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * AUTHOR: Alex
 * DATE: 21/10/2015 18:55
 */
public class BaseActivity extends FragmentActivity {
    public static Handler sHandler;


    public static List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        hideBar();
        activityList.add(0, this);
    }


    /**
     * 隐藏导航栏
     */
    private void hideBar() {
        sHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x222:
//                        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                | View.SYSTEM_UI_FLAG_IMMERSIVE
//                                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//
//                        getWindow().getDecorView().setSystemUiVisibility(flags);
                        Log.d("SYNC", "handleMessage");
                        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                            @Override
                            public void onSystemUiVisibilityChange(int visibility) {

                            }
                        });
                        sHandler.sendEmptyMessageDelayed(0x222, 1000);
                        break;

                }

            }
        };

        sHandler.sendEmptyMessage(0x222); // hide the navigation bar

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
    protected void onResume() {
        super.onResume();
        sHandler.sendEmptyMessageDelayed(0x222, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sHandler.removeMessages(0x222);
        activityList.remove(this);
    }

}

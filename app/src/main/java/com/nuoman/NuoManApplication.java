package com.nuoman;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nuoman.tabletattendance.common.utils.AppConfig;

import im.fir.sdk.FIR;

/**
 * Created by chen on 21/10/2015.
 */
public class NuoManApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FIR.init(this);
        AppConfig.setContext(this);
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=5768e5d9");//初始化讯飞语音

    }


}

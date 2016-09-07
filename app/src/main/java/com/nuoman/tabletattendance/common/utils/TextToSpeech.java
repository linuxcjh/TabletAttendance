package com.nuoman.tabletattendance.common.utils;

import android.app.Activity;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;


/**
 * 文字转语音，采用单例模式
 * Created by 杨洋 on 2016/4/15.
 */
public class TextToSpeech {
    private static TextToSpeech textToSpeech;
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
                // 设置本地合成发音人 voicer为空，默认通过语音+界面指定发音人。
                mTts.setParameter(SpeechConstant.VOICE_NAME, "");

                // 设置播放合成音频打断音乐播放，默认为true
                mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
            }
        }
    };
    private SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(AppConfig.getContext(), mTtsInitListener);
    private ApkInstaller mInstaller;

    private TextToSpeech(Activity activity) {

        //检测是否安装语音助手，如果没有安装提示安装
        mInstaller = new ApkInstaller(activity);

        if (!SpeechUtility.getUtility().checkServiceInstalled())
            mInstaller.install();

    }

    public static TextToSpeech getInstance(Activity activity) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(activity);
        }
        return textToSpeech;
    }

    public void startSpeaking(String text) {
        int codes = mTts.startSpeaking(text, null);
        if (codes != ErrorCode.SUCCESS) {
            if (codes == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED)
                mInstaller.install();
            else {
                Toast.makeText(AppConfig.getContext(), "语音合成失败,错误码: " + codes, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

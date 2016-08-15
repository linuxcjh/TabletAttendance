package com.nuoman.tabletattendance.voice;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SoundRecorder {


    MediaRecorder mRecorder;

    public List<String> paths = new ArrayList<>();

    public String currentPath;

    public void startRecording() {
        currentPath = newFileName();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(currentPath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();

    }

    public void stopRecording() {
        paths.add(currentPath);
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public String newFileName() {

        String mFileName = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

        String s = new SimpleDateFormat("yyyy-MM-dd hhmmss")
                .format(new Date());
        return mFileName += "/rcd_" + s + ".3gp";
    }
}


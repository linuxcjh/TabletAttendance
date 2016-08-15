package com.nuoman.tabletattendance.voice;

import android.media.MediaPlayer;

import java.io.IOException;


public class SoundPlayer {

    MediaPlayer mPlayer;


    public void startPlaying(String fileName) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }
}
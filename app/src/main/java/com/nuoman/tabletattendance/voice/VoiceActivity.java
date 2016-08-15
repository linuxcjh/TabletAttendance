package com.nuoman.tabletattendance.voice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VoiceActivity extends BaseActivity {

    @Bind(R.id.record_start_bt)
    Button recordStartBt;
    @Bind(R.id.record_end_bt)
    Button recordEndBt;
    @Bind(R.id.play_start_bt)
    Button playStartBt;
    @Bind(R.id.play_end_bt)
    Button playEndBt;

    SoundPlayer player = new SoundPlayer();
    SoundRecorder recorder = new SoundRecorder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_voice_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.record_start_bt, R.id.record_end_bt, R.id.play_start_bt, R.id.play_end_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_start_bt:
                Toast.makeText(VoiceActivity.this, "start record", Toast.LENGTH_SHORT).show();
                recorder.startRecording();
                break;
            case R.id.record_end_bt:
                Toast.makeText(VoiceActivity.this, "stop record", Toast.LENGTH_SHORT).show();
                recorder.stopRecording();
                break;
            case R.id.play_start_bt:
                Toast.makeText(VoiceActivity.this, "start play", Toast.LENGTH_SHORT).show();
                player.startPlaying(recorder.paths.get(0));
                break;
            case R.id.play_end_bt:
                Toast.makeText(VoiceActivity.this, "stop play", Toast.LENGTH_SHORT).show();
                player.stopPlaying();
                break;
        }
    }
}
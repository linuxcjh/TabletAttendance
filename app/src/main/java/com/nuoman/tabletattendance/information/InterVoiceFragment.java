package com.nuoman.tabletattendance.information;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.voice.view.MediaManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 语音对话
 * Created by Alex on 2016/5/17.
 */
public class InterVoiceFragment extends Fragment implements ICommonAction ,AdapterView.OnItemClickListener{


    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.recordButton)
    AudioRecordLayout recordButton;

    private ArrayAdapter<Recorder> mAdapter;
    private View viewanim;
    private List<Recorder> mDatas = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_inter_voice_main, container,false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView(){
        recordButton.setAudioFinishRecorderListener(new AudioRecordLayout.AudioFinishRecorderListener() {

            @Override
            public void onFinished(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds, filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                listview.setSelection(mDatas.size() - 1);
            }
        });

        mAdapter = new RecorderVoiceAdapter(getActivity(), mDatas);
        listview.setAdapter(mAdapter);

        listview.setOnItemClickListener(this);

    }


    @Override
    public void obtainData(Object data, String methodIndex, int status) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 播放动画
        if (viewanim != null) {//让第二个播放的时候第一个停止播放
            viewanim.setBackgroundResource(R.drawable.adj);
            viewanim = null;
        }
        viewanim = view.findViewById(R.id.id_recorder_anim);
        viewanim.setBackgroundResource(R.drawable.play);
        AnimationDrawable drawable = (AnimationDrawable) viewanim
                .getBackground();
        drawable.start();

        // 播放音频
        MediaManager.playSound(mDatas.get(position).filePathString,
                new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        viewanim.setBackgroundResource(R.drawable.v_anim3);

                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

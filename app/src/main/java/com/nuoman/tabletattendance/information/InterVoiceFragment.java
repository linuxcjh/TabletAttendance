package com.nuoman.tabletattendance.information;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.StudentInfos;
import com.nuoman.tabletattendance.model.TeacherInfos;
import com.nuoman.tabletattendance.model.VoiceReceiveModel;
import com.nuoman.tabletattendance.model.VoiceTransModel;
import com.nuoman.tabletattendance.voice.view.MediaManager;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 语音对话
 * Created by Alex on 2016/5/17.
 */
public class InterVoiceFragment extends Fragment implements ICommonAction, AdapterView.OnItemClickListener, IDialogViewManager {

 /*-----------------------------------------------录音控件-------------------------------------------------------*/

    private ImageView mIcon;
    private ImageView mVoice;

    private TextView mLable;

    public View view;
 /*-------------------------------------------------------------------------------------------------------------*/

    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.recordButton)
    public AudioRecordViewLayout recordButton;
    @Bind(R.id.container_layout)
    LinearLayout containerLayout;

    private ArrayAdapter<VoiceReceiveModel.VoiceItemModel> mAdapter;
    private View viewanim;
    private List<VoiceReceiveModel.VoiceItemModel> mDatas = new ArrayList<>();
    private CommonPresenter commonPresenter = new CommonPresenter(this);
    private VoiceTransModel model = new VoiceTransModel();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_inter_voice_main, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    /**
     * 设置学生信息
     *
     * @param info
     */
    public void setSendStudentInfo(StudentInfos info) {
        this.model.setUserId(info.getStudentId());
        this.model.setType("v");
        this.model.setKind("0");
    }

    /**
     * 学生群发ID
     *
     * @param groupId
     */
    public void setSendStudentGroupId(String groupId) {
        this.model.setDestId(groupId);
    }


    /**
     * 设置老师信息
     *
     * @param info
     */
    public void setSendTeacherInfo(TeacherInfos info) {
        this.model.setUserId(info.getTeacherId());
        this.model.setType("v");
        this.model.setKind("2");
    }

    /**
     * 老师发送默认班级
     *
     * @param classId
     */
    public void setSendTeacherClassId(String classId) {
        this.model.setDestId(classId);
    }

    private void initView() {
        recordButton.setDialogManager(this);
        recordButton.setAudioFinishRecorderListener(new AudioRecordViewLayout.AudioFinishRecorderListener() {

            @Override
            public void onFinished(float seconds, String filePath) {

                if (!TextUtils.isEmpty(AppTools.getAcacheData(NuoManConstant.TOKEN))) {
                    uploadImageToQiNiu(seconds, filePath, AppTools.getAcacheData(NuoManConstant.TOKEN));
                } else { //重新请求token
                    commonPresenter.invokeInterfaceObtainData(false, "qiniuCtrl", NuoManService.GETTOKEN, null, new TypeToken<BaseReceivedModel>() {
                    });
                }
//                Recorder recorder = new Recorder(seconds, filePath);
//                mDatas.add(recorder);
//                mAdapter.notifyDataSetChanged();
//                listview.setSelection(mDatas.size() - 1);
            }
        });

        mAdapter = new RecorderVoiceAdapter(getActivity(), mDatas);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(this);

    }


    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {

        switch (methodIndex) {
            case NuoManService.GETTOKEN:
                try {
                    if (data != null) {
                        BaseReceivedModel model = (BaseReceivedModel) data;
                         AppTools.acachePut(NuoManConstant.TOKEN,model.getToken());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case NuoManService.SAVEVOICEMSG:
                try {
                    if (data != null) {

                        List<VoiceReceiveModel> m = (List<VoiceReceiveModel>) data;
                        mDatas.clear();
                        mDatas.addAll(m.get(0).getObj());
                        mAdapter.notifyDataSetChanged();
                        listview.setSelection(mDatas.size() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }

    /**
     * 上传音频文件
     *
     * @param filePath
     * @param token
     */
    private void uploadImageToQiNiu(final float seconds, String filePath, String token) {

        try {
            UploadManager uploadManager = new UploadManager();
            File file = new File(filePath);
            if (file.exists()) {

                uploadManager.put(filePath, file.getName(), token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        model.setAudioFile(key);
                        model.setTime(seconds);
                        commonPresenter.invokeInterfaceObtainData(true, "voiceCtrl", NuoManService.SAVEVOICEMSG, model, new TypeToken<List<VoiceReceiveModel>>() {
                        });

                        Log.d("NuoMan", "key: " + key + "\n");
                    }
                }, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        // 播放动画
        if (viewanim != null) {//让第二个播放的时候第一个停止播放
            viewanim.setBackgroundResource(R.drawable.adj);
            viewanim = null;
        }

        if(mDatas.get(position).getKind().equals("0")){
            viewanim = view.findViewById(R.id.id_recorder_anim);
            viewanim.setBackgroundResource(R.drawable.play);
        }else{
            viewanim = view.findViewById(R.id.id_recorder_anim_2);
            viewanim.setBackgroundResource(R.drawable.playleft);
        }

        AnimationDrawable drawable = (AnimationDrawable) viewanim
                .getBackground();
        drawable.start();

        // 播放音频
        MediaManager.playSound(mDatas.get(position).getAudioFile(),
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

    /*-----------------------------------------------录音控制--------------------------------------------------------*/

    @Override
    public void showRecordingDialog() {
        try {
            containerLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            view = inflater.inflate(R.layout.dialog_manager, null);
            mIcon = (ImageView) view.findViewById(R.id.dialog_icon);
            mVoice = (ImageView) view.findViewById(R.id.dialog_voice);
            mLable = (TextView) view.findViewById(R.id.recorder_dialogtext);

            containerLayout.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void recording() {
        try {
            if (view != null && (view.getVisibility() == View.VISIBLE)) {
                mIcon.setVisibility(View.VISIBLE);
                mVoice.setVisibility(View.VISIBLE);
                mLable.setVisibility(View.VISIBLE);

                mIcon.setImageResource(R.drawable.recorder);
                mLable.setText(R.string.shouzhishanghua);
                mLable.setBackgroundColor(Color.TRANSPARENT);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void wantToCancel() {
        try {
            if (view != null && (view.getVisibility() == View.VISIBLE)) {
                mIcon.setVisibility(View.VISIBLE);
                mVoice.setVisibility(View.GONE);
                mLable.setVisibility(View.VISIBLE);

                mIcon.setImageResource(R.drawable.cancel);
                mLable.setText(R.string.want_to_cancle);
                mLable.setBackgroundColor(Color.RED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void tooShort() {
        try {
            if (view != null && (view.getVisibility() == View.VISIBLE)) {
                mIcon.setVisibility(View.VISIBLE);
                mVoice.setVisibility(View.GONE);
                mLable.setVisibility(View.VISIBLE);

                mIcon.setImageResource(R.drawable.voice_to_short);
                mLable.setText(R.string.tooshort);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dimissDialog() {
        try {
            containerLayout.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void updateVoiceLevel(int level) {
        if (view != null && (view.getVisibility() == View.VISIBLE)) {

            //先不改变它的默认状态
//			mIcon.setVisibility(View.VISIBLE);
//			mVoice.setVisibility(View.VISIBLE);
//			mLable.setVisibility(View.VISIBLE);

            try {
                //通过level来找到图片的id，也可以用switch来寻址，但是代码可能会比较长
                int resId = getActivity().getResources().getIdentifier("v" + level,
                        "drawable", getActivity().getPackageName());

                mVoice.setImageResource(resId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     /*-------------------------------------------------------------------------------------------------------------*/

}

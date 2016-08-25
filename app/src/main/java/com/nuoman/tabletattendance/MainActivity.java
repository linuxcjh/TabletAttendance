package com.nuoman.tabletattendance;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.common.utils.BaseUtil;
import com.nuoman.tabletattendance.information.InformationActivity;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;
import com.nuoman.tabletattendance.model.ReceivedWeatherModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements ICommonAction, CameraFragment.ObtainPictureListener {


    //当前时间
    private static final int CURRENT_TIME_INDEX = 0;

    //打卡成功后回退
    private static final int BACK_INDEX = 1;

    //定时刷新时间
    private static final int REFRESH_TIME_INDEX = 1000;

    //打卡成功后延迟回到主页面
    private static final int REBACK_TIME_INDEX = 2000;


    @Bind(R.id.h_time_tv)
    TextView hTimeTv;
    @Bind(R.id.h_single_tv)
    ImageView hSingleTv;
    @Bind(R.id.h_school_name_tv)
    TextView hSchoolNameTv;
    @Bind(R.id.h_slogan_tv)
    TextView hSloganTv;
    @Bind(R.id.h_weather_iv)
    ImageView hWeatherIv;
    @Bind(R.id.h_weather_detail_tv)
    TextView hWeatherDetailTv;
    @Bind(R.id.h_homework_iv)
    ImageView hHomeworkIv;
    @Bind(R.id.h_info_iv)
    ImageView hInfoIv;
    @Bind(R.id.h_voice_iv)
    ImageView hVoiceIv;
    @Bind(R.id.h_no_card_iv)
    ImageView hNoCardIv;
    @Bind(R.id.edit_input_et)
    EditText editInputEt;
    @Bind(R.id.h_punch_card_success_tv)
    TextView hPunchCardSuccessTv;
    @Bind(R.id.h_title_tv)
    TextView hTitleTv;
    @Bind(R.id.h_operation_layout)
    RelativeLayout hOperationLayout;
    @Bind(R.id.h_image_iv)
    ImageView hImageIv;
    @Bind(R.id.weather_operation_layout)
    LinearLayout weatherOperationLayout;
    @Bind(R.id.no_card_et)
    EditText noCardEt;
    @Bind(R.id.keyboard_kbv)
    KeyboardView keyboardKbv;
    @Bind(R.id.no_card_layout)
    LinearLayout noCardLayout;
    private CommonPresenter commonPresenter;

    private BaseTransModel transModel = new BaseTransModel();

    private CameraFragment cameraFragment;
    LoginInfoModel logInfo = AppTools.getLogInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        ButterKnife.bind(this);

        initView();
        initData();

    }

    /**
     * 获取天气
     */
    private void initData() {

        commonPresenter = new CommonPresenter(this);
        transModel.setArea(AppTools.getLogInfo().getAreaId());
        commonPresenter.invokeInterfaceObtainData(false, "weatherCtrl", NuoManService.GETWEATHERFORONEDAY, transModel, new TypeToken<ReceivedWeatherModel>() {
        });

    }

    /**
     * View 初始化
     */
    private void initView() {

        cameraFragment = (CameraFragment) getSupportFragmentManager().findFragmentById(R.id.camera_fragment);
        hSchoolNameTv.setText(logInfo.getSchoolName());
        hTimeTv.setText(BaseUtil.getTime(BaseUtil.YYYY_MM_DD_HH_MM) + "   " + BaseUtil.getTime(BaseUtil.HH_MM_SS) + "    " + BaseUtil.getDayOfWeek());
        mHandler.sendEmptyMessageDelayed(CURRENT_TIME_INDEX, REFRESH_TIME_INDEX);

        editInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mHandler.removeMessages(BACK_INDEX);
                Toast.makeText(AppConfig.getContext(), v.getText().toString(), Toast.LENGTH_SHORT).show();
                AppConfig.setStringConfig(NuoManConstant.CARD_ID, v.getText().toString().replace("\n", ""));
                cameraFragment.takePicture();
                editInputEt.setText("");
                return false;
            }
        });
        keyboardKbv.setKeyboard(new Keyboard(this, R.xml.symbols));
        keyboardKbv.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {

            }

            @Override
            public void onRelease(int primaryCode) {


            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Editable mEditable = noCardEt.getText();
                int start = noCardEt.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_DONE) {//TODO 需要验证卡号是否有效
                    mHandler.removeMessages(BACK_INDEX);
                    Toast.makeText(AppConfig.getContext(), noCardEt.getText().toString(), Toast.LENGTH_SHORT).show();
                    AppConfig.setStringConfig(NuoManConstant.CARD_ID, noCardEt.getText().toString());
                    cameraFragment.takePicture();

                } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (mEditable != null && mEditable.length() > 0) {
                        if (start > 0) {
                            mEditable.delete(start - 1, start);
                        }
                    }
                } else {
                    mEditable.insert(start, Character.toString((char) primaryCode));
                }
            }

            @Override
            public void onText(CharSequence text) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });
    }


    /**
     * 上传打卡信息
     *
     * @param pictureId
     */
    @Override
    public void onChangeArticle(String pictureId) {
        transModel.setMachineNo(logInfo.getMachineId());
        transModel.setMachineId(logInfo.getMachineId());
        transModel.setTel(AppConfig.getStringConfig(NuoManConstant.USER_NAME, ""));
        transModel.setCardNo(AppConfig.getStringConfig(NuoManConstant.CARD_ID, ""));
        transModel.setAttDate(BaseUtil.getTime(BaseUtil.YYYY_MM_DD_HH_MM_SS));
        transModel.setAttPicUrl(pictureId);
        commonPresenter.invokeInterfaceObtainData(true, "attDataCtrl", NuoManService.WRITEATTLOG, transModel, new TypeToken<BaseReceivedModel>() {
        });
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {

        switch (methodIndex) {
            case NuoManService.GETWEATHERFORONEDAY:
                ReceivedWeatherModel model = (ReceivedWeatherModel) data;
                hWeatherDetailTv.setText(model.getTime());

                break;
            case NuoManService.WRITEATTLOG:
                BaseReceivedModel m = (BaseReceivedModel) data;
                if (m.isSuccess()) {
                    Toast.makeText(this, "打卡成功", Toast.LENGTH_SHORT).show();
                    punchCardSuccess();
                }

                break;
        }
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CURRENT_TIME_INDEX:
                    hTimeTv.setText(BaseUtil.getTime(BaseUtil.YYYY_MM_DD_HH_MM) + "   " + BaseUtil.getTime(BaseUtil.HH_MM_SS) + "    " + BaseUtil.getDayOfWeek());
                    mHandler.sendEmptyMessageDelayed(CURRENT_TIME_INDEX, REFRESH_TIME_INDEX);
                    break;
                case BACK_INDEX://返回主页
                    hSloganTv.setVisibility(View.VISIBLE);
                    hOperationLayout.setVisibility(View.VISIBLE);
                    hTitleTv.setVisibility(View.GONE);
                    hImageIv.setVisibility(View.GONE);
                    hPunchCardSuccessTv.setVisibility(View.GONE);
                    weatherOperationLayout.setVisibility(View.VISIBLE);
                    noCardLayout.setVisibility(View.GONE);

                    break;
            }

        }
    };

    @OnClick({R.id.h_homework_iv, R.id.h_info_iv, R.id.h_voice_iv, R.id.h_no_card_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.h_homework_iv:
                break;
            case R.id.h_info_iv:
                startActivity(new Intent(this, InformationActivity.class));
                break;
            case R.id.h_voice_iv:
                break;
            case R.id.h_no_card_iv:
                noCardEt.setText("");
                weatherOperationLayout.setVisibility(View.GONE);
                noCardLayout.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * 打卡成功
     */
    private void punchCardSuccess() {

        String info = "年      级：" + AppConfig.getStringConfig(NuoManConstant.GRADE_NAME, "") + System.getProperty("line.separator")
                + "班      级：" + AppConfig.getStringConfig(NuoManConstant.CLASS_NAME, "") + System.getProperty("line.separator")
                + "监 护 人：" + "" + System.getProperty("line.separator")
                + "卡      号：" + AppConfig.getStringConfig(NuoManConstant.CARD_ID, "") + System.getProperty("line.separator")
                + "签到时间：" + BaseUtil.getTime("HH:mm:ss");

        weatherOperationLayout.setVisibility(View.VISIBLE);
        noCardLayout.setVisibility(View.GONE);
        hSloganTv.setVisibility(View.GONE);
        hOperationLayout.setVisibility(View.GONE);
        hTitleTv.setVisibility(View.VISIBLE);
        hImageIv.setVisibility(View.VISIBLE);
        Glide.with(this).load(cameraFragment.filePath).into(hImageIv);
        hPunchCardSuccessTv.setVisibility(View.VISIBLE);
        hPunchCardSuccessTv.setText(info);

        mHandler.sendEmptyMessageDelayed(BACK_INDEX, REBACK_TIME_INDEX);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(CURRENT_TIME_INDEX);
    }
}

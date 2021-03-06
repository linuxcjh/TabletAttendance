package com.nuoman.tabletattendance;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nuoman.syncadapter.pro.NoteProviderMetaData;
import com.nuoman.tabletattendance.alarm.RemindAlarmReceiver;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.common.utils.BaseUtil;
import com.nuoman.tabletattendance.common.utils.BrightnessTools;
import com.nuoman.tabletattendance.common.utils.LockScreenUtils;
import com.nuoman.tabletattendance.common.utils.TextToSpeech;
import com.nuoman.tabletattendance.common.utils.Utils;
import com.nuoman.tabletattendance.information.InformationActivity;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.CardNoModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;
import com.nuoman.tabletattendance.model.ReceivedWeatherModel;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements ICommonAction, CameraFragment.ObtainPictureListener, NetReceiver.NetEventHandle {


    /* ------Sync START ------ */
    public static final String ACCOUNT_TYPE = "example.com";

    public static final String ACCOUNT = "dummyaccount";


    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 1L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    @Bind(R.id.h_attendance_bt)
    Button hAttendanceBt;
    @Bind(R.id.h_temperature_iv)
    ImageView hTemperatureIv;

    private Account mAccount;

    /* ------Sync END ------ */


    //当前时间
    private static final int CURRENT_TIME_INDEX = 0;

    //打卡成功后回退
    private static final int BACK_INDEX = 1;

    //定时刷新时间
    private static final int REFRESH_TIME_INDEX = 1000;

    //打卡成功后延迟回到主页面
    private static final int REBACK_TIME_INDEX = 2000;

    //设置页面返回
    private static final int SET_REBACK_INDEX = 0x110;


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

    private TextToSpeech textToSpeech;
    private CommonPresenter commonPresenter;


    private CameraFragment cameraFragment;
    private LoginInfoModel logInfo = AppTools.getLogInfo();

    private ScreenLightReceiver screenLightReceiver;
    private RemindAlarmReceiver alarm = new RemindAlarmReceiver();


    private boolean isTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppConfig.setActivity(this);
        setInitAlert();
        NetReceiver.ehList.add(this);//注册网络状态变化监听
        setContentView(R.layout.activity_home_layout);
        ButterKnife.bind(this);
        registerBroadReceiver();
        setNetWorkStatus();
        initSyncAdapter();
        initView();
        initData();

    }

    /**
     * 设置屏幕亮度定时任务
     */
    private void setInitAlert() {
        alarm.setAlarm(AppConfig.getContext(), NuoManConstant.DOWN_SCREEN_LIGHT, 1);
    }

    /**
     * 取消闹钟
     */
    private void cancelAlert() {
        alarm.cancelAlarm(AppConfig.getContext(), NuoManConstant.DOWN_SCREEN_LIGHT, 1);

    }

    /**
     * 广播注册
     */
    private void registerBroadReceiver() {
        screenLightReceiver = new ScreenLightReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NuoManConstant.DROP_SCREEN_LIGHT);
        filter.addAction(NuoManConstant.LIGHT_SCREEN_LIGHT);
        registerReceiver(screenLightReceiver, filter);
    }

    /**
     * 同步适配器
     */
    private void initSyncAdapter() {
        mAccount = CreateSyncAccount(this);
        ContentResolver.setSyncAutomatically(mAccount, NoteProviderMetaData.AUTHORITY, true);
        ContentResolver.addPeriodicSync(mAccount, NoteProviderMetaData.AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        requestSync();
    }

    /**
     * 获取天气
     */
    private void initData() {
        BaseTransModel transModel = new BaseTransModel();
        commonPresenter = new CommonPresenter(this);
        transModel.setArea(AppTools.getLogInfo().getAreaId());
        commonPresenter.invokeInterfaceObtainData(false, "qiniuCtrl", NuoManService.GETTOKEN, null, new TypeToken<BaseReceivedModel>() {
        });
        commonPresenter.invokeInterfaceObtainData(false, "weatherCtrl", NuoManService.GETWEATHERFORONEDAY, transModel, new TypeToken<ReceivedWeatherModel>() {
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseTransModel transModel = new BaseTransModel();
        transModel.setClassId(AppConfig.getStringConfig(NuoManConstant.CLASS_ID, ""));
        transModel.setMachineId(AppTools.getLogInfo().getMachineId());
        commonPresenter.invokeInterfaceObtainData(false, "loginCtrl", NuoManService.SAVEDVCLOCATION, transModel, new TypeToken<BaseTransModel>() {
        });
    }


    /**
     * View 初始化
     */
    private void initView() {
        textToSpeech = TextToSpeech.getInstance(this);
        cameraFragment = (CameraFragment) getSupportFragmentManager().findFragmentById(R.id.camera_fragment);
        hSchoolNameTv.setText(AppConfig.getStringConfig(NuoManConstant.SCHOOL_NAME, AppTools.getLogInfo().getSchoolName()));
        hTimeTv.setText(BaseUtil.getTime(BaseUtil.YYYY_MM_DD_HH_MM) + "   " + BaseUtil.getTime(BaseUtil.HH_MM_SS) + "    " + BaseUtil.getDayOfWeek());
        mHandler.sendEmptyMessageDelayed(CURRENT_TIME_INDEX, REFRESH_TIME_INDEX);

        editInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                mHandler.removeMessages(BACK_INDEX);

                if (BrightnessTools.getScreenBrightness(MainActivity.this) != 255) { //打卡时提升屏幕亮度
                    AppTools.saveBrightness(MainActivity.this, 255);
                }
                AppConfig.setStringConfig(NuoManConstant.CARD_ID, v.getText().toString().replace("\n", ""));
                editInputEt.setText("");
                if (!TextUtils.isEmpty(obtainCardInfo())) {//判断输入的卡号是否有效
                    commonPresenter.invokeInterfaceObtainData(false, "qiniuCtrl", NuoManService.GETTOKEN, null, new TypeToken<BaseReceivedModel>() {
                    });
                    cameraFragment.takePicture();

                } else {
                    setIniMainPage();
                    textToSpeech.startSpeaking("卡号不存在");
                    Toast.makeText(MainActivity.this, "卡号不存在", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        keyboardKbv.setPreviewEnabled(false);
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
                if (primaryCode == Keyboard.KEYCODE_DONE) {
                    mHandler.removeMessages(BACK_INDEX);


                    String pwd = AppTools.getLogInfo().getSuperPass();
                    if (TextUtils.isEmpty(pwd)) {
                        pwd = "666";
                    }
                    if (noCardEt.getText().toString().equals(pwd)) { //进设置页面

                        setIniMainPage();
                        startActivityForResult(new Intent(MainActivity.this, SetInfoActivity.class), SET_REBACK_INDEX);

                    } else {
                        AppConfig.setStringConfig(NuoManConstant.CARD_ID, noCardEt.getText().toString().replace("\n", ""));
                        if (!TextUtils.isEmpty(obtainCardInfo())) {//判断输入的卡号是否有效
                            commonPresenter.invokeInterfaceObtainData(false, "qiniuCtrl", NuoManService.GETTOKEN, null, new TypeToken<BaseReceivedModel>() {
                            });
                            cameraFragment.takePicture();

                        } else {
                            setIniMainPage();
                            textToSpeech.startSpeaking("卡号不存在");
                            Toast.makeText(MainActivity.this, "卡号不存在", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (mEditable != null && mEditable.length() > 0) {
                        if (start > 0) {
                            mEditable.delete(start - 1, start);
                        }
                    } else {
                        mHandler.removeMessages(BACK_INDEX);
                        setIniMainPage();
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
     * 设置当前网络状态
     */
    private void setNetWorkStatus() {

        netState(new NetReceiver().isConnected(this));

    }

    /**
     * 网络状态变化
     */
    @Override
    public void netState(NetReceiver.NetState netCode) {

        switch (netCode) {
            case NET_NO:
                hSingleTv.setImageResource(R.drawable.no_wifi);
                Toast.makeText(this, "没有网络连接", Toast.LENGTH_SHORT).show();
                break;
            case NET_4G:
                hSingleTv.setImageResource(R.drawable.g4_01_03);
                Toast.makeText(this, "4g网络", Toast.LENGTH_SHORT).show();
                break;
            case NET_WIFI:
                hSingleTv.setImageResource(R.drawable.connected_wifi);
                Toast.makeText(this, "WIFI网络", Toast.LENGTH_SHORT).show();
                break;
            case NET_UNKNOWN:
                hSingleTv.setImageResource(R.drawable.bendi_01_03);
                break;
            default:
                hSingleTv.setImageResource(R.drawable.no_wifi);
        }

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            if (!punchCardSuccess()) {
                return;
            }
            hImageIv.setVisibility(View.VISIBLE);
            hImageIv.setImageBitmap(bm);
        } else {
            Toast.makeText(this, "请重新刷卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 上传图片
     *
     * @param filePath
     */
    @Override
    public void onChangeArticle(String filePath) {

        if (!TextUtils.isEmpty(filePath)) {
            hImageIv.setImageBitmap(BitmapFactory.decodeFile(filePath));
        }

        if (Utils.checkNetworkConnection()) {//有网直接上传
            if (!TextUtils.isEmpty(AppTools.getAcacheData(NuoManConstant.TOKEN))) {
                uploadImageToQiNiu(filePath, AppTools.getAcacheData(NuoManConstant.TOKEN));
            }

        } else { //没网存在本地

            BaseTransModel m = new BaseTransModel();
            m.setCardNo(AppConfig.getStringConfig(NuoManConstant.CARD_ID, ""));
            m.setAttDate(BaseUtil.getTime(BaseUtil.YYYY_MM_DD_HH_MM_SS));
            m.setImagePath(filePath);
            insert(m);
        }
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {

        switch (methodIndex) {
            case NuoManService.GETTOKEN:
                if (data != null) {
                    BaseReceivedModel model = (BaseReceivedModel) data;
                    AppTools.acachePut(NuoManConstant.TOKEN, model.getToken());

                }
                break;
            case NuoManService.GETWEATHERFORONEDAY:
                if (data != null) {
                    ReceivedWeatherModel model = (ReceivedWeatherModel) data;
                    hWeatherDetailTv.setText(model.getArea() + "\n" + model.getWeatherTemp() + "\n" + model.getTime());

                    if (!TextUtils.isEmpty(model.getWeatherShape())) {
                        hWeatherIv.setImageDrawable(AppTools.loadImageFromAsserts(this, "WeatherIcon/Day/" + model.getWeatherShape() + ".png"));
                    }

                }

                break;
            case NuoManService.WRITEATTLOG:

                BaseTransModel mTrans = new BaseTransModel();
                for (String key : parameterMap.keySet()) {
                    if (key.equals("cardNo")) {
                        mTrans.setCardNo(parameterMap.get(key));
                    } else if (key.equals("imagePath")) {
                        mTrans.setImagePath(parameterMap.get(key));
                    } else if (key.equals("attDate")) {
                        mTrans.setAttDate(parameterMap.get(key));
                    }
                }
                if (data != null) {
                    BaseReceivedModel m = (BaseReceivedModel) data;
                    if (m.isSuccess()) {
                        Toast.makeText(this, "打卡成功", Toast.LENGTH_SHORT).show();

                    } else { //保存未成功上传的打卡信息
                        insert(mTrans);
                    }
                } else {
                    insert(mTrans);
                }

                break;
        }
    }

    /**
     * 保存未提交的打卡
     */
    private void insert(BaseTransModel transModel) {

        ContentValues values = new ContentValues();
        values.put(NoteProviderMetaData.NoteTableMetaData.PUNCH_CARD_NO, transModel.getCardNo());
        values.put(NoteProviderMetaData.NoteTableMetaData.PUNCH_TIME, transModel.getAttDate());
        values.put(NoteProviderMetaData.NoteTableMetaData.PUNCH_IMAGE_PATH, transModel.getImagePath());
        Uri uri = this.getContentResolver().insert(NoteProviderMetaData.NoteTableMetaData.CONTENT_URI, values);

        Log.d("SYNC", uri.toString());

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
                    setIniMainPage();

                    break;
            }

        }
    };

    /**
     * 初始化主页
     */
    private void setIniMainPage() {
        hSloganTv.setVisibility(View.VISIBLE);
        hOperationLayout.setVisibility(View.VISIBLE);
        hTitleTv.setVisibility(View.GONE);
        hImageIv.setVisibility(View.GONE);
        hPunchCardSuccessTv.setVisibility(View.GONE);
        weatherOperationLayout.setVisibility(View.VISIBLE);
        noCardLayout.setVisibility(View.GONE);

    }

    @OnClick({R.id.h_homework_iv, R.id.h_info_iv, R.id.h_voice_iv, R.id.h_no_card_iv, R.id.h_attendance_bt, R.id.h_temperature_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.h_homework_iv:
                cameraFragment.surfaceDestroyed(cameraFragment.surfaceHolder);//关闭摄像头
                startActivity(new Intent(this, SendHomeworkActivity.class));
                break;
            case R.id.h_info_iv:
                startActivity(new Intent(this, InformationActivity.class));
                break;
            case R.id.h_voice_iv:

                startActivity(new Intent(this, HomeVoiceActivity.class));
                break;
            case R.id.h_attendance_bt:
                startActivity(new Intent(this, AttendanceDetailsActivity.class));

                break;
            case R.id.h_temperature_iv:
                startActivity(new Intent(this, TemperatureActivity.class));

                break;
            case R.id.h_no_card_iv:

//                startActivityForResult(new Intent(MainActivity.this, SetInfoActivity.class), SET_REBACK_INDEX);
                noCardEt.setText("");
                weatherOperationLayout.setVisibility(View.GONE);
                noCardLayout.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessageDelayed(BACK_INDEX, 40000);
                break;

        }
    }


    /**
     * 打卡成功
     */
    private boolean punchCardSuccess() {

        String tutelage = obtainCardInfo();
        String info = "";

        weatherOperationLayout.setVisibility(View.VISIBLE);
        noCardLayout.setVisibility(View.GONE);
        hSloganTv.setVisibility(View.GONE);
        hOperationLayout.setVisibility(View.GONE);
        hTitleTv.setVisibility(View.VISIBLE);
        hImageIv.setVisibility(View.VISIBLE);
        hPunchCardSuccessTv.setVisibility(View.VISIBLE);

        mHandler.sendEmptyMessageDelayed(BACK_INDEX, REBACK_TIME_INDEX);

        if (isTeacher) {
            textToSpeech.startSpeaking(hTitleTv.getText().toString());
            isTeacher = false;
            info = "卡      号：" + AppConfig.getStringConfig(NuoManConstant.CARD_ID, "") + System.getProperty("line.separator")
                    + "签到时间：" + BaseUtil.getTime("HH:mm:ss");
        } else {
            textToSpeech.startSpeaking(hTitleTv.getText().toString() + "同学");
            info = "年      级：" + AppConfig.getStringConfig(NuoManConstant.GRADE_NAME, "") + System.getProperty("line.separator")
                    + "班      级：" + AppConfig.getStringConfig(NuoManConstant.CLASS_NAME, "") + System.getProperty("line.separator")
                    + "监 护 人：" + tutelage + System.getProperty("line.separator")
                    + "卡      号：" + AppConfig.getStringConfig(NuoManConstant.CARD_ID, "") + System.getProperty("line.separator")
                    + "签到时间：" + BaseUtil.getTime("HH:mm:ss");
        }

        hPunchCardSuccessTv.setText(info);

        return true;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(screenLightReceiver);
        NetReceiver.ehList.remove(this);
        cancelAlert();
        mHandler.removeMessages(CURRENT_TIME_INDEX);
    }

    /**
     * 根据卡号获取信息
     */
    private String obtainCardInfo() {

        LoginInfoModel m = AppTools.getLogInfo();

        String currentCardNo = AppConfig.getStringConfig(NuoManConstant.CARD_ID, "");

        for (int i = 0; i < m.getPeopleMap().getStudentInfos().size(); i++) {
            List<CardNoModel> cardNoList = m.getPeopleMap().getStudentInfos().get(i).getCardNoList();

            for (int j = 0; j < cardNoList.size(); j++) {

                if (currentCardNo.equals(cardNoList.get(j).getCardNo())) {

                    hTitleTv.setText(m.getPeopleMap().getStudentInfos().get(i).getStudentName());
                    return cardNoList.get(j).getCardRole();//返回监护人
                }

            }

        }

        for (int i = 0; i < m.getPeopleMap().getTeacherInfos().size(); i++) {
            List<CardNoModel> cardNoList = m.getPeopleMap().getTeacherInfos().get(i).getCardNoList();

            for (int j = 0; j < cardNoList.size(); j++) {

                if (currentCardNo.equals(cardNoList.get(j).getCardNo())) {
                    isTeacher = true;
                    hTitleTv.setText(m.getPeopleMap().getTeacherInfos().get(i).getTeacherName());
                    return hTitleTv.getText().toString();//返回监护人
                }

            }

        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case SET_REBACK_INDEX:
                    hSchoolNameTv.setText(AppConfig.getStringConfig(NuoManConstant.SCHOOL_NAME, AppTools.getLogInfo().getSchoolName()));
                    requestSync();
                    break;
            }

        }
    }

    /**
     * 上传图片到七牛成功后上传打卡信息
     *
     * @param filePath 要上传的图片路径
     * @param token    在七牛官网上注册的token
     */

    private void uploadImageToQiNiu(final String filePath, String token) {
        UploadManager uploadManager = new UploadManager();
        File file = new File(filePath);

        if (file.exists()) {
            uploadManager.put(filePath, file.getName(), token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject res) {
                    if (info.statusCode == 200) {
                        BaseTransModel transModel = new BaseTransModel();
                        transModel.setMachineNo(logInfo.getMachineId());
                        transModel.setMachineId(logInfo.getMachineId());
                        transModel.setTel(AppTools.getAcacheData(NuoManConstant.USER_NAME));
                        transModel.setCardNo(AppConfig.getStringConfig(NuoManConstant.CARD_ID, ""));
                        transModel.setAttDate(BaseUtil.getTime(BaseUtil.YYYY_MM_DD_HH_MM_SS));
                        transModel.setAttPicUrl(key);
                        transModel.setImagePath(filePath);
                        commonPresenter.invokeInterfaceObtainData(true, "attDataCtrl", NuoManService.WRITEATTLOG, transModel, new TypeToken<BaseReceivedModel>() {
                        });
                    } else { //保存本地
                        BaseTransModel m = new BaseTransModel();
                        m.setCardNo(AppConfig.getStringConfig(NuoManConstant.CARD_ID, ""));
                        m.setAttDate(BaseUtil.getTime(BaseUtil.YYYY_MM_DD_HH_MM_SS));
                        m.setImagePath(filePath);
                        insert(m);
                    }

                    Log.d("NuoMan", "key: " + key + "\n");
                }
            }, null);
        }

    }

    /* ------------------------------------------Sync START ------------------------------------------------------------------------- */


    public static Account CreateSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);

        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            Log.d("SYNC", "--SUCCESS--");
        } else {
            Log.d("SYNC", "--ERROR--");
        }

        return newAccount;
    }


    /**
     * 手动触发同步
     */
    private void requestSync() {
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, NoteProviderMetaData.AUTHORITY, b);
    }


    /* ------------------------------------------Sync END ------------------------------------------------------------------------- */

    /**
     * 熄灭屏幕的广播
     */

    public class ScreenLightReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case NuoManConstant.DROP_SCREEN_LIGHT://熄灭屏幕

                    LockScreenUtils.getInstance().LockScreen(MainActivity.this);

                    break;
                case NuoManConstant.LIGHT_SCREEN_LIGHT:

                    LockScreenUtils.getInstance().wakeUpAndUnlock(MainActivity.this);

                    break;
            }
        }
    }

}
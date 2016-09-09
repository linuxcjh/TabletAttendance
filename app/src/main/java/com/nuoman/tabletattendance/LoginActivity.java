package com.nuoman.tabletattendance;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.common.utils.BaseUtil;
import com.nuoman.tabletattendance.components.CustomDialog;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements ICommonAction {

    @Bind(R.id.login_name_tv)
    EditText loginNameTv;
    @Bind(R.id.login_mac_tv)
    EditText loginMacTv;
    @Bind(R.id.login_bt)
    Button loginBt;
    @Bind(R.id.cancel_bt)
    Button cancelBt;
    @Bind(R.id.keyboard_kbv)
    KeyboardView keyboardKbv;

    private CommonPresenter commonPresenter;

    private BaseTransModel transModel = new BaseTransModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        commonPresenter = new CommonPresenter(this);
        loginMacTv.setText(BaseUtil.getBluetoothMac());
        loginNameTv.setText("02987301181");//02987301181 NMKJ87301181
        loginBt.performClick();
        loginNameTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                keyboardKbv.setVisibility(View.VISIBLE);
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
                Editable mEditable = loginNameTv.getText();
                int start = loginNameTv.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_DONE) {// 完成
                    keyboardKbv.setVisibility(View.GONE);
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

    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {
        switch (methodIndex) {
            case NuoManService.LOGIN:
                if (data != null) {

                    LoginInfoModel model = (LoginInfoModel) data;
                    if (model.getSchoolId().equals("-1")) {
                        Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AppConfig.setBooleanConfig(NuoManConstant.IS_LOGIN, true);//登录成功

                    AppTools.acachePut(NuoManConstant.USER_NAME, loginNameTv.getText().toString());
                    AppTools.acachePut(NuoManConstant.USER_MAC, loginMacTv.getText().toString());

                    CustomDialog dialog = new CustomDialog(this, mHandler, model);
                    dialog.show();

                } else {
                    Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @OnClick({R.id.login_bt, R.id.cancel_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_bt:
                if (TextUtils.isEmpty(loginNameTv.getText().toString())) {
                    Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loginNameTv.getText().toString().equals("666666")) {
                    finish();
                    return;
                }

                transModel.setTel(loginNameTv.getText().toString());
                transModel.setMachineNo(loginMacTv.getText().toString());
                commonPresenter.invokeInterfaceObtainData(false, "loginCtrl", NuoManService.LOGIN, transModel, new TypeToken<LoginInfoModel>() {
                });
                break;
            case R.id.cancel_bt:
                finish();
                break;
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case NuoManConstant.CONFIRMDIALOG:

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                    break;
            }

        }
    };
}

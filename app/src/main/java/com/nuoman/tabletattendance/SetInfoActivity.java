package com.nuoman.tabletattendance;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.common.utils.DownloadService;
import com.nuoman.tabletattendance.components.CustomDialog;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.DownloadModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;

import java.lang.reflect.Method;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetInfoActivity extends BaseActivity implements ICommonAction {

    @Bind(R.id.edit_tv)
    EditText editTv;
    @Bind(R.id.edit_pre_tv)
    EditText editPreTv;
    @Bind(R.id.left_layout)
    ImageView leftLayout;
    @Bind(R.id.select_class_bt)
    Button selectClassBt;
    @Bind(R.id.data_refresh_bt)
    Button dataRefreshBt;
    @Bind(R.id.update_version_bt)
    Button updateVersionBt;
    @Bind(R.id.set_bt)
    Button setBt;
    @Bind(R.id.right_layout)
    ImageView rightLayout;
    @Bind(R.id.change_login_bt)
    Button changeLoginBt;
    @Bind(R.id.exit_bt)
    Button exitBt;
    @Bind(R.id.save_bt)
    Button saveBt;
    @Bind(R.id.confirm_bt)
    Button confirmBt;
    @Bind(R.id.cancel_bt)
    Button cancelBt;
    @Bind(R.id.update_layout)
    RelativeLayout updateLayout;
    private CommonPresenter commonPresenter = new CommonPresenter(this);

    private BaseTransModel transModel = new BaseTransModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_layout);
        ButterKnife.bind(this);
        disableShowSoftInput(editTv);
        disableShowSoftInput(editPreTv);
        editTv.setText(AppTools.getLogInfo().getSchoolName());
        editPreTv.setText(AppTools.getLogInfo().getSchoolName());
        selectClassBt.setText(AppConfig.getStringConfig(NuoManConstant.GRADE_NAME, "") + AppConfig.getStringConfig(NuoManConstant.CLASS_NAME, ""));


    }

    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {
        switch (methodIndex) {
            case NuoManService.LOGIN:

                if (data != null) {
                    LoginInfoModel model = (LoginInfoModel) data;
                    NuoManConstant.UPDATE_TIME = model.getUpdateDataTime();
                    NuoManConstant.ENTER_SET_PWD = model.getSuperPass();
                    Toast.makeText(this, "数据更新成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "数据更新失败", Toast.LENGTH_SHORT).show();

                }

                break;
            case NuoManService.GETUPDATEINFO:

                if (data != null) {
                    DownloadModel model = (DownloadModel) data;

                    if (Integer.parseInt(model.getVersion()) > AppTools.getVersionCode()) {
                        AppConfig.setStringConfig(NuoManConstant.DOWNLOAD_URL, model.getDurl());
                        updateLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(this, "已是最新版本", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "版本数据错误", Toast.LENGTH_SHORT).show();

                }

                break;
        }
    }


    @OnClick({R.id.select_class_bt, R.id.data_refresh_bt, R.id.update_version_bt, R.id.set_bt, R.id.change_login_bt, R.id.exit_bt, R.id.save_bt, R.id.confirm_bt, R.id.cancel_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_class_bt:

                CustomDialog dialog = new CustomDialog(this, mHandler, AppTools.getLogInfo());
                dialog.show();
                break;
            case R.id.data_refresh_bt:
                transModel.setTel(AppConfig.getStringConfig(NuoManConstant.USER_NAME, ""));
                transModel.setMachineNo(AppConfig.getStringConfig(NuoManConstant.USER_MAC, ""));
                commonPresenter.invokeInterfaceObtainData(false, "loginCtrl", NuoManService.LOGIN, transModel, new TypeToken<LoginInfoModel>() {
                });
                break;
            case R.id.update_version_bt:
                BaseTransModel model = new BaseTransModel();
                model.setType("2");
                commonPresenter.invokeInterfaceObtainData(false, "updateCtrl", NuoManService.GETUPDATEINFO, model, new TypeToken<DownloadModel>() {
                });
                break;
            case R.id.set_bt:
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
                break;
            case R.id.change_login_bt:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.exit_bt:
                finish();
                break;
            case R.id.save_bt:
                AppConfig.setStringConfig(NuoManConstant.SCHOOL_NAME, editTv.getText().toString() + "\n" + editPreTv.getText().toString());
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.confirm_bt:
                startService(new Intent(SetInfoActivity.this, DownloadService.class));
                updateLayout.setVisibility(View.GONE);
                Toast.makeText(this, "正在后台下载……", Toast.LENGTH_SHORT).show();

                break;
            case R.id.cancel_bt:
                updateLayout.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput(EditText editTv) {
        if (Build.VERSION.SDK_INT <= 10) {
            editTv.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editTv, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editTv, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case NuoManConstant.CONFIRMDIALOG:
                    selectClassBt.setText(AppConfig.getStringConfig(NuoManConstant.GRADE_NAME, "") + AppConfig.getStringConfig(NuoManConstant.CLASS_NAME, ""));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    break;
            }

        }
    };

}

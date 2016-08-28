package com.nuoman.tabletattendance;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;

import java.lang.reflect.Method;

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
    private CommonPresenter commonPresenter = new CommonPresenter(this);

    private BaseTransModel transModel = new BaseTransModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_layout);
        ButterKnife.bind(this);
        disableShowSoftInput(editTv);
        disableShowSoftInput(editPreTv);


    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {

        BaseReceivedModel model = (BaseReceivedModel) data;


    }

    @OnClick({R.id.select_class_bt, R.id.data_refresh_bt, R.id.update_version_bt, R.id.set_bt, R.id.change_login_bt, R.id.exit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_class_bt:
                break;
            case R.id.data_refresh_bt:
                break;
            case R.id.update_version_bt:
                break;
            case R.id.set_bt:
                break;
            case R.id.change_login_bt:
                break;
            case R.id.exit_bt:
                finish();
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
}

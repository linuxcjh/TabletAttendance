package com.nuoman.tabletattendance;

import android.os.Bundle;

import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;

import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements ICommonAction {

    private CommonPresenter commonPresenter;

    private BaseTransModel transModel = new BaseTransModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        commonPresenter = new CommonPresenter(this);
        transModel.setTel("02987301181");
        transModel.setMachineNo("123");
//        commonPresenter.invokeInterfaceObtainData(false, "loginCtrl", NuoManService.LOGIN, transModel, new TypeToken<BaseReceivedModel>() {
//        });

    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {

        switch (methodIndex) {
            case NuoManService.LOGIN:
                BaseReceivedModel model = (BaseReceivedModel) data;
                break;
        }
    }



}

package com.nuoman.tabletattendance;

import android.os.Bundle;

import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.model.BaseReceivedModel;

public class SetInfoActivity extends BaseActivity implements ICommonAction {

//    private CommonPresenter commonPresenter = new CommonPresenter(this);

//    private BaseTransModel transModel = new BaseTransModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        transModel.setTel("18000000000");
//        transModel.setMachineid("123");
//        commonPresenter.invokeInterfaceObtainData(NuoManService.LOGINCONTROLLER, transModel, new TypeToken<BaseReceivedModel>() {
//        });
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {

        BaseReceivedModel model = (BaseReceivedModel) data;


    }
}

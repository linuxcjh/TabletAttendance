package com.nuoman.tabletattendance;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements ICommonAction {

    @Bind(R.id.content_tv)
    TextView contentTv;
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
        transModel.setTel("18000000000");
        transModel.setMachineid("123");
        commonPresenter.invokeInterfaceObtainData(NuoManService.LOGINCONTROLLER, transModel, new TypeToken<BaseReceivedModel>() {
        });
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {

        switch (methodIndex) {
            case NuoManService.LOGINCONTROLLER:
                BaseReceivedModel model = (BaseReceivedModel) data;
                contentTv.setText(model.getRole());
                break;
        }
    }
}

package com.nuoman.tabletattendance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.Adapter.AttendanceAdapter;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.ReceivedCommonResultModel;
import com.nuoman.tabletattendance.model.ReceivedUnreadInforModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息
 * Created by Alex on 2016/5/17.
 */
public class AttendanceDetailsActivity extends BaseActivity implements ICommonAction {


    @Bind(R.id.grid_view)
    GridView gridView;
    @Bind(R.id.back_bt)
    Button backBt;
    @Bind(R.id.confirm_bt)
    Button confirmBt;
    private CommonPresenter commonPresenter;

    private BaseTransModel transModel = new BaseTransModel();
    private AttendanceAdapter adapter;
    private List<ReceivedUnreadInforModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_layout);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        commonPresenter = new CommonPresenter(this);
        transModel.setClassId(AppConfig.getStringConfig(NuoManConstant.CLASS_ID, ""));

        adapter = new AttendanceAdapter(this, R.layout.infor_item_layout, data);
        gridView.setAdapter(adapter);
        commonPresenter.invokeInterfaceObtainData(false, "attDataCtrl", NuoManService.GETATTLISTBYCLASSID, transModel, new TypeToken<ReceivedCommonResultModel>() {
        });
    }

    @OnClick({R.id.back_bt, R.id.confirm_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_bt:
                finish();
                break;
            case R.id.confirm_bt:
                break;
        }
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {

        switch (methodIndex) {
            case NuoManService.GETATTLISTBYCLASSID:
                if (data != null) {
                    ReceivedCommonResultModel model = (ReceivedCommonResultModel) data;
                    List<ReceivedUnreadInforModel> list = new ArrayList<>();
                    list.addAll(model.getObj());

                    adapter.setData(list);

                }
                break;
        }

    }



}

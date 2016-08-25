package com.nuoman.tabletattendance.information;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.Adapter.UnreadInformationAdapter;
import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.ReceivedCommonResultModel;
import com.nuoman.tabletattendance.model.ReceivedUnreadInforModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息
 * Created by Alex on 2016/5/17.
 */
public class InformationActivity extends BaseActivity implements ICommonAction {


    @Bind(R.id.edit_input_et)
    EditText editInputEt;
    @Bind(R.id.grid_view)
    GridView gridView;
    @Bind(R.id.back_bt)
    Button backBt;

    private CommonPresenter commonPresenter;

    private BaseTransModel transModel = new BaseTransModel();
    private UnreadInformationAdapter adapter;
    private List<ReceivedUnreadInforModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_layout);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        commonPresenter = new CommonPresenter(this);
        transModel.setClassId(AppConfig.getStringConfig(NuoManConstant.CLASS_ID, ""));

        adapter = new UnreadInformationAdapter(this, R.layout.activity_camera_layout, data);
        gridView.setAdapter(adapter);
        editInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(AppConfig.getContext(), v.getText().toString(), Toast.LENGTH_SHORT).show();
                String cardId = v.getText().toString().replace("\n", "");
                editInputEt.setText("");
                return false;
            }
        });
        commonPresenter.invokeInterfaceObtainData(false, "voiceCtrl", NuoManService.GETUNREADMSG, transModel, new TypeToken<ReceivedCommonResultModel>() {
        });
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {

        switch (methodIndex) {
            case NuoManService.GETUNREADMSG:
                ReceivedCommonResultModel model = (ReceivedCommonResultModel) data;
//                adapter.setData();

                break;
        }

    }

    @OnClick(R.id.back_bt)
    public void onClick() {
        finish();
    }
}

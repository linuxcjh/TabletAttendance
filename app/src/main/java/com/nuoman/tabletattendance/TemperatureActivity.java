package com.nuoman.tabletattendance;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.CardNoModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;
import com.nuoman.tabletattendance.model.ReceivedCommonResultModel;
import com.nuoman.tabletattendance.model.StudentInfos;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 体温
 * Created by Alex on 2016/5/17.
 */
public class TemperatureActivity extends BaseActivity implements ICommonAction {

    @Bind(R.id.head_iv)
    ImageView headIv;
    @Bind(R.id.left_layout)
    RelativeLayout leftLayout;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.class_tv)
    TextView classTv;
    @Bind(R.id.temperature_tv)
    Button temperatureTv;
    @Bind(R.id.back_bt)
    Button backBt;
    @Bind(R.id.confirm_bt)
    Button confirmBt;
    @Bind(R.id.edit_input_et)
    EditText editInputEt;
    private CommonPresenter commonPresenter;

    private BaseTransModel transModel = new BaseTransModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_layout);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        commonPresenter = new CommonPresenter(this);

        editInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String cardId = v.getText().toString().replace("\n", "");
                StudentInfos info = obtainCardInfo(cardId);

                transModel.setCardno(cardId);
                nameTv.setText(info.getStudentName());
                classTv.setText(info.getGradeName() + info.getClassName());
//                Glide.with(TemperatureActivity.this).load(info.getGradeName()).error(R.drawable.icon).placeholder(R.drawable.icon).into(headIv);

                editInputEt.setText("");
                if (info == null) {
                    Toast.makeText(TemperatureActivity.this, "没有对应的信息", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    @OnClick({R.id.back_bt, R.id.confirm_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_bt:
                finish();
                break;
            case R.id.confirm_bt:
                transModel.setTemper("");//TODO 补充温度数据

                commonPresenter.invokeInterfaceObtainData(false, "heathyCtrl", NuoManService.SAVETEMPERATURE, transModel, new TypeToken<ReceivedCommonResultModel>() {
                });
                break;
        }
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {

        switch (methodIndex) {
            case NuoManService.SAVETEMPERATURE:
                if (data != null) {
                    ReceivedCommonResultModel model = (ReceivedCommonResultModel) data;
                    if (model.getMsg().equals("success")) {
                        Toast.makeText(TemperatureActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TemperatureActivity.this, "请重新提交¬", Toast.LENGTH_SHORT).show();

                    }

                }
                break;
        }

    }

    /**
     * 根据卡号获取信息
     */
    private StudentInfos obtainCardInfo(String cardId) {

        LoginInfoModel m = AppTools.getLogInfo();

        for (int i = 0; i < m.getPeopleMap().getStudentInfos().size(); i++) {
            List<CardNoModel> cardNoList = m.getPeopleMap().getStudentInfos().get(i).getCardNoList();

            for (int j = 0; j < cardNoList.size(); j++) {

                if (cardId.equals(cardNoList.get(j).getCardNo())) {

                    return m.getPeopleMap().getStudentInfos().get(i);//返回学生姓名
                }

            }

        }
        return null;
    }
}

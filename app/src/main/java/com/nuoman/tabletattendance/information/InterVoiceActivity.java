package com.nuoman.tabletattendance.information;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.Adapter.ParentInformationAdapter;
import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.ParentInfo;
import com.nuoman.tabletattendance.model.ReceivedParentInfoModel;
import com.nuoman.tabletattendance.model.StudentInfos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 语音对话
 * Created by Alex on 2016/5/17.
 */
public class InterVoiceActivity extends BaseActivity implements ICommonAction {


    @Bind(R.id.back_bt)
    Button backBt;
    @Bind(R.id.container_layout)
    FrameLayout containerLayout;
    @Bind(R.id.grid_view)
    GridView gridView;
    @Bind(R.id.item_icon)
    ImageView itemIcon;
    @Bind(R.id.name_tv)
    TextView nameTv;


    private CommonPresenter commonPresenter;

    private BaseTransModel transModel = new BaseTransModel();
    private ParentInformationAdapter adapter;
    private List<ParentInfo> data;

    private StudentInfos infos = new StudentInfos();

    private String cardNo;
    InterVoiceFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_inter_main);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        infos = (StudentInfos) getIntent().getSerializableExtra("model");
        cardNo = getIntent().getStringExtra("cardNo");
//        Glide.with(this).load(infos.getStudentName())
        nameTv.setText(infos.getStudentName());

        commonPresenter = new CommonPresenter(this);
        transModel.setCardNo(cardNo);
        commonPresenter.invokeInterfaceObtainData(false, "voiceCtrl", NuoManService.GETPARENTSBYCARDNO, transModel, new TypeToken<ReceivedParentInfoModel>() {
        });
        adapter = new ParentInformationAdapter(this, R.layout.infor_item_layout, data);
        gridView.setAdapter(adapter);


        fragment = new InterVoiceFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container_layout, fragment);
        transaction.commit();
    }


    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {
        switch (methodIndex) {
            case NuoManService.GETPARENTSBYCARDNO:
                ReceivedParentInfoModel model = (ReceivedParentInfoModel) data;
                List<ParentInfo> list = new ArrayList<>();
                list.addAll(model.getObj());
                adapter.setData(list);


                break;
        }
    }


    @OnClick(R.id.back_bt)
    public void onClick() {
        finish();
    }
}

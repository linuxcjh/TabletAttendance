package com.nuoman.tabletattendance.information;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nuoman.tabletattendance.model.TeacherInfos;

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
    private List<ParentInfo> dataList = new ArrayList<>();

    private StudentInfos infos = new StudentInfos();
    private TeacherInfos teacherInfos = new TeacherInfos();

    private String cardNo;
    private InterVoiceFragment fragment;

    private boolean isTeacher; //true :Teacher, false: Student


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_inter_main);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        commonPresenter = new CommonPresenter(this);
        fragment = new InterVoiceFragment();

        cardNo = getIntent().getStringExtra("cardNo");


        isTeacher = getIntent().getBooleanExtra("isTeacher", false);
        if (isTeacher) {
            teacherInfos = (TeacherInfos) getIntent().getSerializableExtra("model");
            //        Glide.with(this).load(infos.getStudentName())
            nameTv.setText(teacherInfos.getTeacherName());
            fragment.setSendTeacherInfo(teacherInfos);

        } else {
            transModel.setCardNo(cardNo);
            fragment.setSendStudentInfo(infos);
            infos = (StudentInfos) getIntent().getSerializableExtra("model");
            //        Glide.with(this).load(infos.getStudentName())
            nameTv.setText(infos.getStudentName());
            ParentInfo m = new ParentInfo();
            m.setDataName("群发");
            dataList.add(m);
        }

        if (isTeacher) {
            transModel.setTeacherId(teacherInfos.getTeacherId());
            transModel.setKind("2");
            commonPresenter.invokeInterfaceObtainData(false, "voiceCtrl", NuoManService.GETCLASSESBYTEACHERID, transModel, new TypeToken<ReceivedParentInfoModel>() {
            });
        } else {
            commonPresenter.invokeInterfaceObtainData(false, "voiceCtrl", NuoManService.GETPARENTSBYCARDNO, transModel, new TypeToken<ReceivedParentInfoModel>() {
            });
        }


        adapter = new ParentInformationAdapter(this, R.layout.infor_item_layout, dataList, isTeacher);
        gridView.setAdapter(adapter);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container_layout, fragment);
        transaction.commit();
    }


    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {
        if (data != null) {
            switch (methodIndex) {
                case NuoManService.GETPARENTSBYCARDNO:
                    ReceivedParentInfoModel model = (ReceivedParentInfoModel) data;
                    dataList.addAll(model.getObj());
                    adapter.setData(dataList);
                    if (model.getObj().size() > 0) {
                        fragment.setSendStudentGroupId(model.getObj().get(0).getUserIds());
                    }
                    break;
                case NuoManService.GETCLASSESBYTEACHERID:
//                    ReceivedParentInfoModel modelReslut = (ReceivedParentInfoModel) data;
//                    dataList.addAll(modelReslut.getObj());
//                    adapter.setData(dataList);
//                    if (modelReslut.getObj().size() > 0) {
//                        fragment.setSendStudentGroupId(modelReslut.getObj().get(0).getUserIds());
//                    }
                    break;
            }
        } else {
            Toast.makeText(InterVoiceActivity.this, "返回空数据", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.back_bt)
    public void onClick() {
        finish();
    }
}

package com.nuoman.tabletattendance.information;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;

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

import java.util.ArrayList;
import java.util.List;

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


    private CommonPresenter commonPresenter;

    private BaseTransModel transModel = new BaseTransModel();
    private UnreadInformationAdapter adapter;
    private List<ReceivedUnreadInforModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_inter_main);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        commonPresenter = new CommonPresenter(this);
        transModel.setCardNo(AppConfig.getStringConfig(NuoManConstant.CARD_ID, ""));
        CardNo cardNo =new CardNo();
        cardNo.setCardNo(AppConfig.getStringConfig(NuoManConstant.CARD_ID, ""));
        adapter = new UnreadInformationAdapter(this, R.layout.infor_item_layout, data);
        gridView.setAdapter(adapter);
        commonPresenter.invokeInterfaceObtainData(false, "voiceCtrl", NuoManService.GETPARENTSBYCARDNO, cardNo, new TypeToken<ReceivedCommonResultModel>() {
        });

        InterVoiceFragment fragment = new InterVoiceFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container_layout, fragment);
        transaction.commit();
    }

    class CardNo{
        String cardNo;

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {
        switch (methodIndex) {
            case NuoManService.GETPARENTSBYCARDNO:
                ReceivedCommonResultModel model = (ReceivedCommonResultModel) data;
                List<ReceivedUnreadInforModel> list = new ArrayList<>();
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

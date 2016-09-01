package com.nuoman.tabletattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.information.InterVoiceActivity;
import com.nuoman.tabletattendance.model.CardNoModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;
import com.nuoman.tabletattendance.model.StudentInfos;
import com.nuoman.tabletattendance.model.TeacherInfos;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 语音
 * Created by Alex on 2016/5/17.
 */
public class HomeVoiceActivity extends BaseActivity {


    @Bind(R.id.edit_input_et)
    EditText editInputEt;
    @Bind(R.id.root_layout)
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_voice_layout);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        editInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String cardId = v.getText().toString().replace("\n", "");
                cardId ="0087301181";
                StudentInfos info = obtainCardInfo(cardId);
                TeacherInfos teacherInfos = obtainTeacherCardInfo(cardId);
                editInputEt.setText("");
                if (info == null && teacherInfos == null) {
                    Toast.makeText(HomeVoiceActivity.this, "没有对应的信息", Toast.LENGTH_SHORT).show();
                } else if (info != null) {
                    startActivity(new Intent(HomeVoiceActivity.this, InterVoiceActivity.class).putExtra("cardNo", cardId).putExtra("model", info).putExtra("isTeacher", false));
                } else if (teacherInfos != null) {
                    startActivity(new Intent(HomeVoiceActivity.this, InterVoiceActivity.class).putExtra("cardNo", cardId).putExtra("model", teacherInfos).putExtra("isTeacher", true));
                }
                finish();

                return false;
            }
        });
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
                    return m.getPeopleMap().getStudentInfos().get(i);//返回学生
                }
            }

        }
        return null;
    }

    /**
     * 根据卡号获取信息
     */
    private TeacherInfos obtainTeacherCardInfo(String cardId) {

        LoginInfoModel m = AppTools.getLogInfo();

        for (int i = 0; i < m.getPeopleMap().getTeacherInfos().size(); i++) {
            List<CardNoModel> cardNoList = m.getPeopleMap().getTeacherInfos().get(i).getCardNoList();

            for (int j = 0; j < cardNoList.size(); j++) {

                if (cardId.equals(cardNoList.get(j).getCardNo())) {

                    return m.getPeopleMap().getTeacherInfos().get(i);//返回老师
                }

            }

        }
        return null;
    }

    @OnClick(R.id.root_layout)
    public void onClick() {
        finish();
    }
}

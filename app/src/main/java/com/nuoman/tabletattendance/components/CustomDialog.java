package com.nuoman.tabletattendance.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.nuoman.tabletattendance.R;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.model.LoginInfoModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alex on 2015/11/5.
 * 对话框
 */
public class CustomDialog extends Dialog implements View.OnClickListener {

    @Bind(R.id.grade_tv)
    TextView gradeTv;
    @Bind(R.id.class_tv)
    TextView classTv;
    @Bind(R.id.confirm_bt)
    Button confirmBt;
    private Handler handler;
    private Window dialogWindow;

    private Context context;
    private LoginInfoModel model;

    private int currentGrade;


    public CustomDialog(Context context, Handler handler, LoginInfoModel model) {
        super(context, R.style.selection_dialog_theme);

        this.context = context;
        this.handler = handler;
        this.model = model;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog_layout);
        dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(android.R.style.Animation_Toast);
        ButterKnife.bind(this);


        if (model.getGradeExtends() != null && model.getGradeExtends().size() > 0) {
            gradeTv.setText(model.getGradeExtends().get(0).getGradeName());
            gradeTv.setTag(model.getGradeExtends().get(0).getId());


            if (model.getGradeExtends().get(0).getClassList() != null && model.getGradeExtends().get(0).getClassList().size() > 0) {
                classTv.setText(model.getGradeExtends().get(0).getClassList().get(0).getClassName());
                classTv.setTag(model.getGradeExtends().get(0).getClassList().get(0).getId());
            }
        }

    }


    @OnClick({R.id.grade_tv, R.id.class_tv, R.id.confirm_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.grade_tv:

                final String[] grade = new String[model.getGradeExtends().size()];
                final String[] gradeId = new String[model.getGradeExtends().size()];

                for (int i = 0; i < grade.length; i++) {
                    grade[i] = model.getGradeExtends().get(i).getGradeName();
                    gradeId[i] = model.getGradeExtends().get(i).getId();

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("选择年级");
                builder.setSingleChoiceItems(grade, 0, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentGrade = which;
                        gradeTv.setText(grade[which]);
                        gradeTv.setTag(gradeId[which]);

                        if (model.getGradeExtends().get(which).getClassList() != null && model.getGradeExtends().get(which).getClassList().size() > 0) {
                            classTv.setText(model.getGradeExtends().get(which).getClassList().get(0).getClassName());
                            classTv.setTag(model.getGradeExtends().get(which).getClassList().get(0).getId());
                        }

                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(400, 300);


                break;
            case R.id.class_tv:
                if (model.getGradeExtends() != null && model.getGradeExtends().size() > 0) {

                    if (model.getGradeExtends().get(currentGrade).getClassList() != null && model.getGradeExtends().get(currentGrade).getClassList().size() > 0) {
                        final String[] className = new String[model.getGradeExtends().get(currentGrade).getClassList().size()];
                        final String[] classId = new String[model.getGradeExtends().get(currentGrade).getClassList().size()];
                        for (int i = 0; i < className.length; i++) {
                            className[i] = model.getGradeExtends().get(currentGrade).getClassList().get(i).getClassName();
                            classId[i] = model.getGradeExtends().get(currentGrade).getClassList().get(i).getId();

                        }

                        AlertDialog.Builder builderC = new AlertDialog.Builder(context);

                        builderC.setTitle("选择班级");
                        builderC.setSingleChoiceItems(className, 0, new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                classTv.setText(className[which]);
                                classTv.setTag(classId[which]);

                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialogC = builderC.create();
                        alertDialogC.show();
                        alertDialogC.getWindow().setLayout(400, 300);


                    }
                }

                break;
            case R.id.confirm_bt:
                AppConfig.setStringConfig(NuoManConstant.GRADE_NAME, gradeTv.getText().toString());
                AppConfig.setStringConfig(NuoManConstant.GRADE_ID, (String) gradeTv.getTag());
                AppConfig.setStringConfig(NuoManConstant.CLASS_NAME, classTv.getText().toString());
                AppConfig.setStringConfig(NuoManConstant.CLASS_ID, (String) classTv.getTag());

                handler.sendMessage(handler.obtainMessage(NuoManConstant.CONFIRMDIALOG));
                this.dismiss();
                break;
        }
    }
}

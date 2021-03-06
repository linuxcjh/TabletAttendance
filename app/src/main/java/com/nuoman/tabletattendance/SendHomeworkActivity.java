package com.nuoman.tabletattendance;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.CardNoModel;
import com.nuoman.tabletattendance.model.HomeWorkReceiveModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * send homework
 */
public class SendHomeworkActivity extends BaseActivity implements ICommonAction {

    private static final int FIRST_IMAGE_INDEX = 0;
    private static final int SECOND_IMAGE_INDEX = 1;
    private static final int THIRD_IMAGE_INDEX = 2;
    @Bind(R.id.tip_layout)
    RelativeLayout tipLayout;
    @Bind(R.id.edit_input_et)
    EditText editInputEt;


    private boolean first_image;
    private boolean second_image;
    private boolean thrid_image;


    @Bind(R.id.image_first_iv)
    ImageView imageFirstIv;
    @Bind(R.id.image_second_iv)
    ImageView imageSecondIv;
    @Bind(R.id.image_third_iv)
    ImageView imageThirdIv;
    @Bind(R.id.cancel_bt)
    Button cancelBt;
    @Bind(R.id.commit_bt)
    Button commitBt;
    private CommonPresenter commonPresenter = new CommonPresenter(this);

    private BaseTransModel transModel = new BaseTransModel();

    /**
     * 图片id
     */
    private Map<Integer, String> pics = new HashMap<>();
    private Map<Integer, String> paths = new HashMap<>();

    private String cardId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_layout);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        commonPresenter.invokeInterfaceObtainData(false, "qiniuCtrl", NuoManService.GETTOKEN, null, new TypeToken<BaseReceivedModel>() {
        });
        editInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                cardId = v.getText().toString().replace("\n", "");
                editInputEt.setText("");
                transModel.setCardNo(cardId);

                if (TextUtils.isEmpty(obtainCardInfo())) {
                    commonPresenter.invokeInterfaceObtainData(false, "homeworkCtrl", NuoManService.GETTEACHERID, transModel, new TypeToken<HomeWorkReceiveModel>() {
                    });
                } else {
                    transModel.setTeacherId(obtainCardInfo());
                    Toast.makeText(SendHomeworkActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                    tipLayout.setVisibility(View.GONE);
                }


                return false;
            }
        });
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {
        if (data != null) {

            switch (methodIndex) {
                case NuoManService.GETTOKEN:
                    if (data != null) {
                        BaseReceivedModel model = (BaseReceivedModel) data;
                        AppTools.acachePut(NuoManConstant.TOKEN, model.getToken());

                    }
                    break;
                case NuoManService.GETTEACHERID:
                    HomeWorkReceiveModel m = (HomeWorkReceiveModel) data;
                    if (m.isSuccess() && !TextUtils.isEmpty(m.getObj().getTeacherId()) && !m.getObj().getTeacherId().equals("-1")) {
                        transModel.setTeacherId(m.getObj().getTeacherId());
                        Toast.makeText(SendHomeworkActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                        tipLayout.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(SendHomeworkActivity.this, "授权失败,请使用指定的卡号", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                case NuoManService.SAVEHOMEWORK:
                    BaseReceivedModel model = (BaseReceivedModel) data;

                    if (model.isSuccess()) {
                        Toast.makeText(SendHomeworkActivity.this, "作业发送成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SendHomeworkActivity.this, "作业发送失败,请重新发送", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    }


    @OnClick({R.id.image_first_iv, R.id.image_second_iv, R.id.image_third_iv, R.id.cancel_bt, R.id.commit_bt, R.id.tip_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_first_iv:
                if (first_image) {
                    startActivityForResult(new Intent(SendHomeworkActivity.this, ImageActivity.class).putExtra("filePath", (String) imageFirstIv.getTag()), 3);
                } else {
                    startActivityForResult(new Intent(SendHomeworkActivity.this, CameraActivity.class), FIRST_IMAGE_INDEX);
                }
                break;
            case R.id.image_second_iv:
                if (second_image) {
                    startActivityForResult(new Intent(SendHomeworkActivity.this, ImageActivity.class).putExtra("filePath", (String) imageSecondIv.getTag()), 4);
                } else {
                    startActivityForResult(new Intent(SendHomeworkActivity.this, CameraActivity.class), SECOND_IMAGE_INDEX);

                }
                break;
            case R.id.image_third_iv:
                if (thrid_image) {
                    startActivityForResult(new Intent(SendHomeworkActivity.this, ImageActivity.class).putExtra("filePath", (String) imageThirdIv.getTag()), 5);
                } else {
                    startActivityForResult(new Intent(SendHomeworkActivity.this, CameraActivity.class), THIRD_IMAGE_INDEX);

                }
                break;
            case R.id.tip_layout:
            case R.id.cancel_bt:
                this.finish();
                break;
            case R.id.commit_bt:

                if ((pics.size() != paths.size()) && paths.size() > 0) {

                    for (Integer key : paths.keySet()) {
                        uploadImageToQiNiu(paths.get(key), AppTools.getAcacheData(NuoManConstant.TOKEN), key);
                    }
                    return;
                }

                if (paths.size() > 0 && (paths.size() == pics.size())) {

                    transModel.setClassId(AppConfig.getStringConfig(NuoManConstant.CLASS_ID, ""));

                    StringBuilder builder = new StringBuilder();

                    for (Integer key : pics.keySet()) {
                        builder.append(pics.get(key)).append("#");
                    }
                    transModel.setHomeworkPic(builder.toString().substring(0, builder.toString().length() - 1));
                    commonPresenter.invokeInterfaceObtainData(true, "homeworkCtrl", NuoManService.SAVEHOMEWORK, transModel, new TypeToken<BaseReceivedModel>() {
                    });
                } else {
                    Toast.makeText(this, "请拍照", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            String filePath = data.getStringExtra("filePath");

            switch (requestCode) {
                case FIRST_IMAGE_INDEX:
                    first_image = true;
                    imageFirstIv.setImageBitmap(BitmapFactory.decodeFile(filePath));
                    imageFirstIv.setTag(filePath);
                    paths.put(FIRST_IMAGE_INDEX, filePath);

//                    uploadImageToQiNiu(filePath, AppTools.getAcacheData(NuoManConstant.TOKEN), FIRST_IMAGE_INDEX);
                    break;
                case SECOND_IMAGE_INDEX:
                    second_image = true;

                    imageSecondIv.setImageBitmap(BitmapFactory.decodeFile(filePath));
                    imageSecondIv.setTag(filePath);
                    paths.put(SECOND_IMAGE_INDEX, filePath);

//                    uploadImageToQiNiu(filePath, AppTools.getAcacheData(NuoManConstant.TOKEN), SECOND_IMAGE_INDEX);
                    break;
                case THIRD_IMAGE_INDEX:
                    thrid_image = true;

                    imageThirdIv.setImageBitmap(BitmapFactory.decodeFile(filePath));
                    imageThirdIv.setTag(filePath);
                    paths.put(THIRD_IMAGE_INDEX, filePath);

//                    uploadImageToQiNiu(filePath, AppTools.getAcacheData(NuoManConstant.TOKEN), THIRD_IMAGE_INDEX);

                    break;
                case 3:
                    first_image = false;
                    imageFirstIv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_03));
                    imageFirstIv.setTag("");
                    paths.remove(FIRST_IMAGE_INDEX);
                    break;
                case 4:
                    second_image = false;
                    imageSecondIv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_03));
                    imageSecondIv.setTag("");
                    paths.remove(SECOND_IMAGE_INDEX);

                    break;
                case 5:
                    thrid_image = false;
                    imageThirdIv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_03));
                    imageThirdIv.setTag("");
                    paths.remove(THIRD_IMAGE_INDEX);


                    break;
            }
        }
    }

    /**
     * 上传图片到七牛
     *
     * @param filePath 要上传的图片路径
     * @param token    在七牛官网上注册的token
     */

    private void uploadImageToQiNiu(String filePath, String token, final int requestCode) {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        File file = new File(filePath);

        if (file.exists()) {
            uploadManager.put(filePath, file.getName(), token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject res) {
                    // info.error中包含了错误信息，可打印调试
                    // 上传成功后将key值上传到自己的服务器
                    pics.put(requestCode, key);
                    if (pics.size() == paths.size()) {//照片全部上传成功
                        commitBt.performClick();
                    }

                    Log.d("NuoMan", "key: " + key + "\n");
                }
            }, null);
        }

    }

    /**
     * 根据卡号获取信息
     */
    private String obtainCardInfo() {

        LoginInfoModel m = AppTools.getLogInfo();

        for (int i = 0; i < m.getPeopleMap().getTeacherInfos().size(); i++) {
            List<CardNoModel> cardNoList = m.getPeopleMap().getTeacherInfos().get(i).getCardNoList();

            for (int j = 0; j < cardNoList.size(); j++) {

                if (cardId.equals(cardNoList.get(j).getCardNo())) {
                    return m.getPeopleMap().getTeacherInfos().get(i).getTeacherId();//返回老师ID
                }

            }

        }
        return "";
    }
}

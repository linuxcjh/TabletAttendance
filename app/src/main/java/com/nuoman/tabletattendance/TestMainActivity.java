package com.nuoman.tabletattendance;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.common.utils.CameraNoMarkActivity;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestMainActivity extends BaseActivity implements ICommonAction {

    @Bind(R.id.content_tv)
    TextView contentTv;
    @Bind(R.id.edit_tv)
    EditText editTv;
    @Bind(R.id.edit_pre_tv)
    EditText editPreTv;
    @Bind(R.id.edit_sub_tv)
    EditText editSubTv;
    @Bind(R.id.camera_bt)
    Button cameraBt;
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
        commonPresenter.invokeInterfaceObtainData(false, "qiniuCtrl", NuoManService.GETTOKEN, null, new TypeToken<BaseReceivedModel>() {
        });

    }

    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {

        switch (methodIndex) {
            case NuoManService.GETTOKEN:
                BaseReceivedModel model = (BaseReceivedModel) data;
                contentTv.setText(model.getToken());

                 AppTools.acachePut(NuoManConstant.TOKEN,model.getToken());
                break;
        }
    }


    @OnClick(R.id.camera_bt)
    public void onClick() {

        startActivityForResult(new Intent(this, CameraNoMarkActivity.class), 0x11);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            uploadImageToQiNiu(data.getStringExtra("filePath"), AppTools.getAcacheData(NuoManConstant.TOKEN));
        }
    }

    /**
     * 上传图片到七牛
     *
     * @param filePath 要上传的图片路径
     * @param token    在七牛官网上注册的token
     */

    private void uploadImageToQiNiu(String filePath, String token) {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        File file = new File(filePath);
        if (file.exists()) {
            uploadManager.put(filePath, file.getName(), token, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject res) {
                    // info.error中包含了错误信息，可打印调试
                    // 上传成功后将key值上传到自己的服务器

                    Log.d("NuoMan", "key: " + key + "\n");
                }
            }, null);
        }

    }



    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput(EditText editTv) {
        if (Build.VERSION.SDK_INT <= 10) {
            editTv.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editTv, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editTv, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

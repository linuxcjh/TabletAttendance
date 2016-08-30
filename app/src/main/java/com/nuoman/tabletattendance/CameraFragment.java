package com.nuoman.tabletattendance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 相机
 */
public class CameraFragment extends Fragment implements Callback, ICommonAction {


    public interface ObtainPictureListener {
        void onChangeArticle(String pictureId);
    }

    ObtainPictureListener obtainPictureListener;

    @Bind(R.id.surfaceView1)
    SurfaceView surfaceView;
    //    private SurfaceView surfaceView;
    public SurfaceHolder surfaceHolder;
    private Camera camera;
    private Camera.Parameters parameters;

    public String filePath;
    private CommonPresenter commonPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ObtainPictureListener) {
            obtainPictureListener = (ObtainPictureListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChangeArticleListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_camera_layout, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    private void init() {
        commonPresenter = new CommonPresenter(this);
        commonPresenter.invokeInterfaceObtainData(false, "qiniuCtrl", NuoManService.GETTOKEN, null, new TypeToken<BaseReceivedModel>() {
        });
        surfaceView.setFocusable(true);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置类型
        surfaceHolder.setKeepScreenOn(true);

    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {

        switch (methodIndex) {
            case NuoManService.GETTOKEN:
                BaseReceivedModel model = (BaseReceivedModel) data;
                AppConfig.setStringConfig("token", model.getToken());

                Toast.makeText(getActivity(),model.getToken(),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
//        //实现自动对焦
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    initCamera();//实现相机的参数初始化
                    camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
//                    takePicture();
                }
            }

        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (null == camera) {
            try {
                camera = Camera.open();
                camera.setPreviewDisplay(surfaceHolder);
                initCamera();
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        try {
            camera.stopPreview();
            camera.release();
            camera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void takePicture() {
        camera.takePicture(null, null, new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
//                matrix.setRotate(90);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                        matrix, true);

                String  dirPath = AppTools.getFileSavePath(AppConfig.getContext());
                filePath = dirPath + "/" + System.currentTimeMillis() + ".jpg";
                savePicture(bm, filePath);
//                setResult(RESULT_OK, new Intent().putExtra("filePath", filePath));
//                finish();
                obtainPictureListener.onChangeArticle("");

//                uploadImageToQiNiu(filePath, AppConfig.getStringConfig("token", ""));
            }
        });
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
                    obtainPictureListener.onChangeArticle(key);

                    Log.d("NuoMan", "key: " + key + "\n");
                }
            }, null);
        }

    }

    public void savePicture(Bitmap bm, String pathName) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(pathName);
            bm.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                stream.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }


    //相机参数的初始化设置
    private void initCamera() {

        parameters = camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
//        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        parameters.setPictureSize(640, 480);// 设置保存图片大小
        parameters.setPreviewSize(640, 480);// 设置预览图片大小
//        setDispaly(parameters, camera);
        camera.setParameters(parameters);
        camera.startPreview();
        camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

    }

    //控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }

    }

    //实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
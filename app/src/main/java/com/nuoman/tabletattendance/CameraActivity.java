package com.nuoman.tabletattendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.nuoman.tabletattendance.common.BaseActivity;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 相机
 */
public class CameraActivity extends BaseActivity implements Callback {


    @Bind(R.id.surfaceView1)
    SurfaceView surfaceView;
    @Bind(R.id.photograph_bt)
    Button photographBt;
    @Bind(R.id.cancel_bt)
    Button cancelBt;
    //    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Camera.Parameters parameters;

    public String filePath;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_homework_layout);
        ButterKnife.bind(this);
        init();
    }


    private void init() {
        surfaceView.setFocusable(true);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 设置类型
        surfaceHolder.setKeepScreenOn(true);

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
        if (camera == null) {
            return;
        }
        camera.takePicture(null, null, new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
//                matrix.setRotate(90);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                        matrix, true);

                String dirPath = AppTools.getFileSavePath(AppConfig.getContext());
                filePath = dirPath + "/" + System.currentTimeMillis() + ".jpg";
                savePicture(bm, filePath);
                setResult(RESULT_OK, new Intent().putExtra("filePath", filePath));
                finish();
//                uploadImageToQiNiu(filePath, AppTools.getAcacheData(NuoManConstant.TOKEN));
            }
        });
    }



    public void savePicture(Bitmap bm, String pathName) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(pathName);
            bm.compress(Bitmap.CompressFormat.JPEG, 80, stream);
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
        parameters.setPictureSize(1600, 1200);// 设置保存图片大小
        parameters.setPreviewSize(1600, 1200);// 设置预览图片大小
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

    @OnClick({R.id.photograph_bt, R.id.cancel_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photograph_bt:
                takePicture();
                break;
            case R.id.cancel_bt:
                surfaceDestroyed(surfaceHolder);
                finish();
                break;
        }
    }
}

package com.nuoman.tabletattendance.common.utils;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BasePresenter;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.model.LoginInfoModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * AUTHOR: Alex
 * DATE: 12/11/2015 09:47
 */
public class AppTools {


    /**
     * Bean Convert Map
     *
     * @param targetBean
     * @return
     */
    public static Map<String, String> toMap(Object targetBean) {

        Map<String, String> result = new IdentityHashMap<String, String>();
//        result.putAll(AppTools.toMap());
        Method[] methods = targetBean.getClass().getDeclaredMethods();

        for (Method method : methods) {
            try {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);

                    Object value = method.invoke(targetBean, (Object[]) null);
                    result.put(field, null == value ? "" : value.toString());
                }
            } catch (Exception e) {
            }
        }


        return result;
    }

    /**
     * Bean Convert Map
     *
     * @return
     */
//    public static Map<String, String> toMap() {
//        BaseTransModel commonBean = new BaseTransModel();
//        Map<String, String> result = new IdentityHashMap<>();
//        Method[] commonMethods = commonBean.getClass().getDeclaredMethods();
//
//        for (Method method : commonMethods) {
//            try {
//                if (method.getName().startsWith("get")) {
//                    String field = method.getName();
//                    field = field.substring(field.indexOf("get") + 3);
//                    field = field.toLowerCase().charAt(0) + field.substring(1);
//
//                    Object value = method.invoke(commonBean, (Object[]) null);
//                    result.put(field, null == value ? "" : value.toString());
//                }
//            } catch (Exception e) {
//            }
//        }
//
//        return result;
//    }

    /**
     * 方法名:获取文件存取路径
     * <p>
     * 功能说明：获取文件存取路径
     * </p>
     *
     * @return
     */
    public static String getFileSavePath(Context context) {
        if (AppTools.getSDPath().equals("")) {
            return context.getFilesDir().getPath();
        }
        File file = new File(AppTools.getSDPath() + "/pictures");
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file.getPath();
            }
            return "";
        }
        return file.getPath();
    }

    /**
     * 方法名: 判断SD卡是否存在
     * <p>
     * 功能说明： 判断SD卡是否存在, 如果存在返回SD卡路径 , 如果不存在返回路径为空
     * </p>
     *
     * @return
     */
    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            File sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return "";
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public static void disableShowSoftInput(EditText editTv) {
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

    /**
     * 获取登陆信息
     *
     * @return
     */
    public static LoginInfoModel getLogInfo() {

//        LoginInfoModel model = BasePresenter.gson.fromJson(AppConfig.getStringConfig(NuoManService.LOGIN, ""), new TypeToken<LoginInfoModel>() {
//        }.getType());
        LoginInfoModel model = BasePresenter.gson.fromJson(ACache.get(AppConfig.getContext()).getAsString(NuoManService.LOGIN), new TypeToken<LoginInfoModel>() {
        }.getType());
        return model;
    }

    /**
     * 从assets 文件夹中读取图片
     */
    public static Drawable loadImageFromAsserts(final Context ctx, String fileName) {
        try {
            InputStream is = ctx.getResources().getAssets().open(fileName);
            return Drawable.createFromStream(is, null);
        } catch (IOException e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //设置应用中的亮度 不保存
    public static void Brightness(Activity context) {
        boolean autoBrightness = BrightnessTools.isAutoBrightness(context.getContentResolver());
        if (autoBrightness) {
            BrightnessTools.stopAutoBrightness(context);
        }
        BrightnessTools.setBrightness(context, 255);
    }

    //获取当前亮度 并设置最大的亮度保存
    public static void saveBrightness(Context context, int brightValue) {
        int screenBrightness = BrightnessTools.getScreenBrightness(context);
        BrightnessTools.saveBrightness(context.getContentResolver(), brightValue);
    }

    //开启关闭自动调节亮度
    public static void offAuto(Activity context) {
        boolean autoBrightness = BrightnessTools.isAutoBrightness(context.getContentResolver());
        if (autoBrightness) {
            BrightnessTools.stopAutoBrightness(context);
        } else {
            BrightnessTools.startAutoBrightness(context);
        }
    }

    /**
     * 方法名: getVersionCode
     * <p>
     * 功能说明： 返回当前应用的版本号
     * </p>
     *
     * @return
     */
    public static int getVersionCode() {
        int verCode = 0;
        try {
            verCode = AppConfig.getContext().getPackageManager()
                    .getPackageInfo(AppConfig.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

    /*
    删除目录下的所有文件
     */

    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }


    /**
     * 获取时间
     *
     * @param textView
     */
    public static void obtainTime(final Activity activity, final TextView textView, final int index) {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                textView.setText(inspectTime(hourOfDay, minute));
                if (index == 1) {
                    AppConfig.setStringConfig(NuoManConstant.DOWN_SCREEN_LIGHT, textView.getText().toString());
                } else if (index == 2) {
                    AppConfig.setStringConfig(NuoManConstant.REBACK_SCREEN_LIGHT, textView.getText().toString());
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }


    /**
     * 格式化事件
     *
     * @param hourOfDay
     * @param minute
     * @return
     */
    public static String inspectTime(int hourOfDay, int minute) {

        String tempHour = String.valueOf((hourOfDay));
        String tempMin = String.valueOf((minute));

        if ((hourOfDay) < 10) {
            tempHour = "0" + String.valueOf(hourOfDay);
        }
        if (minute < 10) {
            tempMin = "0" + String.valueOf((minute));
        }
        String timeStr = tempHour + ":" + tempMin;

        return timeStr;

    }


    /**
     * 获取ACACHE实力
     *
     * @return
     */
    public static ACache getAcache() {

        return ACache.get(AppConfig.getContext());
    }

    /**
     * 存数据
     *
     * @param key
     * @param value
     */
    public static void acachePut(String key, String value) {
        getAcache().put(key, value);
    }

    /**
     * 获取String类型数据
     *
     * @param key
     * @return
     */
    public static String getAcacheData(String key) {

        String result = getAcache().getAsString(key);
        if (TextUtils.isEmpty(result)) {
            result = "";
        }
        return result;
    }

}

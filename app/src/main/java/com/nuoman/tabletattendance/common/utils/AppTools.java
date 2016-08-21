package com.nuoman.tabletattendance.common.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.InputType;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.BasePresenter;
import com.nuoman.tabletattendance.model.LoginInfoModel;

import java.io.File;
import java.lang.reflect.Method;
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
     * @return
     */
    public static LoginInfoModel getLogInfo() {

        LoginInfoModel model = BasePresenter.gson.fromJson(AppConfig.getStringConfig(NuoManService.LOGIN, ""), new TypeToken<LoginInfoModel>() {
        }.getType());

        return model;
    }


}

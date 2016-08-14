package com.nuoman.tabletattendance.common;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.common.utils.AppTools;

/**
 * 【CommonPresenter presenter】
 * AUTHOR: Alex
 * DATE: 22/10/2015 10:57
 */
public class CommonPresenter extends BasePresenter {


    public ICommonAction iCommonAction;

    public CommonPresenter(ICommonAction iCommonAction) {
        this.iCommonAction = iCommonAction;
    }



    /**
     * 有参公共方法调用
     *
     * @param methodName
     * @param model
     * @param typeToken
     */
    public void invokeInterfaceObtainData(String methodName, Object model, TypeToken<?> typeToken) {
        commonApi(methodName, AppTools.toMap(model), typeToken);
    }



    @Override
    public void onResponse(String methodName, Object object, int status) {

        switch (methodName) {
            default:
                iCommonAction.obtainData(object, methodName,status);
                break;

        }
    }


}

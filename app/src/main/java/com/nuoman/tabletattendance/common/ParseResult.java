package com.nuoman.tabletattendance.common;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.nuoman.tabletattendance.common.utils.ACache;
import com.nuoman.tabletattendance.common.utils.AppConfig;

/**
 * AUTHOR: Alex
 * DATE: 17/11/2015 19:35
 */
public class ParseResult {

    public static ParseResult parseResult;

    public static ParseResult instance() {

        if (parseResult == null) {
            parseResult = new ParseResult();
        }
        return parseResult;
    }

    /**
     * 服务器返回数据解析
     *
     * @param receivedStr
     * @param typeToken
     * @return
     */
    public Object requestServer(String methodName, String receivedStr, TypeToken<?> typeToken) {

        Object result = null;
        if (!TextUtils.isEmpty(receivedStr)) {
            result = BasePresenter.gson.fromJson(receivedStr, typeToken.getType());
            ACache.get(AppConfig.getContext()).put(methodName, receivedStr);//缓存到文件
//            AppConfig.setStringConfig(methodName, receivedStr);//缓存接口数据内容
        }

        return result;
    }


}

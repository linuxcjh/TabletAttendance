package com.nuoman.tabletattendance.api;

import com.squareup.okhttp.RequestBody;

import java.util.Map;

import retrofit.Call;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Path;

/**
 * Total API
 * AUTHOR: Alex
 * DATE: 21/10/2015 18:44
 */
public interface NuoManAPI {

    String URL ="http://123.57.34.179/attendence_sys/";

    /**
     * 通用接口调用
     *
     * @param methodName //     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("{methodName}")
    Call<String> serviceAPI(@Path("methodName") String methodName, @FieldMap() Map<String, String> map);

    //图片上传

    @Multipart
    @POST("uploadFile")
    Call<String> uploadFile(@PartMap Map<String, RequestBody> params);

}



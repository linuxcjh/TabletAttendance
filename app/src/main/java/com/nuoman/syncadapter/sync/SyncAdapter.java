package com.nuoman.syncadapter.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nuoman.syncadapter.pro.NoteProviderMetaData;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.NuoManConstant;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.common.utils.BaseUtil;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AUTHOR: Alex
 * DATE: 14/8/2016 15:35
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter implements ICommonAction {

    private CommonPresenter commonPresenter;

    private ContentProviderClient provider;

    private int i = 0;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        commonPresenter = new CommonPresenter(this);

    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        commonPresenter = new CommonPresenter(this);


    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        this.provider = provider;

        //查询是否有未上传的打卡记录
        List<BaseTransModel> transModels = query(provider);

        //上传未上传的数据
        upLoadPunchCardInfo(transModels);

        //定时任务 清空图片缓存
        if (BaseUtil.getTime(BaseUtil.HH_MM).equals(NuoManConstant.CLEAR_PICTHRE_CACHE_TIME)) {
            //判断是否有未上传的图片
            if (transModels.size() == 0) {
                AppTools.deleteAllFiles(new File(AppTools.getFileSavePath(AppConfig.getContext())));
                Log.d("SYNC", "CLEAR_PICTHRE_CACHE Picture   ---  " + BaseUtil.getTime(BaseUtil.HH_MM));

            }
        }

        //更新时间
        String updateTime = AppTools.getLogInfo().getUpdateDataTime();
        if (TextUtils.isEmpty(updateTime)) {
            updateTime = "23:00";
        }
        //定时任务 更新数据
        if (BaseUtil.getTime(BaseUtil.HH_MM).equals(updateTime)) {
            BaseTransModel refreshModel = new BaseTransModel();

            refreshModel.setTel(AppTools.getAcacheData(NuoManConstant.USER_NAME));
            refreshModel.setMachineNo(AppTools.getAcacheData(NuoManConstant.USER_MAC));

            commonPresenter.invokeInterfaceObtainData(false, "loginCtrl", NuoManService.LOGIN, refreshModel, new TypeToken<LoginInfoModel>() {
            });
            Log.d("SYNC", "CLEAR_PICTURE_CACHE UPDATA   ---  " + BaseUtil.getTime(BaseUtil.HH_MM));
        }

        Log.d("SYNC", "onPerformSync   ---  " + BaseUtil.getTime(BaseUtil.HH_MM)+"  ==  "+AppTools.getAcacheData(NuoManConstant.USER_MAC));

    }


    @Override
    public void obtainData(Object data, String methodIndex, int status, Map<String, String> parameterMap) {

        switch (methodIndex) {
            case NuoManService.GETTOKEN:
                if (data != null) {
                    BaseReceivedModel model = (BaseReceivedModel) data;
                    AppTools.acachePut(NuoManConstant.TOKEN,model.getToken());
//                    AppConfig.setStringConfig("token", model.getToken());
                }
                break;
            case NuoManService.WRITEATTLOG:
                BaseReceivedModel m = (BaseReceivedModel) data;
                if (m.isSuccess()) {
                    String id = "";
                    for (String key : parameterMap.keySet()) {
                        if (key.equals("unique_id")) {
                            id = parameterMap.get(key);
                        }
                        Log.d("SYNC", "parameterMap   ---  " + "key= " + key + " and value= " + parameterMap.get(key));
                    }

                    Log.d("SYNC", "obtainData   ---  id :" + id + " ====== " + BaseUtil.getTime(BaseUtil.HH_MM));
                    try {
                        provider.delete(Uri.parse("content://" + NoteProviderMetaData.AUTHORITY + "/notes" + "/" + id), null, null);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case NuoManService.LOGIN:

                break;
        }
    }


    private List<BaseTransModel> query(ContentProviderClient provider) {
        List<BaseTransModel> transModels = new ArrayList<>();
        LoginInfoModel loginInfo = AppTools.getLogInfo();
        Cursor cursor = null;
        try {
            cursor = provider.query(NoteProviderMetaData.NoteTableMetaData.CONTENT_URI, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData._ID));
                String punchCardNo = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.PUNCH_CARD_NO));
                String punchTime = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.PUNCH_TIME));
                String punchImagePath = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.PUNCH_IMAGE_PATH));

                Log.d("SYNC", "id: " + id);
                Log.d("SYNC", "punchCardNo: " + punchCardNo);
                Log.d("SYNC", "punchTime: " + punchTime);
                Log.d("SYNC", "punchImagePath: " + punchImagePath);

                BaseTransModel transModel = new BaseTransModel();

                transModel.setUnique_id(id);
                transModel.setMachineNo(loginInfo.getMachineId());
                transModel.setMachineId(loginInfo.getMachineId());
                transModel.setTel(AppTools.getAcacheData(NuoManConstant.USER_NAME));
                transModel.setCardNo(punchCardNo);
                transModel.setAttDate(punchTime);
                transModel.setUnique_id(id);
                transModel.setImagePath(punchImagePath);


                transModels.add(transModel);
                cursor.moveToNext();
            }

            cursor.close();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return transModels;
    }


    /**
     * 上传打卡信息
     */
    private void upLoadPunchCardInfo(List<BaseTransModel> transModels) {


        for (int i = 0; i < transModels.size(); i++) {

            if (!TextUtils.isEmpty(AppTools.getAcacheData(NuoManConstant.TOKEN))) {
                uploadImageToQiNiu(transModels.get(i).getImagePath(), AppTools.getAcacheData(NuoManConstant.TOKEN), transModels.get(i));
            } else { //重新请求token
                commonPresenter.invokeInterfaceObtainData(false, "qiniuCtrl", NuoManService.GETTOKEN, null, new TypeToken<BaseReceivedModel>() {
                });
            }

        }

    }

    /**
     * 上传图片到七牛
     *
     * @param filePath 要上传的图片路径
     * @param token    在七牛官网上注册的token
     */

    private void uploadImageToQiNiu(String filePath, String token, final BaseTransModel model) {

        try {
            UploadManager uploadManager = new UploadManager();
            File file = new File(filePath);
            if (file.exists()) {

                uploadManager.put(filePath, file.getName(), token, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        // info.error中包含了错误信息，可打印调试
                        // 上传成功后将key值上传到自己的服务器

                        model.setAttPicUrl(key);
                        commonPresenter.invokeInterfaceObtainData(true, "attDataCtrl", NuoManService.WRITEATTLOG, model, new TypeToken<BaseReceivedModel>() {
                        });

                    }
                }, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

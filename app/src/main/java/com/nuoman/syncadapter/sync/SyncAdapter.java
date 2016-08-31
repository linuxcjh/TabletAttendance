package com.nuoman.syncadapter.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.nuoman.syncadapter.pro.NoteProviderMetaData;
import com.nuoman.tabletattendance.api.NuoManService;
import com.nuoman.tabletattendance.common.CommonPresenter;
import com.nuoman.tabletattendance.common.ICommonAction;
import com.nuoman.tabletattendance.common.utils.AppConfig;
import com.nuoman.tabletattendance.common.utils.AppTools;
import com.nuoman.tabletattendance.common.utils.BaseUtil;
import com.nuoman.tabletattendance.model.BaseReceivedModel;
import com.nuoman.tabletattendance.model.BaseTransModel;
import com.nuoman.tabletattendance.model.LoginInfoModel;

/**
 * AUTHOR: Alex
 * DATE: 14/8/2016 15:35
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter implements ICommonAction {

    private CommonPresenter commonPresenter;
    private BaseTransModel transModel = new BaseTransModel();

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

        if (BaseUtil.getTime(BaseUtil.HH_MM).equals("12:00")) {//定时任务
            AppTools.saveBrightness(getContext(), 50);
            Log.d("SYNC", "50   ---  " + BaseUtil.getTime(BaseUtil.HH_MM));

        }
//        query(provider);
//        executeTask();
        Log.d("SYNC", "onPerformSync   ---  " + BaseUtil.getTime(BaseUtil.HH_MM));
    }

    private void executeTask() {
        LoginInfoModel loginInfo = AppTools.getLogInfo();
//        transModel.setMachineNo(loginInfo.getMachineId());
//        transModel.setMachineId(loginInfo.getMachineId());
//        transModel.setTel(AppConfig.getStringConfig(NuoManConstant.USER_NAME, ""));
//        transModel.setCardNo(AppConfig.getStringConfig(NuoManConstant.CARD_ID, ""));
//        transModel.setAttDate(BaseUtil.getTime(BaseUtil.YYYY_MM_DD_HH_MM_SS));
//        transModel.setAttPicUrl(pictureId);
        commonPresenter.invokeInterfaceObtainData(true, "attDataCtrl", NuoManService.WRITEATTLOG, transModel, new TypeToken<BaseReceivedModel>() {
        });
    }

    @Override
    public void obtainData(Object data, String methodIndex, int status) {

        switch (methodIndex) {
            case NuoManService.WRITEATTLOG:
                BaseReceivedModel m = (BaseReceivedModel) data;
                if (m.isSuccess()) {
                    Toast.makeText(AppConfig.getContext(), "打卡成功", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void query(ContentProviderClient provider) {

        Cursor cursor = null;
        try {
            cursor = provider.query(NoteProviderMetaData.NoteTableMetaData.CONTENT_URI, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData._ID));

                String title = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.NOTE_TITLE));
                String content = cursor.getString(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.NOTE_CONTENT));
                long createDate = cursor.getLong(cursor.getColumnIndex(NoteProviderMetaData.NoteTableMetaData.CREATE_DATE));

                Log.d("SYNC", "id: " + id);
                Log.d("SYNC", "title: " + title);
                Log.d("SYNC", "content: " + content);
                Log.d("SYNC", "createDate: " + createDate);


                cursor.moveToNext();
            }

            cursor.close();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

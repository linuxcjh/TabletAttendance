//package com.nuoman.tabletattendance.common.utils;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.hardware.Camera;
//import android.util.Log;
//
//import com.nuoman.tabletattendance.common.function.picture.PictureUtil;
//import com.nuoman.tabletattendance.common.function.util.BaseUtil;
//import com.nuoman.tabletattendance.common.function.util.FilePath;
//import com.nuoman.tabletattendance.common.function.util.FileUtil;
//import com.nuoman.tabletattendance.login.entity.PeopleEntity;
//
//import java.io.File;
//import java.io.IOException;
//
///**
// * 照相完成回调
// * Created by 杨小过 on 2016/6/2.
// */
//public class TakePictureCallback implements Camera.PictureCallback {
//
//    private OnPunchCardListener mOnPunchCardListener;
//    private String mCardNo;
//    private String TAG = getClass().getName();
//    private int mPunchType;
//
//    public TakePictureCallback(String mCardNo, int mPunchType, OnPunchCardListener onPunchCardListener) {
//        this.mCardNo = mCardNo;
//        this.mPunchType = mPunchType;
//        this.mOnPunchCardListener = onPunchCardListener;
//    }
//
//    @Override
//    public void onPictureTaken(byte[] data, Camera camera) {
//        Log.d(TAG, "onPictureTaken...");
//        Bitmap bitmap = PictureUtil.rotateBitmap(BitmapFactory.decodeByteArray(data, 0, data.length), 180);
//        PeopleEntity peopleEntity = getPeopleEntity(mCardNo);
//        if (mOnPunchCardListener != null)
//            mOnPunchCardListener.punchCardComplete(peopleEntity, bitmap, mPunchType);
//        byte[] bitmapData = PictureUtil.compressImage(bitmap);//对图片进行压缩
//        FileUtil.createFolder(FilePath.REAL_TIEM_PICTURE);//创建保存文件夹
//        String path = FilePath.REAL_TIEM_PICTURE + File.separator + mCardNo + BaseUtil.getTime("yyyy-MM-dd HH_mm_ss");
//        try {
//            FileUtil.saveFileFromBytes(path, bitmapData);
//            FileUtil.renameFile(path, path + ".jpg");
//            Log.d(TAG, "打卡后保存照片成功");
//        } catch (Exception e) {
//            Log.e(TAG, "打卡后保存图片失败");
//            e.printStackTrace();
//        }
//    }
//
//    private PeopleEntity getPeopleEntity(String cardNo) {
//        try {
//            for (PeopleEntity peopleEntity : PeopleEntity.getPeopleEntity()) {
//                if (peopleEntity.getCardno().equals(cardNo)) {
//                    return peopleEntity;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(TAG, e.toString());
//        }
//        return null;
//    }
//}

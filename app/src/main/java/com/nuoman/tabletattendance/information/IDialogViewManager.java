package com.nuoman.tabletattendance.information;


public interface IDialogViewManager {

    /**
     * 设置正在录音时的dialog界面
     */
    public void showRecordingDialog();

    public void recording();

    /**
     * 取消界面
     */
    public void wantToCancel();

    // 时间过短
    public void tooShort();

    // 隐藏dialog
    public void dimissDialog();

    public void updateVoiceLevel(int level);


}

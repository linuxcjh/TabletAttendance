package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 29/8/2016 10:50
 */
public class ParentInfo {


//    "dataId":"2",
//            "dataName":"爸爸",
//            "userIds":"2_5_6_7_8_9"

    private String dataId;
    private String dataName;
    private String userIds;
    private String headPicUrl;

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }
}

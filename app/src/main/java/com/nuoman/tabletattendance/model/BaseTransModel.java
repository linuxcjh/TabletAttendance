package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 9/8/2016 10:38
 */
public class BaseTransModel {

//    {"machineId":"1","cardNo":"0008124733","attDate":"2016-08-16 11:25:02","attPicUrl":"aaaaaaa.jpg"}
    private String tel;
    private String machineNo;
    private String area;
    private String machineId;
    private String cardNo;

    private String attDate;
    private String attPicUrl;

    private String classId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAttDate() {
        return attDate;
    }

    public void setAttDate(String attDate) {
        this.attDate = attDate;
    }

    public String getAttPicUrl() {
        return attPicUrl;
    }

    public void setAttPicUrl(String attPicUrl) {
        this.attPicUrl = attPicUrl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }
}


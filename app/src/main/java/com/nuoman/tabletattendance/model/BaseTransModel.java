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
    private String imagePath;//应用内使用

    private String attDate;
    private String attPicUrl;

    private String classId;

    private String userId;
    private String kindId;//发起方类型   0：学生向家长发送     1：家长向学生发送    2：教师向家长发送


    //发作业
    private String teacherId;
    private String homeworkPic;

    //版本更新
    private String type;//type取值范围: 1: 大蓝 2：小蓝  3：大黑


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getHomeworkPic() {
        return homeworkPic;
    }

    public void setHomeworkPic(String homeworkPic) {
        this.homeworkPic = homeworkPic;
    }

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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


package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 24/8/2016 21:58
 */
public class ReceivedUnreadInforModel {

    private String headPicUrl;
    private String studentName;
    private String attStatus;

    public String getAttStatus() {
        return attStatus;
    }

    public void setAttStatus(String attStatus) {
        this.attStatus = attStatus;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}

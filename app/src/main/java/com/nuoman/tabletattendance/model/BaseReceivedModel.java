package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 9/8/2016 10:38
 */
public class BaseReceivedModel {

//    {"role":"1","schoolId":"2","schoolname":"和安全","superpass":"0612141732211"}
    private String role;
    private String schoolId;
    private String schoolname;
    private String superpass;


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getSuperpass() {
        return superpass;
    }

    public void setSuperpass(String superpass) {
        this.superpass = superpass;
    }
}


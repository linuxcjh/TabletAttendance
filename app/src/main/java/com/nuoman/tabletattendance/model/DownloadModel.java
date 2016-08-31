package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 31/8/2016 15:01
 */
public class DownloadModel {

//    {"durl":"http://api.nuomankeji.com/AppFiles/att_soft/Pop_Vm.apk",
//            "intro":"测试版本","version":"1.0.0","version_date":"2016-08-29"}



    private String durl;
    private String intro;
    private String version;
    private String version_date;

    public String getDurl() {
        return durl;
    }

    public void setDurl(String durl) {
        this.durl = durl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion_date() {
        return version_date;
    }

    public void setVersion_date(String version_date) {
        this.version_date = version_date;
    }
}


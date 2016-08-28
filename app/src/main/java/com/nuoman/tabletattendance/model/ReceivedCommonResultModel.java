package com.nuoman.tabletattendance.model;

import java.util.List;

/**
 * AUTHOR: Alex
 * DATE: 24/8/2016 21:56
 */
public class ReceivedCommonResultModel {
//    {
//        "msg":"success", "obj":[{
//        "headPicUrl":
//        "http://7xtr6l.com1.z0.glb.clouddn.com/o_1an1n4te51nfpe4te0c15f51qouh.jpg", "studentName":
//        "哥 I l"
//    },{
//        "headPicUrl":
//        "http://7xtr6l.com1.z0.glb.clouddn.com/o_1an1n4te51nfpe4te0c15f51qouh.jpg", "studentName":
//        "弟弟you to "
//    }],"success":true
//    }

    private String msg;
    private String success;
    private List<ReceivedUnreadInforModel> obj;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ReceivedUnreadInforModel> getObj() {
        return obj;
    }

    public void setObj(List<ReceivedUnreadInforModel> obj) {
        this.obj = obj;
    }
}

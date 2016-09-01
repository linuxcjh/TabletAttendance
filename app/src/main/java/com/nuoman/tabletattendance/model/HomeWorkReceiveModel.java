package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 9/8/2016 10:38
 */
public class HomeWorkReceiveModel {

//    [{"msg":"success","obj":null,"success":true}]
    private String token;
    private boolean success;
    private String msg;
    private Teacher obj;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Teacher getObj() {
        return obj;
    }

    public void setObj(Teacher obj) {
        this.obj = obj;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public class Teacher{
        private String teacherId;

        public String getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }
    }
}


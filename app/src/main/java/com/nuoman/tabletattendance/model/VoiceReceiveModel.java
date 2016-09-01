package com.nuoman.tabletattendance.model;

import java.util.List;

/**
 * AUTHOR: Alex
 * DATE: 9/8/2016 10:38
 */
public class VoiceReceiveModel {

    //    [{"msg":"success","obj":null,"success":true}]
    private String token;
    private boolean success;
    private String msg;
    private List<VoiceItemModel> obj;

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

    public List<VoiceItemModel> getObj() {
        return obj;
    }

    public void setObj(List<VoiceItemModel> obj) {
        this.obj = obj;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public class VoiceItemModel {
//        "audioFile":"sadfasdfasdfsadfcxzvsqwerqwerqwerqwerqwe",
//                "audioid":"3",
//                "fdate":"2016-07-26 09:43:27",
//                "kind":"1",
//                "state":"0",
//                "toUserId":"2",
//                "toUserName":"TestParent",
//                "type":"t",
//                "userId":"2",
//                "userName":"TestStudent"
        private String audioFile;
        private String fdate;
        private String kind;
        private String state;
        private String toUserId;
        private String toUserName;
        private String type;
        private String userId;
        private String userName;
        private  float time;



        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public String getAudioFile() {
            return audioFile;
        }

        public void setAudioFile(String audioFile) {
            this.audioFile = audioFile;
        }

        public String getFdate() {
            return fdate;
        }

        public void setFdate(String fdate) {
            this.fdate = fdate;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public String getToUserName() {
            return toUserName;
        }

        public void setToUserName(String toUserName) {
            this.toUserName = toUserName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}


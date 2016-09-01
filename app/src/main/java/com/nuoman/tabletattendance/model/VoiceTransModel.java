package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 9/8/2016 10:38
 */
public class VoiceTransModel {

    private String userId;         //即发送方 userId
    private String audioFile;      //即文件名
    private String destId;        //即接收方 userId，多选或全选 以 _ 连接 如  111_222_333
    private String kind;       //即发送方类型   0：学生向家长发送     1：家长向学生发送    2：教师向家长发送
    private String type;  //消息类型   v：语音消息   t： 文字消息"
    private float time;//消息时长

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


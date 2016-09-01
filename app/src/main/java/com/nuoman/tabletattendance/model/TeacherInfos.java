package com.nuoman.tabletattendance.model;

import java.io.Serializable;
import java.util.List;

public class TeacherInfos implements Serializable{

    private List<CardNoModel> cardNoList;
    private String teacherName;
    private String teacherId;
    
    
	public TeacherInfos () {
		
	}


    public List<CardNoModel> getCardNoList() {
        return cardNoList;
    }

    public void setCardNoList(List<CardNoModel> cardNoList) {
        this.cardNoList = cardNoList;
    }

    public String getTeacherName() {
        return this.teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }


    
}

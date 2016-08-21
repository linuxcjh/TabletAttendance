package com.nuoman.tabletattendance.model;

import java.util.ArrayList;

public class TeacherInfos {

    private ArrayList<CardNoModel> cardNoList;
    private String teacherName;
    private String teacherId;
    
    
	public TeacherInfos () {
		
	}


    public ArrayList<CardNoModel> getCardNoList() {
        return cardNoList;
    }

    public void setCardNoList(ArrayList<CardNoModel> cardNoList) {
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

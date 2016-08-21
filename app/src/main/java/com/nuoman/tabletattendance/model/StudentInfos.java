package com.nuoman.tabletattendance.model;

import java.util.ArrayList;

public class StudentInfos {
	
    private String studentId;
    private String className;
    private String studentName;
    private String gradeName;
    private ArrayList<CardNoModel> cardNoList;
    
    
	public StudentInfos () {
		
	}	
        

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGradeName() {
        return this.gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public ArrayList<CardNoModel> getCardNoList() {
        return cardNoList;
    }

    public void setCardNoList(ArrayList<CardNoModel> cardNoList) {
        this.cardNoList = cardNoList;
    }
}

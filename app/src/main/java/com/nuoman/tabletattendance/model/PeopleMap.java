package com.nuoman.tabletattendance.model;

import java.util.ArrayList;

public class PeopleMap {
	
    private ArrayList<StudentInfos> studentInfos;
    private ArrayList<TeacherInfos> teacherInfos;
    
    
	public PeopleMap () {
		
	}	
        

    public ArrayList<StudentInfos> getStudentInfos() {
        return this.studentInfos;
    }

    public void setStudentInfos(ArrayList<StudentInfos> studentInfos) {
        this.studentInfos = studentInfos;
    }

    public ArrayList<TeacherInfos> getTeacherInfos() {
        return this.teacherInfos;
    }

    public void setTeacherInfos(ArrayList<TeacherInfos> teacherInfos) {
        this.teacherInfos = teacherInfos;
    }


    
}

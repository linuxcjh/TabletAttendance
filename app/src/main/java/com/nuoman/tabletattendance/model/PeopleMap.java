package com.nuoman.tabletattendance.model;

import java.util.ArrayList;
import java.util.List;

public class PeopleMap {
	
    private List<StudentInfos> studentInfos;
    private List<TeacherInfos> teacherInfos;
    
    
	public PeopleMap () {
		
	}	
        

    public List<StudentInfos> getStudentInfos() {
        return this.studentInfos;
    }

    public void setStudentInfos(List<StudentInfos> studentInfos) {
        this.studentInfos = studentInfos;
    }

    public List<TeacherInfos> getTeacherInfos() {
        return this.teacherInfos;
    }

    public void setTeacherInfos(ArrayList<TeacherInfos> teacherInfos) {
        this.teacherInfos = teacherInfos;
    }


    
}

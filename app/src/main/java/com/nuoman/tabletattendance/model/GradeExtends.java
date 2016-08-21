package com.nuoman.tabletattendance.model;

import java.util.ArrayList;

public class GradeExtends {
	
    private String id;
    private String specialId;
    private String gradeName;
    private ArrayList<ClassList> classList;
    
    
	public GradeExtends () {
		
	}	
        

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialId() {
        return this.specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public String getGradeName() {
        return this.gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public ArrayList<ClassList> getClassList() {
        return this.classList;
    }

    public void setClassList(ArrayList<ClassList> classList) {
        this.classList = classList;
    }


    
}

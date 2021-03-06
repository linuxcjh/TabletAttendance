package com.nuoman.tabletattendance.model;

import java.util.List;

public class LoginInfoModel {

    private List<GradeExtends> gradeExtends;
    private String machineId;
    private PeopleMap peopleMap;
    private String schoolName;
    private String schoolId;
    private String areaId;

    private String superPass; //密码
    private String updateDataTime;//更新时间

    public String getUpdateDataTime() {
        return updateDataTime;
    }

    public void setUpdateDataTime(String updateDataTime) {
        this.updateDataTime = updateDataTime;
    }

    public String getSuperPass() {
        return superPass;
    }

    public void setSuperPass(String superPass) {
        this.superPass = superPass;
    }

    public LoginInfoModel() {

    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public List<GradeExtends> getGradeExtends() {
        return this.gradeExtends;
    }

    public void setGradeExtends(List<GradeExtends> gradeExtends) {
        this.gradeExtends = gradeExtends;
    }

    public String getMachineId() {
        return this.machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public PeopleMap getPeopleMap() {
        return this.peopleMap;
    }

    public void setPeopleMap(PeopleMap peopleMap) {
        this.peopleMap = peopleMap;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolId() {
        return this.schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }


}

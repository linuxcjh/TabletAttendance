package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 21/8/2016 17:31
 */
public class ReceivedWeatherModel {

//    [
//
//    {
//        "dayShape":"", "daytemp":"", "daywind":"|", "nightShape":"00&晴", "nighttemp":
//        "27", "nightwind":"东风|微风", "time":"18:00"
//    }
//
//    ]

    private String dayShape;
    private String daytemp;
    private String daywind;
    private String nightShape;
    private String nighttemp;
    private String nightwind;
    private String time;


    public String getDayShape() {
        return dayShape;
    }

    public void setDayShape(String dayShape) {
        this.dayShape = dayShape;
    }

    public String getDaytemp() {
        return daytemp;
    }

    public void setDaytemp(String daytemp) {
        this.daytemp = daytemp;
    }

    public String getDaywind() {
        return daywind;
    }

    public void setDaywind(String daywind) {
        this.daywind = daywind;
    }

    public String getNightShape() {
        return nightShape;
    }

    public void setNightShape(String nightShape) {
        this.nightShape = nightShape;
    }

    public String getNighttemp() {
        return nighttemp;
    }

    public void setNighttemp(String nighttemp) {
        this.nighttemp = nighttemp;
    }

    public String getNightwind() {
        return nightwind;
    }

    public void setNightwind(String nightwind) {
        this.nightwind = nightwind;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

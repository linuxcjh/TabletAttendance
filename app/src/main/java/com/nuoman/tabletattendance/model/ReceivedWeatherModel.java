package com.nuoman.tabletattendance.model;

/**
 * AUTHOR: Alex
 * DATE: 21/8/2016 17:31
 */
public class ReceivedWeatherModel {

//{"area":"西安","time":"18:00","weatherShape":"00","weatherSituation":"晴","weatherTemp":"20℃","weatherWind":""}
    private String area;
    private String time;
    private String weatherShape;
    private String weatherSituation;
    private String weatherTemp;
    private String weatherWind;


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeatherShape() {
        return weatherShape;
    }

    public void setWeatherShape(String weatherShape) {
        this.weatherShape = weatherShape;
    }

    public String getWeatherSituation() {
        return weatherSituation;
    }

    public void setWeatherSituation(String weatherSituation) {
        this.weatherSituation = weatherSituation;
    }

    public String getWeatherTemp() {
        return weatherTemp;
    }

    public void setWeatherTemp(String weatherTemp) {
        this.weatherTemp = weatherTemp;
    }

    public String getWeatherWind() {
        return weatherWind;
    }

    public void setWeatherWind(String weatherWind) {
        this.weatherWind = weatherWind;
    }
}

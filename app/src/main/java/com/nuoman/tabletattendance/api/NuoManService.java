package com.nuoman.tabletattendance.api;

/**
 * AUTHOR: Alex
 * DATE: 17/11/2015 21:34
 */
public interface NuoManService {



    //登陆
    String LOGIN="Login";

    //7牛token
    String GETTOKEN="GetToken";

    //获取天气
    String GETWEATHERFORONEDAY="GetWeatherForOneDay";

    //上传打卡信息
    String WRITEATTLOG = "WriteAttLog";

    //获取未读消息
    String GETUNREADMSG="GetUnReadMsg";

    //获取家长列表
    String GETPARENTSBYCARDNO="GetParentsByCardNo";

    //上传作业
    String SAVEHOMEWORK= "SaveHomework";

    //发作业鉴权
    String  GETTEACHERID="GetTeacherId";

    //版本更新
    String GETUPDATEINFO="GetUpdateInfo";

}

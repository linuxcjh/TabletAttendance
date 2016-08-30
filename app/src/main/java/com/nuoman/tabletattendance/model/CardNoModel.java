package com.nuoman.tabletattendance.model;

import java.io.Serializable;

/**
 * AUTHOR: Alex
 * DATE: 20/8/2016 16:03
 */
public class CardNoModel implements Serializable{

//    "cardNo":"0087301181",
//            "cardRole":"家长"

    private String cardNo;
    private String cardRole;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardRole() {
        return cardRole;
    }

    public void setCardRole(String cardRole) {
        this.cardRole = cardRole;
    }
}



package com.oyourneed.model;

import java.util.ArrayList;

public class OtpModel {
    private String response;
    public ArrayList<Forgot> userMsgList;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<Forgot> getResult() {
        return userMsgList;
    }

    public void setResult(ArrayList<Forgot> userMsgList) {
        this.userMsgList = userMsgList;
    }

}


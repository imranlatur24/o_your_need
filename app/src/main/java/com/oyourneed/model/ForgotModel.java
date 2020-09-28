package com.oyourneed.model;

import java.util.ArrayList;

public class ForgotModel {

    private String response;
    public ArrayList<Forgot> login;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<Forgot> getLogin() {
        return login;
    }

    public void setLogin(ArrayList<Forgot> login) {
        this.login = login;
    }
}

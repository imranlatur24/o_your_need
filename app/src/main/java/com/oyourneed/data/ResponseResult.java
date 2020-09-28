package com.oyourneed.data;

import com.oyourneed.model.*;

import java.util.ArrayList;

public class ResponseResult {

    private String response;
    private String userMsgList;
    private String place_order;
    public ArrayList<User> UserRegList;
    public ArrayList<User> login;
   // public ArrayList<User> VersionDetails;
    public ArrayList<OrderList> order_list;
    public ArrayList<WorkSpaceCity> work_cities_list;
    public ArrayList<WorkSpaceLocality> locality_list;
    public ArrayList<LocalityList> product_list;
    public ArrayList<CityListModel> cities_list;
    public ArrayList<CountryList> country_list;
    public ArrayList<WorkSpaceData> workspace_list;
    public ArrayList<StateList> state_list;
    public ArrayList<SliderList> slider_list;
    public ArrayList<CategoryList> category_list;
    public ArrayList<OrderListModel> order_product_list;
    public ArrayList<LanguageList> language_list;
    public ArrayList<SupportList> SupportList;
    public ArrayList<PlaceOrder> result_order_place;
    public ArrayList<PlaceOrder> ChangePassList;

    public ArrayList<PlaceOrder> getChangePassList() {
        return ChangePassList;
    }

    public void setChangePassList(ArrayList<PlaceOrder> changePassList) {
        ChangePassList = changePassList;
    }

    public ArrayList<PlaceOrder> getResult_order_place() {
        return result_order_place;
    }

    public String getPlace_order() {
        return place_order;
    }

    public void setPlace_order(String place_order) {
        this.place_order = place_order;
    }

    public void setResult_order_place(ArrayList<PlaceOrder> result_order_place) {
        this.result_order_place = result_order_place;
    }

    public ArrayList<com.oyourneed.model.SupportList> getSupportList() {
        return SupportList;
    }

    public void setSupportList(ArrayList<com.oyourneed.model.SupportList> supportList) {
        SupportList = supportList;
    }

    public String getUserMsgList() {
        return userMsgList;
    }

    public void setUserMsgList(String userMsgList) {
        this.userMsgList = userMsgList;
    }


    private String otp;

    public ArrayList<User> getLogin() {
        return login;
    }

    public void setLogin(ArrayList<User> login) {
        this.login = login;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public ArrayList<LanguageList> getLanguage_list() {
        return language_list;
    }

    public void setLanguage_list(ArrayList<LanguageList> language_list) {
        this.language_list = language_list;
    }

    public ArrayList<OrderListModel> getOrder_product_list() {
        return order_product_list;
    }

    public void setOrder_product_list(ArrayList<OrderListModel> order_product_list) {
        this.order_product_list = order_product_list;
    }

    public ArrayList<OrderList> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(ArrayList<OrderList> order_list) {
        this.order_list = order_list;
    }

    public ArrayList<WorkSpaceData> getWorkspace_list() {
        return workspace_list;
    }

    public void setWorkspace_list(ArrayList<WorkSpaceData> workspace_list) {
        this.workspace_list = workspace_list;
    }

    public ArrayList<WorkSpaceLocality> getLocality_list() {
        return locality_list;
    }

    public void setLocality_list(ArrayList<WorkSpaceLocality> locality_list) {
        this.locality_list = locality_list;
    }

    public ArrayList<WorkSpaceCity> getWork_cities_list() {
        return work_cities_list;
    }

    public void setWork_cities_list(ArrayList<WorkSpaceCity> work_cities_list) {
        this.work_cities_list = work_cities_list;
    }

    public ArrayList<CountryList> getCountry_list() {
        return country_list;
    }

    public void setCountry_list(ArrayList<CountryList> country_list) {
        this.country_list = country_list;
    }

    public ArrayList<LocalityList> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(ArrayList<LocalityList> product_list) {
        this.product_list = product_list;
    }

    public ArrayList<CategoryList> getCategory_list() {
        return category_list;
    }

    public void setCategory_list(ArrayList<CategoryList> category_list) {
        this.category_list = category_list;
    }

    public ArrayList<SliderList> getSlider_list() {
        return slider_list;
    }

    public void setSlider_list(ArrayList<SliderList> slider_list) {
        this.slider_list = slider_list;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<User> getUserRegList() {
        return UserRegList;
    }

    public void setUserRegList(ArrayList<User> userRegList) {
        UserRegList = userRegList;
    }

    public ArrayList<CityListModel> getCities_list() {
        return cities_list;
    }

    public void setCities_list(ArrayList<CityListModel> cities_list) {
        this.cities_list = cities_list;
    }

    public ArrayList<StateList> getState_list() {
        return state_list;
    }

    public void setState_list(ArrayList<StateList> state_list) {
        this.state_list = state_list;
    }
}

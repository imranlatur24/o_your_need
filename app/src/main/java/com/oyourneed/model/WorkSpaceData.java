package com.oyourneed.model;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkSpaceData implements Serializable {

    private String work_space_status;

    private String wrok_space_owner_id;

    private String address;

    private String upload_image;

    private String work_space_id;

    private String log;

    private String locality_name;

    private String city_name;
    
    private String is_created_date;

    private String metting_description;

    private String workspace_des;

    private String location_id;

    private String work_space_title;

    private String lat;

    private String category_id;

    private ArrayList<DeskList> desk_list;

    private ArrayList<TimingList> timing_list;

    private ArrayList<AminitiesList> aminities_list;

    private ArrayList<PriceList> price_list;
    
    private ArrayList<SliderList> slider_list;
    
    private ArrayList<OwnerList> owner_list;


    public String getWork_space_status() {
        return work_space_status;
    }

    public void setWork_space_status(String work_space_status) {
        this.work_space_status = work_space_status;
    }

    public String getWrok_space_owner_id() {
        return wrok_space_owner_id;
    }

    public void setWrok_space_owner_id(String wrok_space_owner_id) {
        this.wrok_space_owner_id = wrok_space_owner_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUpload_image() {
        return upload_image;
    }

    public void setUpload_image(String upload_image) {
        this.upload_image = upload_image;
    }

    public String getWork_space_id() {
        return work_space_id;
    }

    public void setWork_space_id(String work_space_id) {
        this.work_space_id = work_space_id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLocality_name() {
        return locality_name;
    }

    public void setLocality_name(String locality_name) {
        this.locality_name = locality_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getIs_created_date() {
        return is_created_date;
    }

    public void setIs_created_date(String is_created_date) {
        this.is_created_date = is_created_date;
    }

    public String getMetting_description() {
        return metting_description;
    }

    public void setMetting_description(String metting_description) {
        this.metting_description = metting_description;
    }

    public String getWorkspace_des() {
        return workspace_des;
    }

    public void setWorkspace_des(String workspace_des) {
        this.workspace_des = workspace_des;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getWork_space_title() {
        return work_space_title;
    }

    public void setWork_space_title(String work_space_title) {
        this.work_space_title = work_space_title;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public ArrayList<DeskList> getDesk_list() {
        return desk_list;
    }

    public void setDesk_list(ArrayList<DeskList> desk_list) {
        this.desk_list = desk_list;
    }

    public ArrayList<TimingList> getTiming_list() {
        return timing_list;
    }

    public void setTiming_list(ArrayList<TimingList> timing_list) {
        this.timing_list = timing_list;
    }

    public ArrayList<AminitiesList> getAminities_list() {
        return aminities_list;
    }

    public void setAminities_list(ArrayList<AminitiesList> aminities_list) {
        this.aminities_list = aminities_list;
    }

    public ArrayList<PriceList> getPrice_list() {
        return price_list;
    }

    public void setPrice_list(ArrayList<PriceList> price_list) {
        this.price_list = price_list;
    }

    public ArrayList<SliderList> getSlider_list() {
        return slider_list;
    }

    public void setSlider_list(ArrayList<SliderList> slider_list) {
        this.slider_list = slider_list;
    }

    public ArrayList<OwnerList> getOwner_list() {
        return owner_list;
    }

    public void setOwner_list(ArrayList<OwnerList> owner_list) {
        this.owner_list = owner_list;
    }
}

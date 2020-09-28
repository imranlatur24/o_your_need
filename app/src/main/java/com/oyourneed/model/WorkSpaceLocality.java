package com.oyourneed.model;

import java.io.Serializable;

public class WorkSpaceLocality implements Serializable {

    private String locality_id;
    private String language_id;
    private String work_city_id;
    private String locality_name;
    private String locality_status;
    private String is_created_date;

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getLocality_id() {
        return locality_id;
    }

    public void setLocality_id(String locality_id) {
        this.locality_id = locality_id;
    }

    public String getWork_city_id() {
        return work_city_id;
    }

    public void setWork_city_id(String work_city_id) {
        this.work_city_id = work_city_id;
    }

    public String getLocality_name() {
        return locality_name;
    }

    public void setLocality_name(String locality_name) {
        this.locality_name = locality_name;
    }

    public String getLocality_status() {
        return locality_status;
    }

    public void setLocality_status(String locality_status) {
        this.locality_status = locality_status;
    }

    public String getIs_created_date() {
        return is_created_date;
    }

    public void setIs_created_date(String is_created_date) {
        this.is_created_date = is_created_date;
    }
}

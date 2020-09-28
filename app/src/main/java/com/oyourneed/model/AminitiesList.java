package com.oyourneed.model;

import java.io.Serializable;

public class AminitiesList implements Serializable {
    private String amenities_id;

    private String amenities_pic;

    private String is_created_date;

    private String amenities_status;

    private String amenities_name;

    public String getAmenities_id ()
    {
        return amenities_id;
    }

    public void setAmenities_id (String amenities_id)
    {
        this.amenities_id = amenities_id;
    }

    public String getAmenities_pic ()
    {
        return amenities_pic;
    }

    public void setAmenities_pic (String amenities_pic)
    {
        this.amenities_pic = amenities_pic;
    }

    public String getIs_created_date ()
    {
        return is_created_date;
    }

    public void setIs_created_date (String is_created_date)
    {
        this.is_created_date = is_created_date;
    }

    public String getAmenities_status ()
    {
        return amenities_status;
    }

    public void setAmenities_status (String amenities_status)
    {
        this.amenities_status = amenities_status;
    }

    public String getAmenities_name ()
    {
        return amenities_name;
    }

    public void setAmenities_name (String amenities_name)
    {
        this.amenities_name = amenities_name;
    }
}

package com.oyourneed.model;

import java.io.Serializable;

public class SliderList implements Serializable {
    private String slider_name;

    private String work_space_id;

    private String is_created_date;

    private String sliderimg_image;

    private String sliderimg_id;

    private String slider_status;

    public SliderList(String slider_name, String work_space_id, String is_created_date, String sliderimg_image, String sliderimg_id, String slider_status) {
        this.slider_name = slider_name;
        this.work_space_id = work_space_id;
        this.is_created_date = is_created_date;
        this.sliderimg_image = sliderimg_image;
        this.sliderimg_id = sliderimg_id;
        this.slider_status = slider_status;
    }

    public String getSlider_name ()
    {
        return slider_name;
    }

    public void setSlider_name (String slider_name)
    {
        this.slider_name = slider_name;
    }

    public String getWork_space_id ()
    {
        return work_space_id;
    }

    public void setWork_space_id (String work_space_id)
    {
        this.work_space_id = work_space_id;
    }

    public String getIs_created_date ()
    {
        return is_created_date;
    }

    public void setIs_created_date (String is_created_date)
    {
        this.is_created_date = is_created_date;
    }

    public String getsliderimg_image ()
    {
        return sliderimg_image;
    }

    public void setsliderimg_image (String sliderimg_image)
    {
        this.sliderimg_image = sliderimg_image;
    }

    public String getsliderimg_id ()
    {
        return sliderimg_id;
    }

    public void setsliderimg_id (String sliderimg_id)
    {
        this.sliderimg_id = sliderimg_id;
    }

    public String getSlider_status ()
    {
        return slider_status;
    }

    public void setSlider_status (String slider_status)
    {
        this.slider_status = slider_status;
    }

}

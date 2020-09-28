package com.oyourneed.model;

import java.io.Serializable;

public class DeskList implements Serializable {
    private String desk_status;

    private String work_space_id;

    private String intersted_id;

    private String is_created_date;

    private String desk_qty;

    private String desk_id;

    private String intersted_name;

    public String getDesk_status ()
    {
        return desk_status;
    }

    public void setDesk_status (String desk_status)
    {
        this.desk_status = desk_status;
    }

    public String getWork_space_id ()
    {
        return work_space_id;
    }

    public void setWork_space_id (String work_space_id)
    {
        this.work_space_id = work_space_id;
    }

    public String getIntersted_id ()
    {
        return intersted_id;
    }

    public void setIntersted_id (String intersted_id)
    {
        this.intersted_id = intersted_id;
    }

    public String getIs_created_date ()
    {
        return is_created_date;
    }

    public void setIs_created_date (String is_created_date)
    {
        this.is_created_date = is_created_date;
    }

    public String getDesk_qty ()
    {
        return desk_qty;
    }

    public void setDesk_qty (String desk_qty)
    {
        this.desk_qty = desk_qty;
    }

    public String getDesk_id ()
    {
        return desk_id;
    }

    public void setDesk_id (String desk_id)
    {
        this.desk_id = desk_id;
    }

    public String getIntersted_name ()
    {
        return intersted_name;
    }

    public void setIntersted_name (String intersted_name)
    {
        this.intersted_name = intersted_name;
    }
}

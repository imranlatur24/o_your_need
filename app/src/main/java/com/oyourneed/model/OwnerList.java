package com.oyourneed.model;

import java.io.Serializable;

public class OwnerList implements Serializable {
    private String email_id;

    private String wrok_space_owner_id;

    private String is_created_date;

    private String last_name;

    private String mobile_no;

    private String owner_status;

    private String first_name;

    public String getEmail_id ()
    {
        return email_id;
    }

    public void setEmail_id (String email_id)
    {
        this.email_id = email_id;
    }

    public String getWrok_space_owner_id ()
    {
        return wrok_space_owner_id;
    }

    public void setWrok_space_owner_id (String wrok_space_owner_id)
    {
        this.wrok_space_owner_id = wrok_space_owner_id;
    }

    public String getIs_created_date ()
    {
        return is_created_date;
    }

    public void setIs_created_date (String is_created_date)
    {
        this.is_created_date = is_created_date;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public String getOwner_status ()
    {
        return owner_status;
    }

    public void setOwner_status (String owner_status)
    {
        this.owner_status = owner_status;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}

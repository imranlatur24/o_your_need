package com.oyourneed.model;

import java.util.ArrayList;

public class UpdateProfile {

    private String response;

/*
    private ArrayList<Customer_details> customer_details;
*/

    private ArrayList<Customer_details> login;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

   /* public ArrayList<Customer_details> getCustomer_details() {
        return customer_details;
    }

    public void setCustomer_details(ArrayList<Customer_details> customer_details) {
        this.customer_details = customer_details;
    }*/

    public ArrayList<Customer_details> getResult() {
        return login;
    }

    public void setResult(ArrayList<Customer_details> login) {
        this.login = login;
    }

    public class Customer_details
    {
        private String reg_id;

        private String first_name;

        private String last_name;

        private String mobile_no;

        private String email_id;

        private String dob;

        private String country;

        private String state;

        private String city;

        private String address;

        private String pincode;

        private String user_status;

        private String is_created_date;


        public String getReg_id() {
            return reg_id;
        }

        public void setReg_id(String reg_id) {
            this.reg_id = reg_id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getIs_created_date() {
            return is_created_date;
        }

        public void setIs_created_date(String is_created_date) {
            this.is_created_date = is_created_date;
        }
    }
}

package com.oyourneed.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "product")
public class Cartdb {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pro_id")
    private String pro_id;

    @ColumnInfo(name = "pro_title")
    private String title;

    @ColumnInfo(name = "pro_desc")
    private String pro_desc;

    @ColumnInfo(name = "pro_qty")
    private String pro_qty;

    @ColumnInfo(name = "pro_total")
    private String pro_total;

    @ColumnInfo(name = "pro_price")
    private String pro_price;

    @ColumnInfo(name = "pro_image")
    private String pro_image;

    @ColumnInfo(name = "product_temp_price")
    private String product_temp_price;

    @ColumnInfo(name = "product_discount")
    private String product_discount;

    @ColumnInfo(name = "product_unit")
    private String product_unit;


    public String getProduct_unit() {
        return product_unit;
    }

    public void setProduct_unit(String product_unit) {
        this.product_unit = product_unit;
    }

    public String getProduct_temp_price() {
        return product_temp_price;
    }

    public void setProduct_temp_price(String product_temp_price) {
        this.product_temp_price = product_temp_price;
    }

    public String getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(String product_discount) {
        this.product_discount = product_discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPro_desc() {
        return pro_desc;
    }

    public void setPro_desc(String pro_desc) {
        this.pro_desc = pro_desc;
    }

    public String getPro_qty() {
        return pro_qty;
    }

    public void setPro_qty(String pro_qty) {
        this.pro_qty = pro_qty;
    }

    public String getPro_total() {
        return pro_total;
    }

    public void setPro_total(String pro_total) {
        this.pro_total = pro_total;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }

    public String getPro_image() {
        return pro_image;
    }

    public void setPro_image(String pro_image) {
        this.pro_image = pro_image;
    }
}

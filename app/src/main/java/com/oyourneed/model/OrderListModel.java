package com.oyourneed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderListModel {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_minimum_qty")
    @Expose
    private String productMinimumQty;
    @SerializedName("product_unit")
    @Expose
    private String productUnit;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_qty")
    @Expose
    private String productQty;
    @SerializedName("product_total_price")
    @Expose
    private String productTotalPrice;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("is_created_date")
    @Expose
    private String isCreatedDate;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductMinimumQty() {
        return productMinimumQty;
    }

    public void setProductMinimumQty(String productMinimumQty) {
        this.productMinimumQty = productMinimumQty;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getIsCreatedDate() {
        return isCreatedDate;
    }

    public void setIsCreatedDate(String isCreatedDate) {
        this.isCreatedDate = isCreatedDate;
    }

}
package com.oyourneed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderList {

    @SerializedName("order_payment_id")
    @Expose
    private String orderPaymentId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("total_qty")
    @Expose
    private String totalQty;
    @SerializedName("final_total_amount")
    @Expose
    private String finalTotalAmount;
    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;
    @SerializedName("payment_staus")
    @Expose
    private String paymentStaus;
    @SerializedName("is_created_date")
    @Expose
    private String isCreatedDate;

    public String getOrderPaymentId() {
        return orderPaymentId;
    }

    public void setOrderPaymentId(String orderPaymentId) {
        this.orderPaymentId = orderPaymentId;
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

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getFinalTotalAmount() {
        return finalTotalAmount;
    }

    public void setFinalTotalAmount(String finalTotalAmount) {
        this.finalTotalAmount = finalTotalAmount;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentStaus() {
        return paymentStaus;
    }

    public void setPaymentStaus(String paymentStaus) {
        this.paymentStaus = paymentStaus;
    }

    public String getIsCreatedDate() {
        return isCreatedDate;
    }

    public void setIsCreatedDate(String isCreatedDate) {
        this.isCreatedDate = isCreatedDate;
    }

}

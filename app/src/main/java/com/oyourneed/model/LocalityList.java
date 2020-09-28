package com.oyourneed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalityList {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("language_id")
    @Expose
    private String languageId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
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
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("is_created_date")
    @Expose
    private String isCreatedDate;
    @SerializedName("product_temp_price")
    @Expose
    private String product_temp_price;
    @SerializedName("product_discount")
    @Expose
    private String product_discount;

    public String getProductId() {
        return productId;
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

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getIsCreatedDate() {
        return isCreatedDate;
    }

    public void setIsCreatedDate(String isCreatedDate) {
        this.isCreatedDate = isCreatedDate;
    }

}

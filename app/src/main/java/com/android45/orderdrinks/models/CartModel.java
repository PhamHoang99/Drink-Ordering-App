package com.android45.orderdrinks.models;

import java.io.Serializable;

public class CartModel implements Serializable {
    String documentID;
    String productName;
    String currentDate;
    String currentTime;
    String productImg;
    String totalQuantity;
    int productPrice;
    int totalPrice;

    public CartModel() {
    }

    public CartModel(String documentID, String productName, String currentDate, String currentTime, String productImg, String totalQuantity, int productPrice, int totalPrice) {
        this.documentID = documentID;
        this.productName = productName;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.productImg = productImg;
        this.totalQuantity = totalQuantity;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String id) {
        this.documentID = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}

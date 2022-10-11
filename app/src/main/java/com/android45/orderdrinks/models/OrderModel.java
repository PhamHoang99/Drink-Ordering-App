package com.android45.orderdrinks.models;

public class OrderModel {
    private String productName, productImg, documentID;
    private int productPrice;
    int totalPrice;

    public OrderModel() {
    }

    public OrderModel(String productName, String productImg, String documentID, int productPrice, int totalPrice) {
        this.productName = productName;
        this.productImg = productImg;
        this.documentID = documentID;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
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

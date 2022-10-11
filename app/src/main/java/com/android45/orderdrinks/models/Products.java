package com.android45.orderdrinks.models;

import java.io.Serializable;

public class Products implements Serializable {
    String name;
    int price;
    String description;
    String img_url;
    String type;

    public Products() {
    }

    public Products(String name, int price, String description, String img_url, String type) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.img_url = img_url;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDetail(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

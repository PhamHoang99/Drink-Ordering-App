package com.android45.orderdrinks.models;

public class UserModel {
    String name;
    String sdt;
    String email;
    String username;
    String password;
    String address;

    public UserModel() {
    }

    public UserModel(String name, String sdt, String email,String username, String address) {
        this.name = name;
        this.sdt = sdt;
        this.email = email;
        this.username = username;
        this.address = address;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserModel( String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

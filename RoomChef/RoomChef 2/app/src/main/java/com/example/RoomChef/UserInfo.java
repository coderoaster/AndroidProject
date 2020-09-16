package com.example.RoomChef;

public class UserInfo {


    String phone;
    String password;
    String image;

    public UserInfo(String phone, String password, String image) {
        this.phone = phone;
        this.password = password;
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

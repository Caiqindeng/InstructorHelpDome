package com.example.instructorhelpdome.JavaBean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Release  extends BmobObject {
    private String username;//学号
    private String title;
    private String price;
    private String phone;
    private String describe;
    private BmobFile images;



    public BmobFile getImages() {
        return images;
    }

    public void setImages(BmobFile images) {
        this.images = images;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}

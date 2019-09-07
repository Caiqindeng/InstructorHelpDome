package com.example.instructorhelpdome.JavaBean;

import cn.bmob.v3.BmobObject;

public class Foodphone extends BmobObject {
    private String shopname;
    private String shopphone;

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopphone() {
        return shopphone;
    }

    public void setShopphone(String shopphone) {
        this.shopphone = shopphone;
    }
}

package com.example.instructorhelpdome.JavaBean;

import cn.bmob.v3.BmobObject;

public class Suggestion extends BmobObject {
    String username;
    String theme;
    String advise;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }
}

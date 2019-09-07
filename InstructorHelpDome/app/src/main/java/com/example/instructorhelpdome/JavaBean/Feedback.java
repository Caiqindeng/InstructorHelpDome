package com.example.instructorhelpdome.JavaBean;

import cn.bmob.v3.BmobObject;

public class Feedback extends BmobObject {
    private String problem;
    private String details;
    private String contact;

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

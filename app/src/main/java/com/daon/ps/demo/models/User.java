package com.daon.ps.demo.models;

public class User {

    private String userId;
    private String href;

    public User(String userId, String href) {
        this.userId = userId;
        this.href = href;
    }

    public User(String userId){
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

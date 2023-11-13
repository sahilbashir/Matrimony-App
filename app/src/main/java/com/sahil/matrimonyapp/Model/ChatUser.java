package com.sahil.matrimonyapp.Model;

public class ChatUser {
    private String ImageUrl;
    private String name;
    private String userId;
    private String mORr;

    public ChatUser(){

    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getmORr() {
        return mORr;
    }

    public void setmORr(String mORr) {
        this.mORr = mORr;
    }

}

package com.sahil.matrimonyapp.Model;

import com.google.type.DateTime;

import java.util.Date;

public class userMessage {

    private String message;
    private String contex;
    private String dateTime;
    private Date dateTimeFormat;

    public userMessage(){


    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContex() {
        return contex;
    }

    public void setContex(String contex) {
        this.contex = contex;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(Date dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }
}

package com.sahil.matrimonyapp.Model;

import com.google.type.DateTime;

class Message{

    private String message;
    private String contex;
    private String dateTime;
    private DateTime dateTimeFormat;

    public Message(){


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

    public DateTime getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(DateTime dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }
}


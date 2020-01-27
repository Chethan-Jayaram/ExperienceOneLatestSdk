package com.example.experienceone.pojo.ticketdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketDetailsSocketPojo {


    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("message")
    @Expose
    private Result message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Result getMessage() {
        return message;
    }

    public void setMessage(Result message) {
        this.message = message;
    }
}

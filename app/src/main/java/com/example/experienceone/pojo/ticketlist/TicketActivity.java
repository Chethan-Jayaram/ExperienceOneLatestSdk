
package com.example.experienceone.pojo.ticketlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketActivity {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("user")
    @Expose
    private Object user;
    @SerializedName("note")
    @Expose
    private Object note;
    @SerializedName("extra")
    @Expose
    private String extra;
    @SerializedName("activity_on")
    @Expose
    private String activityOn;
    @SerializedName("ticket")
    @Expose
    private Integer ticket;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public Object getNote() {
        return note;
    }

    public void setNote(Object note) {
        this.note = note;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getActivityOn() {
        return activityOn;
    }

    public void setActivityOn(String activityOn) {
        this.activityOn = activityOn;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

}

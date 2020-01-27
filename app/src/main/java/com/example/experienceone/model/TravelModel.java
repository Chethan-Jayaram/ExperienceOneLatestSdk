package com.example.experienceone.model;

import com.example.experienceone.pojo.travel.CategoryItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravelModel {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("booking")
    @Expose
    private Integer booking;
    @SerializedName("details")
    @Expose
    private List<CategoryItem> details = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBooking() {
        return booking;
    }

    public void setBooking(Integer booking) {
        this.booking = booking;
    }

    public List<CategoryItem> getDetails() {
        return details;
    }

    public void setDetails(List<CategoryItem> details) {
        this.details = details;
    }
}

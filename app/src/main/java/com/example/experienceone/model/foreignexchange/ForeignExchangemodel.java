
package com.example.experienceone.model.foreignexchange;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForeignExchangemodel {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("booking")
    @Expose
    private Integer booking;
    @SerializedName("layout")
    @Expose
    private String layout;
    @SerializedName("details")
    @Expose
    private List<Map> details = null;

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

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public List<Map> getDetails() {
        return details;
    }

    public void setDetails(List<Map> details) {
        this.details = details;
    }
}


package com.example.experienceone.model.foreignexchange;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForeignExchangemodel implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("booking")
    @Expose
    private Integer booking;

    @SerializedName("room_no")
    @Expose
    private String room_no;

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

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

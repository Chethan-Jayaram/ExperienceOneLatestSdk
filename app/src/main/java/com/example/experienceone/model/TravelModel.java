package com.example.experienceone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.experienceone.pojo.travel.CategoryItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TravelModel implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("booking")
    @Expose
    private Integer booking;

    @SerializedName("room_no")
    @Expose
    private String RoomNo;

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


    public String getRoomNo() {
        return RoomNo;
    }

    public void setRoomNo(String roomNo) {
        RoomNo = roomNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

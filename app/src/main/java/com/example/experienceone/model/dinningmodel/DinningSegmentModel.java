
package com.example.experienceone.model.dinningmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


import com.example.experienceone.pojo.dinning.CategoryItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DinningSegmentModel implements Parcelable{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("booking")
    @Expose
    private Integer booking;


    @SerializedName("room_no")
    @Expose
    private String room_no;


    @SerializedName("details")
    @Expose
    private List<CategoryItem> details = null;

    @SerializedName("special_instructions")
    @Expose
    private String specialinstructions;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public String getSpecialinstructions() {
        return specialinstructions;
    }

    public void setSpecialinstructions(String specialinstructions) {
        this.specialinstructions = specialinstructions;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }
}

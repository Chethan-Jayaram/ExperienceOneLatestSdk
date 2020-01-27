
package com.example.experienceone.model.raisingticketmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.HouseKeeping.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModuleSegmentModel implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("booking")
    @Expose
    private Integer booking;




    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @SerializedName("result")
    @Expose
    private List<Result> results = null;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


}


package com.example.experienceone.pojo.tourguide;

import java.util.List;

import com.example.experienceone.pojo.GeneralPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TourGuidePojo extends GeneralPojo {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

}

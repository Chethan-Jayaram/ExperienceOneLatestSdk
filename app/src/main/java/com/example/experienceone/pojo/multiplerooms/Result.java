
package com.example.experienceone.pojo.multiplerooms;

import java.util.List;

import com.example.experienceone.pojo.loginmpin.ActiveBooking;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("activeBooking")
    @Expose
    private List<ActiveBooking> activeBooking = null;

    public List<ActiveBooking> getActiveBooking() {
        return activeBooking;
    }

    public void setActiveBooking(List<ActiveBooking> activeBooking) {
        this.activeBooking = activeBooking;
    }
}

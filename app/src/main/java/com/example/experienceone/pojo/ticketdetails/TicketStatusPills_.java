
package com.example.experienceone.pojo.ticketdetails;

import android.content.res.ColorStateList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketStatusPills_ {

    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("color")
    @Expose
    private String color;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}

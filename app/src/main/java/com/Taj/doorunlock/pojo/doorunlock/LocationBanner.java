
package com.taj.doorunlock.pojo.doorunlock;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;

import com.google.gson.annotations.SerializedName;


@Keep
public class LocationBanner {

    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}

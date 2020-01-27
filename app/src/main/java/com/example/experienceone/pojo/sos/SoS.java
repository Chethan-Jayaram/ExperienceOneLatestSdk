package com.example.experienceone.pojo.sos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SoS {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sos_status")
    @Expose
    private Boolean sosStatus;
    @SerializedName("location_id")
    @Expose
    private Object locationId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;
    @SerializedName("guest")
    @Expose
    private Integer guest;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getSosStatus() {
        return sosStatus;
    }

    public void setSosStatus(Boolean sosStatus) {
        this.sosStatus = sosStatus;
    }

    public Object getLocationId() {
        return locationId;
    }

    public void setLocationId(Object locationId) {
        this.locationId = locationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(String lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Integer getGuest() {
        return guest;
    }

    public void setGuest(Integer guest) {
        this.guest = guest;
    }

}

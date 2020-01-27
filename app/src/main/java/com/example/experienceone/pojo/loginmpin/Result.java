
package com.example.experienceone.pojo.loginmpin;

import java.util.List;

import com.example.experienceone.pojo.GeneralPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result  {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("guest")
    @Expose
    private Guest guest;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isGuest")
    @Expose
    private Boolean isGuest;
    @SerializedName("activeBooking")
    @Expose
    private List<ActiveBooking> activeBooking = null;
    @SerializedName("organizationDetails")
    @Expose
    private OrganizationDetails organizationDetails;
    @SerializedName("location")
    @Expose
    private Location location;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsGuest() {
        return isGuest;
    }

    public void setIsGuest(Boolean isGuest) {
        this.isGuest = isGuest;
    }

    public List<ActiveBooking> getActiveBooking() {
        return activeBooking;
    }

    public void setActiveBooking(List<ActiveBooking> activeBooking) {
        this.activeBooking = activeBooking;
    }

    public OrganizationDetails getOrganizationDetails() {
        return organizationDetails;
    }

    public void setOrganizationDetails(OrganizationDetails organizationDetails) {
        this.organizationDetails = organizationDetails;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}

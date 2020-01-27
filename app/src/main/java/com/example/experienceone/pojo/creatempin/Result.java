
package com.example.experienceone.pojo.creatempin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("guest")
    @Expose
    private Guest guest;
    @SerializedName("isGuest")
    @Expose
    private Boolean isGuest;
    @SerializedName("isOtp")
    @Expose
    private Boolean isOtp;
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("status")
    @Expose
    private String status;

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Boolean guest) {
        isGuest = guest;
    }

    public Boolean getOtp() {
        return isOtp;
    }

    public void setOtp(Boolean otp) {
        isOtp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
}

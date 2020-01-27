
package com.example.experienceone.pojo.doorunlock;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileKeysResult {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isGuest")
    @Expose
    private Boolean isGuest;
    @SerializedName("AutoKey")
    @Expose
    private AutoKey autoKey;

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

    public Boolean getIsGuest() {
        return isGuest;
    }

    public void setIsGuest(Boolean isGuest) {
        this.isGuest = isGuest;
    }

    public Boolean getGuest() {
        return isGuest;
    }

    public void setGuest(Boolean guest) {
        isGuest = guest;
    }

    public AutoKey getAutoKey() {
        return autoKey;
    }

    public void setAutoKey(AutoKey autoKey) {
        this.autoKey = autoKey;
    }

}

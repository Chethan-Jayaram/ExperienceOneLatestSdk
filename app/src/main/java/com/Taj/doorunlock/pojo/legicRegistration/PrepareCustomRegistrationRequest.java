
package com.taj.doorunlock.pojo.legicRegistration;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class PrepareCustomRegistrationRequest {

    @SerializedName("publicRegistrationId")
    @Expose
    private String publicRegistrationId;

    public String getPublicRegistrationId() {
        return publicRegistrationId;
    }

    public void setPublicRegistrationId(String publicRegistrationId) {
        this.publicRegistrationId = publicRegistrationId;
    }

}

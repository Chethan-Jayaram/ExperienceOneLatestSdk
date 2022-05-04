
package com.taj.doorunlock.pojo.legicRegistration;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class LegicRegistration {

    @SerializedName("prepareCustomRegistrationRequest")
    @Expose
    private PrepareCustomRegistrationRequest prepareCustomRegistrationRequest;

    public PrepareCustomRegistrationRequest getPrepareCustomRegistrationRequest() {
        return prepareCustomRegistrationRequest;
    }

    public void setPrepareCustomRegistrationRequest(PrepareCustomRegistrationRequest prepareCustomRegistrationRequest) {
        this.prepareCustomRegistrationRequest = prepareCustomRegistrationRequest;
    }

}

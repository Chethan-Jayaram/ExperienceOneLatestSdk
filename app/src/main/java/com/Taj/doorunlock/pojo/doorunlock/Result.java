
package com.taj.doorunlock.pojo.doorunlock;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class Result {

    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("prepareCustomRegistrationResponse")
    @Expose
    private PrepareCustomRegistrationResponse prepareCustomRegistrationResponse;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public PrepareCustomRegistrationResponse getPrepareCustomRegistrationResponse() {
        return prepareCustomRegistrationResponse;
    }

    public void setPrepareCustomRegistrationResponse(PrepareCustomRegistrationResponse prepareCustomRegistrationResponse) {
        this.prepareCustomRegistrationResponse = prepareCustomRegistrationResponse;
    }

}

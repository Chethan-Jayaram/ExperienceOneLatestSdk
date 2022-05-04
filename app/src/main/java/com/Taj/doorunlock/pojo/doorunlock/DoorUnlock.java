
package com.taj.doorunlock.pojo.doorunlock;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class DoorUnlock {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}

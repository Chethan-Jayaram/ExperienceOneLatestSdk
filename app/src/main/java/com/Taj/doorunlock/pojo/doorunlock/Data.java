
package com.taj.doorunlock.pojo.doorunlock;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class Data {

    @SerializedName("invitation_code")
    @Expose
    private String invitationCode;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("developerMessage")
    @Expose
    private String developerMessage;

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
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

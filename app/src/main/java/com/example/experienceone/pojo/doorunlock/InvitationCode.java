
package com.example.experienceone.pojo.doorunlock;

import com.example.experienceone.pojo.GeneralPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvitationCode extends GeneralPojo {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}

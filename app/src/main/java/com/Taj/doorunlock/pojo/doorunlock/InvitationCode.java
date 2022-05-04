
package com.taj.doorunlock.pojo.doorunlock;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taj.doorunlock.pojo.GeneralPojo;


@Keep
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

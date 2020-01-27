
package com.example.experienceone.pojo.posttickets;

import com.example.experienceone.pojo.GeneralPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketID extends GeneralPojo {

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

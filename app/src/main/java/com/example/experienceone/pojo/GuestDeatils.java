
package com.example.experienceone.pojo;

import com.example.experienceone.pojo.GeneralPojo;
import com.example.experienceone.pojo.loginmpin.Guest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestDeatils extends GeneralPojo {

    @SerializedName("result")
    @Expose
    private Guest result;

    public Guest getResult() {
        return result;
    }

    public void setResult(Guest result) {
        this.result = result;
    }
}


package com.example.experienceone.pojo.doorunlock;

import com.example.experienceone.pojo.GeneralPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mobilekeys extends GeneralPojo {

    @SerializedName("result")
    @Expose
    private MobileKeysResult result;

    public MobileKeysResult getResult() {
        return result;
    }

    public void setResult(MobileKeysResult result) {
        this.result = result;
    }
}

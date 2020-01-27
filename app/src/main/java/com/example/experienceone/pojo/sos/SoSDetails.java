package com.example.experienceone.pojo.sos;

import com.example.experienceone.pojo.GeneralPojo;
import com.example.experienceone.pojo.sos.SoS;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SoSDetails extends GeneralPojo {

    @SerializedName("result")
    @Expose
    private List<SoS> result;

    public List<SoS> getResult() {
        return result;
    }

    public void setResult(List<SoS> result) {
        this.result = result;
    }
}

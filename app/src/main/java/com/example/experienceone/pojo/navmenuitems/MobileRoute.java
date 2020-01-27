
package com.example.experienceone.pojo.navmenuitems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileRoute {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("route_name")
    @Expose
    private String routeName;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

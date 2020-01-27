
package com.example.experienceone.pojo.dashboardelements;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("dashboardItems")
    @Expose
    private List<DashboardItem> dashboardItems = null;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<DashboardItem> getDashboardItems() {
        return dashboardItems;
    }

    public void setDashboardItems(List<DashboardItem> dashboardItems) {
        this.dashboardItems = dashboardItems;
    }

}


package com.example.experienceone.pojo.travel;

import java.util.List;

import com.example.experienceone.services.ObjectListner;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements ObjectListner {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;
    @SerializedName("last_activity_by")
    @Expose
    private Integer lastActivityBy;
    @SerializedName("category_item")
    @Expose
    private List<CategoryItem> categoryItem = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(String lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Integer getLastActivityBy() {
        return lastActivityBy;
    }

    public void setLastActivityBy(Integer lastActivityBy) {
        this.lastActivityBy = lastActivityBy;
    }

    public List<CategoryItem> getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(List<CategoryItem> categoryItem) {
        this.categoryItem = categoryItem;
    }

    @Override
    public int getType() {
        return ObjectListner.TYPE_CATEGORY_CARD;
    }
}

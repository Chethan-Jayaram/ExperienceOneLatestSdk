
package com.example.experienceone.pojo.HouseKeeping;

import com.example.experienceone.services.ObjectListner;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItem  implements ObjectListner {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("sla_time_in_minutes")
    @Expose
    private Integer slaTimeInMinutes;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("last_activity_by")
    @Expose
    private Integer lastActivityBy;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSlaTimeInMinutes() {
        return slaTimeInMinutes;
    }

    public void setSlaTimeInMinutes(Integer slaTimeInMinutes) {
        this.slaTimeInMinutes = slaTimeInMinutes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(String lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getLastActivityBy() {
        return lastActivityBy;
    }

    public void setLastActivityBy(Integer lastActivityBy) {
        this.lastActivityBy = lastActivityBy;
    }

    @Override
    public int getType() {
        return ObjectListner.TYPE_SUBCATEGORY_CARD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

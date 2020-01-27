
package com.example.experienceone.pojo.travel;

import java.util.List;

import com.example.experienceone.services.ObjectListner;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItem implements ObjectListner {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("car_model")
    @Expose
    private String carModel;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("last_activity_by")
    @Expose
    private Integer lastActivityBy;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("carTariff")
    @Expose
    private List<CarTariff> carTariff = null;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    public String getTitle() {
        return title;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLastActivityBy() {
        return lastActivityBy;
    }

    public void setLastActivityBy(Integer lastActivityBy) {
        this.lastActivityBy = lastActivityBy;
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

    public List<CarTariff> getCarTariff() {
        return carTariff;
    }

    public void setCarTariff(List<CarTariff> carTariff) {
        this.carTariff = carTariff;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Override
    public int getType() {
        return ObjectListner.TYPE_SUBCATEGORY_CARD;
    }
}

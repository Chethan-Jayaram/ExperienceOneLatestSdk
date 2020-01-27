
package com.example.experienceone.pojo.posttickets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("record_id")
    @Expose
    private String recordId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("curency_symbol")
    @Expose
    private String curencySymbol;
    @SerializedName("append_currency")
    @Expose
    private String appendCurrency;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCurencySymbol() {
        return curencySymbol;
    }

    public void setCurencySymbol(String curencySymbol) {
        this.curencySymbol = curencySymbol;
    }

    public String getAppendCurrency() {
        return appendCurrency;
    }

    public void setAppendCurrency(String appendCurrency) {
        this.appendCurrency = appendCurrency;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

}

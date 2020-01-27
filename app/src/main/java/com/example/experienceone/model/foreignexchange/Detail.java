
package com.example.experienceone.model.foreignexchange;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("curency_symbol")
    @Expose
    private String curencySymbol;
    @SerializedName("append_currency")
    @Expose
    private String appendCurrency;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

}

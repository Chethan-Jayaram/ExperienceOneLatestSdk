
package com.example.experienceone.pojo.currencyexchnage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("conversion_amount")
    @Expose
    private Double conversionAmount;
    @SerializedName("commission_rate")
    @Expose
    private Double commissionRate;
    @SerializedName("exchange_rate")
    @Expose
    private Double exchangeRate;
    @SerializedName("commission_in_percent")
    @Expose
    private Double commissionInPercent;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;

    public Double getConversionAmount() {
        return conversionAmount;
    }

    public void setConversionAmount(Double conversionAmount) {
        this.conversionAmount = conversionAmount;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getCommissionInPercent() {
        return commissionInPercent;
    }

    public void setCommissionInPercent(Double commissionInPercent) {
        this.commissionInPercent = commissionInPercent;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(String lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

}

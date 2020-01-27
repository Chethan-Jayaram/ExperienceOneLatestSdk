
package com.example.experienceone.pojo.loginmpin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("location_dataname")
    @Expose
    private String locationDataname;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("country")
    @Expose
    private Integer country;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("city")
    @Expose
    private Integer city;
    @SerializedName("zip_code")
    @Expose
    private Integer zipCode;
    @SerializedName("address_1")
    @Expose
    private String address1;
    @SerializedName("address_2")
    @Expose
    private String address2;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("use_default_logo")
    @Expose
    private Integer useDefaultLogo;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("last_activity")
    @Expose
    private String lastActivity;
    @SerializedName("currency_conversion_commission")
    @Expose
    private Double currencyConversionCommission;
    @SerializedName("org_id")
    @Expose
    private Integer orgId;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("last_activity_by")
    @Expose
    private Integer lastActivityBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationDataname() {
        return locationDataname;
    }

    public void setLocationDataname(String locationDataname) {
        this.locationDataname = locationDataname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getUseDefaultLogo() {
        return useDefaultLogo;
    }

    public void setUseDefaultLogo(Integer useDefaultLogo) {
        this.useDefaultLogo = useDefaultLogo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public Double getCurrencyConversionCommission() {
        return currencyConversionCommission;
    }

    public void setCurrencyConversionCommission(Double currencyConversionCommission) {
        this.currencyConversionCommission = currencyConversionCommission;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getLastActivityBy() {
        return lastActivityBy;
    }

    public void setLastActivityBy(Integer lastActivityBy) {
        this.lastActivityBy = lastActivityBy;
    }

}

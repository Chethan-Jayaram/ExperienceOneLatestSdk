
package com.example.experienceone.pojo.loginmpin;

import com.example.experienceone.pojo.GeneralPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class Guest  implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("country_code")
    @Expose
    private Object countryCode;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;
    @SerializedName("pms_id")
    @Expose
    private Object pmsId;
    @SerializedName("mpin")
    @Expose
    private Integer mpin;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("room_no")
    @Expose
    private Object roomNo;
    @SerializedName("checkin_date_time")
    @Expose
    private Object checkinDateTime;
    @SerializedName("checkout_date_time")
    @Expose
    private Object checkoutDateTime;
    @SerializedName("booking_ref")
    @Expose
    private Object bookingRef;
    @SerializedName("salutation")
    @Expose
    private String salutation;
    @SerializedName("no_of_invite_code")
    @Expose
    private Integer noOfInviteCode;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("image")
    @Expose
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("gender")
    @Expose
    private String gender;


    @SerializedName("marital_status")
    @Expose
    private String marital_status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Object countryCode) {
        this.countryCode = countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(String lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Object getPmsId() {
        return pmsId;
    }

    public void setPmsId(Object pmsId) {
        this.pmsId = pmsId;
    }

    public Integer getMpin() {
        return mpin;
    }

    public void setMpin(Integer mpin) {
        this.mpin = mpin;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Object getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(Object roomNo) {
        this.roomNo = roomNo;
    }

    public Object getCheckinDateTime() {
        return checkinDateTime;
    }

    public void setCheckinDateTime(Object checkinDateTime) {
        this.checkinDateTime = checkinDateTime;
    }

    public Object getCheckoutDateTime() {
        return checkoutDateTime;
    }

    public void setCheckoutDateTime(Object checkoutDateTime) {
        this.checkoutDateTime = checkoutDateTime;
    }

    public Object getBookingRef() {
        return bookingRef;
    }

    public void setBookingRef(Object bookingRef) {
        this.bookingRef = bookingRef;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Integer getNoOfInviteCode() {
        return noOfInviteCode;
    }

    public void setNoOfInviteCode(Integer noOfInviteCode) {
        this.noOfInviteCode = noOfInviteCode;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }
}

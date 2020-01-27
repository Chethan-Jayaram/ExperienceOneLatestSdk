
package com.example.experienceone.pojo.ticketdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Guest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
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
    private Object salutation;
    @SerializedName("no_of_invite_code")
    @Expose
    private Object noOfInviteCode;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("organization_id")
    @Expose
    private Integer organizationId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
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

    public Object getSalutation() {
        return salutation;
    }

    public void setSalutation(Object salutation) {
        this.salutation = salutation;
    }

    public Object getNoOfInviteCode() {
        return noOfInviteCode;
    }

    public void setNoOfInviteCode(Object noOfInviteCode) {
        this.noOfInviteCode = noOfInviteCode;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

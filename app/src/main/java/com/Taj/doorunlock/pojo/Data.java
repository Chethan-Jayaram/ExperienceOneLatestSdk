package com.taj.doorunlock.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taj.doorunlock.pojo.doorunlock.LocationBanner;

import java.io.Serializable;


@Keep
public class Data  {

    @SerializedName("guestUUID")
    @Expose
    private String guestUUID;

    public String getGuestUUID() {
        return guestUUID;
    }

    public void setGuestUUID(String guestUUID) {
        this.guestUUID = guestUUID;
    }

    @SerializedName("request_id")
    @Expose
    private String requestId;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("profile")
    @Expose
    private Profile profile;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("lock_type")
    @Expose
    private String lockType;

    @SerializedName("checkin_date")
    @Expose
    private String checkinDate;

    @SerializedName("checkout_date")
    @Expose
    private String checkoutDate;

    @SerializedName("rooms")
    @Expose
    private String rooms;

    @SerializedName("location_key")
    @Expose
    private String location_key;

    @SerializedName("reservation_key")
    @Expose
    private String reservation_key;


    @SerializedName("mpin")
    @Expose
    private String mpin;

    @SerializedName("location_banner")
    @Expose
    private LocationBanner locationBanner;


    public LocationBanner getLocationBanner() {
        return locationBanner;
    }

    public void setLocationBanner(LocationBanner locationBanner) {
        this.locationBanner = locationBanner;
    }

    public String getMpin() {
        return mpin;
    }

    public void setMpin(String mpin) {
        this.mpin = mpin;
    }

    public String getReservation_key() {
        return reservation_key;
    }

    public void setReservation_key(String reservation_key) {
        this.reservation_key = reservation_key;
    }

    public String getLocation_key() {
        return location_key;
    }

    public void setLocation_key(String location_key) {
        this.location_key = location_key;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

}

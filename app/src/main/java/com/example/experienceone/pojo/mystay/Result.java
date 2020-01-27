
package com.example.experienceone.pojo.mystay;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("activeBooking")
    @Expose
    private List<ActiveBookingPojo> activeBooking = null;
    @SerializedName("upcomingBookingSerializer")
    @Expose
    private List<Object> upcomingBookingSerializer = null;
    @SerializedName("BookingHistory")
    @Expose
    private List<BookingHistoryPojo> bookingHistory = null;

    public List<ActiveBookingPojo> getActiveBooking() {
        return activeBooking;
    }

    public void setActiveBooking(List<ActiveBookingPojo> activeBooking) {
        this.activeBooking = activeBooking;
    }

    public List<Object> getUpcomingBookingSerializer() {
        return upcomingBookingSerializer;
    }

    public void setUpcomingBookingSerializer(List<Object> upcomingBookingSerializer) {
        this.upcomingBookingSerializer = upcomingBookingSerializer;
    }

    public List<BookingHistoryPojo> getBookingHistory() {
        return bookingHistory;
    }

    public void setBookingHistory(List<BookingHistoryPojo> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }
}

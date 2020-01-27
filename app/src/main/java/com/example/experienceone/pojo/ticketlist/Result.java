
package com.example.experienceone.pojo.ticketlist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ticket_number")
    @Expose
    private String ticketNumber;
    @SerializedName("escalated_level")
    @Expose
    private String escalatedLevel;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("start_date_time")
    @Expose
    private String startDateTime;
    @SerializedName("end_date_time")
    @Expose
    private Object endDateTime;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("last_escalated_date_time")
    @Expose
    private String lastEscalatedDateTime;
    @SerializedName("booking")
    @Expose
    private Integer booking;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("current_status")
    @Expose
    private CurrentStatus currentStatus;
    @SerializedName("ticketActivity")
    @Expose
    private List<TicketActivity> ticketActivity = null;
    @SerializedName("assignee")
    @Expose
    private Integer assignee;
    @SerializedName("current_escalated_level")
    @Expose
    private Integer currentEscalatedLevel;
    @SerializedName("layout")
    @Expose
    private String layout;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getEscalatedLevel() {
        return escalatedLevel;
    }

    public void setEscalatedLevel(String escalatedLevel) {
        this.escalatedLevel = escalatedLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Object getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Object endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(String lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLastEscalatedDateTime() {
        return lastEscalatedDateTime;
    }

    public void setLastEscalatedDateTime(String lastEscalatedDateTime) {
        this.lastEscalatedDateTime = lastEscalatedDateTime;
    }

    public Integer getBooking() {
        return booking;
    }

    public void setBooking(Integer booking) {
        this.booking = booking;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<TicketActivity> getTicketActivity() {
        return ticketActivity;
    }

    public void setTicketActivity(List<TicketActivity> ticketActivity) {
        this.ticketActivity = ticketActivity;
    }

    public Integer getAssignee() {
        return assignee;
    }

    public void setAssignee(Integer assignee) {
        this.assignee = assignee;
    }

    public Integer getCurrentEscalatedLevel() {
        return currentEscalatedLevel;
    }

    public void setCurrentEscalatedLevel(Integer currentEscalatedLevel) {
        this.currentEscalatedLevel = currentEscalatedLevel;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

}

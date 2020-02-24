
package com.example.experienceone.pojo.ticketdetails;

import java.util.List;

import com.example.experienceone.pojo.mystay.Room;
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
    private List<Detail> details = null;
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
    private Booking booking;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("current_status")
    @Expose
    private CurrentStatus currentStatus;
    @SerializedName("assignee")
    @Expose
    private Assignee assignee;
    @SerializedName("current_escalated_level")
    @Expose
    private CurrentEscalatedLevel currentEscalatedLevel;
    @SerializedName("ticketActivity")
    @Expose
    private List<TicketActivity> ticketActivity = null;
    @SerializedName("current_level")
    @Expose
    private CurrentLevel currentLevel;
    @SerializedName("layout")
    @Expose
    private String layout;

    @SerializedName("room_no")
    @Expose
    private String room_no;

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    @SerializedName("special_instructions")
    @Expose
    private String special_instructions;

    public String getSpecial_instructions() {
        return special_instructions;
    }

    public void setSpecial_instructions(String special_instructions) {
        this.special_instructions = special_instructions;
    }

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

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
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

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public CurrentEscalatedLevel getCurrentEscalatedLevel() {
        return currentEscalatedLevel;
    }

    public void setCurrentEscalatedLevel(CurrentEscalatedLevel currentEscalatedLevel) {
        this.currentEscalatedLevel = currentEscalatedLevel;
    }

    public List<TicketActivity> getTicketActivity() {
        return ticketActivity;
    }

    public void setTicketActivity(List<TicketActivity> ticketActivity) {
        this.ticketActivity = ticketActivity;
    }

    public CurrentLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(CurrentLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }


}

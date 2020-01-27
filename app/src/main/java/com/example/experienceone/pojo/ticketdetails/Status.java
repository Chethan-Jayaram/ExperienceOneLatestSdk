
package com.example.experienceone.pojo.ticketdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("is_dashboard_visible")
    @Expose
    private Boolean isDashboardVisible;
    @SerializedName("event_style")
    @Expose
    private EventStyle_ eventStyle;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;
    @SerializedName("last_activity_by")
    @Expose
    private Integer lastActivityBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Boolean getIsDashboardVisible() {
        return isDashboardVisible;
    }

    public void setIsDashboardVisible(Boolean isDashboardVisible) {
        this.isDashboardVisible = isDashboardVisible;
    }

    public EventStyle_ getEventStyle() {
        return eventStyle;
    }

    public void setEventStyle(EventStyle_ eventStyle) {
        this.eventStyle = eventStyle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(String lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Integer getLastActivityBy() {
        return lastActivityBy;
    }

    public void setLastActivityBy(Integer lastActivityBy) {
        this.lastActivityBy = lastActivityBy;
    }

}

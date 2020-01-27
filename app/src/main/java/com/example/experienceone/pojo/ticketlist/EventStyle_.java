
package com.example.experienceone.pojo.ticketlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventStyle_ {

    @SerializedName("ticketStatusPills")
    @Expose
    private TicketStatusPills_ ticketStatusPills;
    @SerializedName("actionStyles")
    @Expose
    private ActionStyles_ actionStyles;

    public TicketStatusPills_ getTicketStatusPills() {
        return ticketStatusPills;
    }

    public void setTicketStatusPills(TicketStatusPills_ ticketStatusPills) {
        this.ticketStatusPills = ticketStatusPills;
    }

    public ActionStyles_ getActionStyles() {
        return actionStyles;
    }

    public void setActionStyles(ActionStyles_ actionStyles) {
        this.actionStyles = actionStyles;
    }

}

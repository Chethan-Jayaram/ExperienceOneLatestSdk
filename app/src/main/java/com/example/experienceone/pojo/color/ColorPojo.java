
package com.example.experienceone.pojo.color;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ColorPojo {

    @SerializedName("ticketStatusPills")
    @Expose
    private TicketStatusPills ticketStatusPills;
    @SerializedName("actionStyles")
    @Expose
    private ActionStyles actionStyles;

    public TicketStatusPills getTicketStatusPills() {
        return ticketStatusPills;
    }

    public void setTicketStatusPills(TicketStatusPills ticketStatusPills) {
        this.ticketStatusPills = ticketStatusPills;
    }

    public ActionStyles getActionStyles() {
        return actionStyles;
    }

    public void setActionStyles(ActionStyles actionStyles) {
        this.actionStyles = actionStyles;
    }

}

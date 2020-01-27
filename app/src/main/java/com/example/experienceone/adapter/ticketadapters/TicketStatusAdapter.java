package com.example.experienceone.adapter.ticketadapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.ticketdetails.TicketActivity;

import java.util.List;

public class TicketStatusAdapter extends RecyclerView.Adapter<TicketStatusAdapter.MyViewHolder> {

    private List<TicketActivity> ticketActivity;

    public TicketStatusAdapter(List<TicketActivity> ticketActivity) {
           this.ticketActivity=ticketActivity;
    }

    @NonNull
    @Override
    public TicketStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_status_recycler_content, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketStatusAdapter.MyViewHolder holder, int position) {
        try {
            holder.tv_date_time.setText(GlobalClass.outputdateformat.format(GlobalClass.inputdateformat.parse(ticketActivity.get(position).getActivityOn())));
            holder.tv_status.setText(ticketActivity.get(position).getStatus().getName());



            GradientDrawable drawable = (GradientDrawable) holder.tv_status.getBackground();
            drawable.setColor(Color.parseColor(ticketActivity.get(position).getStatus().getEventStyle().getTicketStatusPills().getBackground()));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return ticketActivity.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date_time,tv_status;

         MyViewHolder(@NonNull View itemView) {
            super(itemView);
             tv_date_time=itemView.findViewById(R.id.tv_date_time);
             tv_status=itemView.findViewById(R.id.tv_status);






        }
    }
}

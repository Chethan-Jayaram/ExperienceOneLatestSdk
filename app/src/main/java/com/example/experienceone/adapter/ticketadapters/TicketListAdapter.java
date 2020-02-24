package com.example.experienceone.adapter.ticketadapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;

import com.example.experienceone.helper.GlobalClass;


import com.example.experienceone.pojo.ticketlist.Result;

import java.util.ArrayList;
import java.util.List;


public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.MyViewHolder> {

    private List<Result> result;
    private GlobalClass.AdapterClickListner adapterClickListner;
    private boolean isLoadingAdded = false;


    public TicketListAdapter(GlobalClass.AdapterClickListner adapterClickListner) {
        this.adapterClickListner = adapterClickListner;
        result = new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_list_recycler_content, parent, false);

        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.tv_ticket_list_number.setText("#" + result.get(position).getTicketNumber());
            holder.tv_ticket_list_status.setText(result.get(position).getCurrentStatus().getName());
            holder.tv_Ticket_list_module_category.setText(result.get(position).getTitle());
            holder.content_lyt.setOnClickListener(v -> adapterClickListner.onItemClickListener(position));
            GradientDrawable drawable = (GradientDrawable) holder.tv_ticket_list_status.getBackground();
            drawable.setColor(Color.parseColor(result.get(position).getCurrentStatus().getEventStyle().getTicketStatusPills().getBackground()));
            holder.tv_started_date_time.setText( GlobalClass.dateTimeConverter(result.get(position).getStartDateTime()));
            holder.tv_activated_date_time.setText(GlobalClass.dateTimeConverter(result.get(position).getLastActivityOn()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
         return result == null ? 0 : result.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_ticket_list_number, tv_ticket_list_status,
                tv_Ticket_list_module_category, tv_activated_date_time, tv_started_date_time;

        LinearLayout content_lyt;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            content_lyt = itemView.findViewById(R.id.content_lyt);
            tv_ticket_list_number = itemView.findViewById(R.id.tv_ticket_list_number);
            tv_ticket_list_status = itemView.findViewById(R.id.tv_ticket_list_status);
            tv_Ticket_list_module_category = itemView.findViewById(R.id.tv_Ticket_list_module_category);
            tv_activated_date_time = itemView.findViewById(R.id.tv_activated_date_time);
            tv_started_date_time = itemView.findViewById(R.id.tv_started_date_time);
        }
    }

    public void addAll(List<Result> results) {
        for (Result result : results) {
            add(result);
        }
    }


    public void add(Result r) {
        result.add(r);
        notifyItemInserted(result.size());
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

}

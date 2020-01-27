package com.example.experienceone.adapter.moduleadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.pojo.wifi.Result;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WifiAdapter  extends RecyclerView.Adapter<WifiAdapter.MyViewHolder> {

    private List<Result> item_;

    public WifiAdapter(List<Result> item_) {
        this.item_ = item_;
    }



    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_wifi_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        try{
            holder.tv_user_name.setText(item_.get(position).getUsername());
            holder.tv_password.setText(item_.get(position).getPassword());
            holder.tv_floor_no.setText("Use the Password to connect for floor "+item_.get(position).getFloorNumber());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return item_.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_user_name, tv_password, tv_floor_no;
        MyViewHolder(View view) {
            super(view);
            tv_floor_no = view.findViewById(R.id.tv_floor_no);
            tv_user_name = view.findViewById(R.id.tv_user_name);
            tv_password = view.findViewById(R.id.tv_password);
        }
    }
}
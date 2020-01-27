package com.example.experienceone.adapter.moduleadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.pojo.hoteldirectory.Result;

import java.util.List;


public class HotelDirectoryAdapter extends RecyclerView.Adapter<HotelDirectoryAdapter.MyViewHolder> {

    private List<Result> item_;

    public HotelDirectoryAdapter(List<Result> item_) {
        this.item_ = item_;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_hotel_directory_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.tv_title.setText(item_.get(position).getTitle());
            holder.tv_desc.setText(item_.get(position).getDescription());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return item_.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title, tv_desc;

         MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_desc = itemView.findViewById(R.id.tv_desc);
        }
    }
}

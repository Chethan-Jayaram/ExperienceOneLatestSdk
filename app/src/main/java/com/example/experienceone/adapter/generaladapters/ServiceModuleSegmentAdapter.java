package com.example.experienceone.adapter.generaladapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.HouseKeeping.Result;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServiceModuleSegmentAdapter extends RecyclerView.Adapter<ServiceModuleSegmentAdapter.MyViewHolder> {
    private List<CategoryItem> details;
    private ArrayList<Result> results;



    public ServiceModuleSegmentAdapter(List<CategoryItem> details, ArrayList<com.example.experienceone.pojo.HouseKeeping.Result> results) {
        this.details=details;
        this.results=results;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.housekeeping_segment_content, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try{
            ArrayList<Result> res=results;
            for(int i=0;i<res.size();i++){
                if(res.get(i).getId().equals(details.get(position).getCategoryId())){
                    holder.tv_category_title.setVisibility(View.VISIBLE);
                    holder.tv_category_title.setText(res.get(i).getName());
                    res.remove(i);
                }
            }
            holder.tv_category_subtitle.setText(details.get(position).getName());
            holder.tv_category_count.setText("x"+details.get(position).getQuantity());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return details.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_category_title, tv_category_subtitle, tv_category_count;
        MyViewHolder(View view) {
            super(view);
            tv_category_count = view.findViewById(R.id.tv_category_count);
            tv_category_title = view.findViewById(R.id.tv_category_title);
            tv_category_subtitle = view.findViewById(R.id.tv_category_subtitle);
        }
    }
}
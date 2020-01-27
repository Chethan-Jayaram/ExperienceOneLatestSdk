package com.example.experienceone.adapter.moduleadapters.housekeeping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.model.raisingticketmodel.ModuleSegmentModel;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.HouseKeeping.Result;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HouseKeepingAdapter extends PagedListAdapter<Result, HouseKeepingAdapter.ItemViewHolder> {

    private Context mCtx;
    private HouseKeepingSubCategory mAdapter;
    private AdapterClickListner adapterClickListner;




    public HouseKeepingAdapter(Context mCtx, AdapterClickListner adapterClickListner) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.adapterClickListner = adapterClickListner;
    }


    public interface AdapterClickListner { // create an interface
        void onItemClickListener(CategoryItem details); // create callback function
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.category_header_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Result result = getItem(position);

        try {
            holder.tv_header_title.setText(result.getName());
            mAdapter = new HouseKeepingSubCategory(result.getCategoryItem(),v -> {
                adapterClickListner.onItemClickListener(v);
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mCtx);
            holder.sub_category_recylcer.setLayoutManager(mLayoutManager);
            holder.sub_category_recylcer.setNestedScrollingEnabled(false);
            holder.sub_category_recylcer.setItemAnimator(new DefaultItemAnimator());
            holder.sub_category_recylcer.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static DiffUtil.ItemCallback<Result> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Result>() {
                @Override
                public boolean areItemsTheSame(@NotNull Result oldItem, Result newItem) {

                    return oldItem.getId() == newItem.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Result oldItem, Result newItem) {
                    return oldItem.equals(newItem);
                }
            };


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_header_title;
        private RecyclerView sub_category_recylcer;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_header_title = itemView.findViewById(R.id.tv_header_title);
            sub_category_recylcer = itemView.findViewById(R.id.sub_category_recylcer);
        }


    }
}

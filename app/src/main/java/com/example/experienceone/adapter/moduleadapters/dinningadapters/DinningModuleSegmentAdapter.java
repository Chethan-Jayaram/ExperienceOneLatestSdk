package com.example.experienceone.adapter.moduleadapters.dinningadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;
import com.example.experienceone.pojo.dinning.CategoryItem;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class DinningModuleSegmentAdapter extends RecyclerView.Adapter<DinningModuleSegmentAdapter.MyViewHolder> {
    private List<CategoryItem> details;



    public DinningModuleSegmentAdapter(List<CategoryItem> details) {
        this.details=details;
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
            holder.tv_category_subtitle.setText(details.get(position).getItemName());
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

        private TextView  tv_category_subtitle, tv_category_count;
        MyViewHolder(View view) {
            super(view);
            tv_category_count = view.findViewById(R.id.tv_category_count);
            tv_category_subtitle = view.findViewById(R.id.tv_category_subtitle);
        }
    }
}
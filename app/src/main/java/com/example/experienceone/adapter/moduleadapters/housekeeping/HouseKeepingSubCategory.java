package com.example.experienceone.adapter.moduleadapters.housekeeping;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HouseKeepingSubCategory  extends RecyclerView.Adapter<HouseKeepingSubCategory.MyViewHolder> {
   private List<CategoryItem> mCategoryItem;
    private AdapterClickListner adapterClickListner;


    public HouseKeepingSubCategory(List<CategoryItem> categoryItem,AdapterClickListner adapterClickListner) {
        this.mCategoryItem=categoryItem;
        this.adapterClickListner=adapterClickListner;
    }
    public interface AdapterClickListner { // create an interface
        void onItemClickListener(CategoryItem categoryItem); // create callback function
    }
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_subcategory_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        try {
            holder.tv_category_desc.setVisibility(View.GONE);
           if(mCategoryItem.get(position).getQuantity()!=null){
               holder.tv_count.setText(mCategoryItem.get(position).getQuantity());
           }
            holder.tv_category_title.setText(mCategoryItem.get(position).getName());
           if(!mCategoryItem.get(position).getDescription().isEmpty()){
               holder.tv_category_desc.setVisibility(View.VISIBLE);
               holder.tv_category_desc.setText(mCategoryItem.get(position).getDescription());
           }
            holder.img_add.setOnClickListener(v -> {
                try {
                    holder.tv_count.setText(String.valueOf(GlobalClass.numberStepperAdd(Integer.parseInt( holder.tv_count.getText().toString()))));
                    mCategoryItem.get(position).setQuantity( holder.tv_count.getText().toString());
                    mCategoryItem.get(position).setTitle(mCategoryItem.get(position).getName());
                    mCategoryItem.get(position).setDescription("");
                    adapterClickListner.onItemClickListener(mCategoryItem.get(position));
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            holder.img_mius.setOnClickListener(v -> {
                try {
                    holder.tv_count.setText(String.valueOf(GlobalClass.numberStepperSub(Integer.parseInt( holder.tv_count.getText().toString()))));
                    mCategoryItem.get(position).setQuantity( holder.tv_count.getText().toString());
                    mCategoryItem.get(position).setTitle( mCategoryItem.get(position).getName());
                    mCategoryItem.get(position).setDescription("");
                    adapterClickListner.onItemClickListener(mCategoryItem.get(position));
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_category_title, tv_category_desc, tv_count;
        private ImageView img_add, img_mius;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category_title = itemView.findViewById(R.id.tv_category_title);
            tv_category_desc = itemView.findViewById(R.id.tv_category_desc);
            tv_count = itemView.findViewById(R.id.tv_count);
            img_add = itemView.findViewById(R.id.img_add);
            img_mius = itemView.findViewById(R.id.img_mius);
        }
    }
}
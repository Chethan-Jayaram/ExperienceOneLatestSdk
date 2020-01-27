package com.example.experienceone.adapter.generaladapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.services.ObjectListner;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.HouseKeeping.Result;
import java.util.List;

public class ServiceModuleAdapter extends RecyclerView.Adapter {

    private List<ObjectListner> mObjectListenr;
    private ModuleAdapterlistner moduleAdapterlistner;

    public ServiceModuleAdapter() {
    }

    public ServiceModuleAdapter(List<ObjectListner> mObjectListenr, ModuleAdapterlistner moduleAdapterlistner) {
        this.mObjectListenr = mObjectListenr;
        this.moduleAdapterlistner = moduleAdapterlistner;
    }

    public interface ModuleAdapterlistner { // create an interface
        void onItemClickListener(ObjectListner mObjectListenr); // create callback function
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType) {
            case ObjectListner.TYPE_CATEGORY_CARD:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_header_card, parent, false);
                return new HeaderCategory(itemView);
            case ObjectListner.TYPE_SUBCATEGORY_CARD:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_subcategory_card, parent, false);
                return new SubHeaderCategory(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_header_card, parent, false);
                return new HeaderCategory(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ObjectListner.TYPE_CATEGORY_CARD:
                ((HeaderCategory) holder).bindView(position);
                break;
            case ObjectListner.TYPE_SUBCATEGORY_CARD:
                ((SubHeaderCategory) holder).bindView(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mObjectListenr == null) {
            return 0;
        } else {
            return mObjectListenr.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mObjectListenr.get(position).getType();
    }

    class HeaderCategory extends RecyclerView.ViewHolder {

        private TextView tv_header_title;

        HeaderCategory(@NonNull View itemView) {
            super(itemView);
            tv_header_title = itemView.findViewById(R.id.tv_header_title);
        }

        void bindView(int position) {
            try {
                Result result = (Result) mObjectListenr.get(position);
                tv_header_title.setText(result.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SubHeaderCategory extends RecyclerView.ViewHolder {

        private TextView tv_category_title, tv_category_desc, tv_count;
        private ImageView img_add, img_mius;

        SubHeaderCategory(@NonNull View itemView) {
            super(itemView);
            tv_category_title = itemView.findViewById(R.id.tv_category_title);
            tv_category_desc = itemView.findViewById(R.id.tv_category_desc);
            tv_count = itemView.findViewById(R.id.tv_count);
            img_add = itemView.findViewById(R.id.img_add);
            img_mius = itemView.findViewById(R.id.img_mius);
        }

        void bindView(int position) {
            try {
                CategoryItem item = (CategoryItem) mObjectListenr.get(position);
                tv_category_title.setText(item.getName());
                tv_category_desc.setText(item.getDescription());
                img_add.setOnClickListener(v -> {
                    try {
                        tv_count.setText(String.valueOf(GlobalClass.numberStepperAdd(Integer.parseInt(tv_count.getText().toString()))));
                        item.setQuantity(tv_count.getText().toString());
                        item.setTitle(item.getName());
                        item.setDescription("");
                        moduleAdapterlistner.onItemClickListener(item);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                img_mius.setOnClickListener(v -> {
                    try {
                        tv_count.setText(String.valueOf(GlobalClass.numberStepperSub(Integer.parseInt(tv_count.getText().toString()))));
                        item.setQuantity(tv_count.getText().toString());
                        item.setTitle(item.getName());
                        item.setDescription("");
                        moduleAdapterlistner.onItemClickListener(item);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}

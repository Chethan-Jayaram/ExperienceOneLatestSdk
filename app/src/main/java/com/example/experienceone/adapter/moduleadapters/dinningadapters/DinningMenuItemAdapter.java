package com.example.experienceone.adapter.moduleadapters.dinningadapters;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;


import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.dinning.CategoryItem;


import java.util.List;

public class DinningMenuItemAdapter extends RecyclerView.Adapter<DinningMenuItemAdapter.SubHeaderCategory> {

    private MenuAdapterlistner menuAdapterlistner;
    private  Integer count=0;
    private  Double price=0.0;
    private List<CategoryItem> mCategoryItems;



    public DinningMenuItemAdapter(List<CategoryItem> mCategoryItems,Integer count,Double price,MenuAdapterlistner menuAdapterlistner) {
        this.count=count;
        this.price=price;
        this.mCategoryItems=mCategoryItems;
        this.menuAdapterlistner = menuAdapterlistner;
    }

    public interface MenuAdapterlistner { // create an interface
        void onItemClickListener(CategoryItem categoryItem,Double  price, int positon); // create callback function
    }

    @NonNull
    @Override
    public SubHeaderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category_subcategory_card, parent, false);
                return new SubHeaderCategory(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull SubHeaderCategory holder, int position) {
        try {
          if(mCategoryItems.get(position).getQuantity()!=null) {
                holder.tv_count.setText(mCategoryItems.get(position).getQuantity().toString());
            }
            holder.tv_category_title.setText(mCategoryItems.get(position).getItemName());
            holder.tv_category_price.setText(mCategoryItems.get(position).getItemPrice() + " Rs");
            holder.tv_category_price.setTextColor(Color.BLACK);
            holder.tv_category_price.setTypeface( holder.tv_category_price.getTypeface(), Typeface.BOLD);
            holder.tv_category_desc.setText(mCategoryItems.get(position).getItemDescription());
            holder.img_add.setOnClickListener(v -> {
                try {
                    if (Integer.parseInt( holder.tv_count.getText().toString()) < 100) {
                        count += 1;
                        price += Double.parseDouble(mCategoryItems.get(position).getItemPrice());
                    }
                    holder.tv_count.setText(String.valueOf(GlobalClass.numberStepperAdd(Integer.parseInt( holder.tv_count.getText().toString()))));
                    mCategoryItems.get(position).setQuantity(Integer.valueOf(holder.tv_count.getText().toString()));
                    mCategoryItems.get(position).setTitle(mCategoryItems.get(position).getItemName()+"("+mCategoryItems.get(position).getItemCode()+")");
                    mCategoryItems.get(position).setDescription(mCategoryItems.get(position).getItemDescription());
                    mCategoryItems.get(position).setPrice(mCategoryItems.get(position).getItemPrice());
                    menuAdapterlistner.onItemClickListener(mCategoryItems.get(position),price,position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            holder.img_mius.setOnClickListener(v -> {
                try {
                    if (Integer.parseInt(holder.tv_count.getText().toString()) >0) {
                        count -= 1;
                        price -= Double.parseDouble(mCategoryItems.get(position).getItemPrice());
                    }
                    holder.tv_count.setText(String.valueOf(GlobalClass.numberStepperSub(Integer.parseInt(holder.tv_count.getText().toString()))));
                    mCategoryItems.get(position).setQuantity(Integer.valueOf(holder.tv_count.getText().toString()));
                    mCategoryItems.get(position).setTitle(mCategoryItems.get(position).getItemName());
                    mCategoryItems.get(position).setDescription(mCategoryItems.get(position).getItemDescription());
                    mCategoryItems.get(position).setPrice(mCategoryItems.get(position).getItemPrice());
                    menuAdapterlistner.onItemClickListener(mCategoryItems.get(position),price,position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return  mCategoryItems.size();
    }




    class SubHeaderCategory extends RecyclerView.ViewHolder {

        private TextView tv_category_title, tv_category_desc, tv_count,tv_category_price;
        private ImageView img_add, img_mius;

        SubHeaderCategory(@NonNull View itemView) {
            super(itemView);
            tv_category_price = itemView.findViewById(R.id.tv_category_price);
            tv_category_title = itemView.findViewById(R.id.tv_category_title);
            tv_category_desc = itemView.findViewById(R.id.tv_category_desc);
            tv_count = itemView.findViewById(R.id.tv_count);
            img_add = itemView.findViewById(R.id.img_add);
            img_mius = itemView.findViewById(R.id.img_mius);
        }
    }
}

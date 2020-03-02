package com.example.experienceone.adapter.generaladapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.experienceone.R;
import com.example.experienceone.fragment.general.MobileCheckInFragment;
import com.example.experienceone.helper.GlobalClass;

import java.util.List;

public class MobileCheckinImagePickerAdapter extends RecyclerView.Adapter<MobileCheckinImagePickerAdapter.ViewHolder> {


    private   List<Image> images;
    private  GlobalClass.AdapterClickListner onclicklistner;


    public MobileCheckinImagePickerAdapter(List<Image> images,GlobalClass.AdapterClickListner onclicklistner) {
        this.images = images;
        this.onclicklistner=onclicklistner;
    }

    @NonNull
    @Override
    public MobileCheckinImagePickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mobile_chekin_image_picker_recycler_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MobileCheckinImagePickerAdapter.ViewHolder holder, int position) {
        holder.iv_holder.setImageBitmap(GlobalClass.getBitmap(images.get(position).getPath()));
        holder.iv_btn_cancel.setOnClickListener(v->{
            try {
                images.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                onclicklistner.onItemClickListener(holder.getAdapterPosition());
            }catch (Exception e){
                e.printStackTrace();
            }
        });

    }


    @Override
    public int getItemCount() {
        return images.size() ;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_holder, iv_btn_cancel;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_holder = itemView.findViewById(R.id.iv_holder);
            iv_btn_cancel = itemView.findViewById(R.id.iv_btn_cancel);

        }
    }
}

package com.example.experienceone.adapter.moduleadapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.tourguide.Image;
import com.example.experienceone.pojo.tourguide.Result;
import org.jetbrains.annotations.NotNull;
import java.util.List;


import me.relex.circleindicator.CircleIndicator3;

public class TourGuideAdapter extends RecyclerView.Adapter<TourGuideAdapter.MyViewHolder> {

    private List<Result> item_;
    private Context mContext;
    private GlobalClass.AdapterClickListner clickListner;

    public TourGuideAdapter(Context context,List<Result> item_,GlobalClass.AdapterClickListner clickListner) {
        this.clickListner=clickListner;
        this.item_ = item_;
        this.mContext=context;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_guide_recycler_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        try {

            holder.tour_title.setText(item_.get(position).getTitle());
            holder.tour_loc.setText(item_.get(position).getCity() + " | " + item_.get(position).getState() + " | " + item_.get(position).getCountry());
            holder.tour_desc.setText(item_.get(position).getDescription());
           if(item_.get(position).getDistance()!=null){
               holder.tour_distance.setText(item_.get(position).getDistance() + " kms from hotel");
           }else{
               holder.tour_distance.setText(0 + " kms from hotel");
           }
            holder.tour_button.setOnClickListener(v -> {
                clickListner.onItemClickListener(position);
            });
            SlidingImage_Adapter adapter=new SlidingImage_Adapter(mContext,item_.get(position).getImage());            holder.tour_indicator.setViewPager(holder.tour_pager);
            holder.tour_pager.setAdapter(adapter);
            holder.tour_indicator.setViewPager(holder.tour_pager);
            holder.tour_timings.setText(GlobalClass.outputTimeFormat.format(GlobalClass.inputdateformat.parse(item_.get(position).getFromTime()))+
                    " - "+GlobalClass.outputTimeFormat.format(GlobalClass.inputdateformat.parse(item_.get(position).getToTime())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return item_.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewPager2 tour_pager;
        private CircleIndicator3 tour_indicator;
        private TextView tour_title, tour_loc, tour_timings, tour_desc, tour_distance, tour_button;

        MyViewHolder(View view) {
            super(view);
            tour_title = view.findViewById(R.id.tour_title);
            tour_loc = view.findViewById(R.id.tour_loc);
            tour_timings = view.findViewById(R.id.tour_timings);
            tour_desc = view.findViewById(R.id.tour_desc);
            tour_distance = view.findViewById(R.id.tour_distance);
            tour_button = view.findViewById(R.id.tour_button);
            tour_pager = view.findViewById(R.id.tour_pager);
            tour_indicator = view.findViewById(R.id.tour_indicator);
        }
    }

    class SlidingImage_Adapter extends RecyclerView.Adapter<SlidingImage_Adapter.ViewHolder> {

        private List<Image> images;
        private Context context;


         SlidingImage_Adapter(Context context, List<Image> images) {
            this.context = context;
            this.images=images;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View imageLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_sliding_images, parent, false);
            return new ViewHolder(imageLayout);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Glide.with(context).load(images.get(position).getImage()).into(holder.img_tour);
        }

        @Override
        public int getItemCount() {
            return images.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
             private ImageView img_tour;
             ViewHolder(@NonNull View itemView) {
                super(itemView);
                 img_tour =  itemView.findViewById(R.id.img_tour);
            }
        }
    }
}
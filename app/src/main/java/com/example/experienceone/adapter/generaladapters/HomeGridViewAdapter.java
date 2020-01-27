package com.example.experienceone.adapter.generaladapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.dashboardelements.DashboardItem;
import com.example.experienceone.pojo.dashboardelements.Result;

import java.util.List;

public class HomeGridViewAdapter extends BaseAdapter {
    private List<DashboardItem> item;
    private Context mContext;
    private GridAdapterlistner gridAdapterlistner;
    private   LayoutInflater inflater;

    public HomeGridViewAdapter(Context mContext, List<DashboardItem> item, GridAdapterlistner gridAdapterlistner) {
        this.mContext=mContext;
        this.item=item;
        this.gridAdapterlistner=gridAdapterlistner;
        inflater = (LayoutInflater.from(mContext));
    }
    public interface GridAdapterlistner { // create an interface
        void onItemClickListener(int position); // create callback function
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub\
        // TODO Auto-generated method stub
        View grid = null;
        try {
            if (convertView == null) {
                grid = inflater.inflate(R.layout.home_row, null);
                TextView textView = grid.findViewById(R.id.grid_text);
                ImageView imageView = grid.findViewById(R.id.grid_image);
                textView.setText(item.get(position).getTitle());
                Glide.with(mContext).load(item.get(position).getImage()).into(imageView);
                imageView.setOnClickListener(v->{
                    gridAdapterlistner.onItemClickListener(position);
                });
                if(!GlobalClass.hasActiveBooking){
                    textView.setEnabled(false);
                    imageView.setEnabled(false);
                }
            } else {
                grid = (View) convertView;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return grid;
    }

}

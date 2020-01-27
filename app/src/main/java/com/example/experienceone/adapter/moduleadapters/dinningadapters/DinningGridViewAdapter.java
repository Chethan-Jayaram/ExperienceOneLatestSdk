package com.example.experienceone.adapter.moduleadapters.dinningadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.experienceone.R;

import com.example.experienceone.pojo.dinning.Result;

import java.util.List;

public class DinningGridViewAdapter extends BaseAdapter {

    private List<Result> item;
    private Context mContext;
    private GridAdapterlistner gridAdapterlistner;

    public DinningGridViewAdapter(Context mContext, List<Result> item, GridAdapterlistner gridAdapterlistner) {
        this.mContext = mContext;
        this.item = item;
        this.gridAdapterlistner = gridAdapterlistner;
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
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                grid = inflater.inflate(R.layout.dinning_grid_content, null);
                TextView grid_dinning_text=grid.findViewById(R.id.grid_dinning_text);
                ImageView grid_dining_image=grid.findViewById(R.id.grid_dining_image);

                grid_dinning_text.setText(item.get(position).getMenuName());
                grid_dining_image.setImageResource(R.drawable.ic_local_dining);
                grid_dining_image.setOnClickListener(v -> {
                    gridAdapterlistner.onItemClickListener(position);
                });

            } else {
                grid = convertView;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return grid;
    }
}

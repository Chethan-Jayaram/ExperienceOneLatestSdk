package com.example.experienceone.adapter.moduleadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.experienceone.R;
import com.example.experienceone.pojo.emergency.Result;
import java.util.List;

public class EmergencyGridViewAdapter extends BaseAdapter {


    private List<Result> item;
    private Context mContext;
    private GridAdapterlistner gridAdapterlistner;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;

    public EmergencyGridViewAdapter(Context mContext, List<Result> item, GridAdapterlistner gridAdapterlistner) {
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
                grid = inflater.inflate(R.layout.emergency_recycler_content, null);
                ImageView imageView = grid.findViewById(R.id.emergency_img);
                RadioButton radioButton = grid.findViewById(R.id.radio_btn);

                TextView Textview = grid.findViewById(R.id.tv_emergency);
                Textview.setText(item.get(position).getTitle());
                Glide.with(mContext).load(item.get(position).getImage()).into(imageView);
                imageView.setOnClickListener(v -> {
                    radioButton.performClick();
                });
                radioButton.setOnClickListener(v -> {
                    selectEmgergencyOption(position,v,radioButton);
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

    private void selectEmgergencyOption(int position, View v, RadioButton radioButton) {
        if ((position != mSelectedPosition && mSelectedRB != null)) {
            mSelectedRB.setChecked(false);
        }
        mSelectedPosition = position;
        mSelectedRB = (RadioButton) v;
        handelRadioButton(radioButton, position);
        gridAdapterlistner.onItemClickListener(position);
    }


    private void handelRadioButton(RadioButton radioButton, int position) {


        if (mSelectedPosition != position) {
            item.get(position).setRadioButtonClicked(false);
            radioButton.setChecked(false);
        } else {
            item.get(position).setRadioButtonClicked(true);
            radioButton.setChecked(true);
            if (mSelectedRB != null && radioButton != mSelectedRB) {
                mSelectedRB = radioButton;
            }
        }
    }


}

package com.example.experienceone.adapter.moduleadapters.travel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.travel.CarTariff;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TravelRadioAdapter extends RecyclerView.Adapter<TravelRadioAdapter.MyViewHolder> {

    private List<CarTariff> carTariff;
    private int mSelectedItem = -1;
    private GlobalClass.AdapterClickListner clickListner;


    TravelRadioAdapter(List<CarTariff> carTariff, GlobalClass.AdapterClickListner clickListner) {
        this.carTariff = carTariff;
        this.clickListner = clickListner;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_radio_recycler_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        try {

            holder.bindView(position);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return carTariff.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_fare;
        private RadioButton radio_btn;
        View.OnClickListener clickListener;

        MyViewHolder(View view) {
            super(view);
            tv_fare = view.findViewById(R.id.tv_fare);
            radio_btn = view.findViewById(R.id.radio_btn);


        }

        public void bindView(int position) {

            radio_btn.setOnClickListener(v -> {
                clickListner.onItemClickListener(position);
                mSelectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
            radio_btn.setChecked(position == mSelectedItem);
            tv_fare.setText(carTariff.get(position).getAmount());
            radio_btn.setText(carTariff.get(position).getLabel());

        }
    }

}

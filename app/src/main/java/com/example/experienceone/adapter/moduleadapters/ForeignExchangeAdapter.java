package com.example.experienceone.adapter.moduleadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.foreignexchange.Result;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForeignExchangeAdapter extends RecyclerView.Adapter<ForeignExchangeAdapter.MyViewHolder> {

    private List<Result> item_;
    GlobalClass.AdapterClickListner adapterClickListner;



    public ForeignExchangeAdapter(List<Result> item_,GlobalClass.AdapterClickListner adapterClickListner) {
        this.item_=item_;
        this.adapterClickListner=adapterClickListner;
    }


    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.foreign_exchange_recycler_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        try{
            holder.tv_currency_name.setText(item_.get(position).getName());
            holder.tv_currency_name.setOnClickListener(v->{
                adapterClickListner.onItemClickListener(position);
            });
            holder.exg_radio_btn.setOnClickListener(v->{
                adapterClickListner.onItemClickListener(position);
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return item_.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_currency_name;
        private RadioButton exg_radio_btn;
        MyViewHolder(View view) {
            super(view);
            tv_currency_name = view.findViewById(R.id.tv_currency_name);
            exg_radio_btn=view.findViewById(R.id.exg_radio_btn);

        }
    }
}
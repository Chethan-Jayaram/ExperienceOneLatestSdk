package com.taj.doorunlock.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.pojo.Data;

import static com.taj.doorunlock.helper.GlobalClass.inputdateformat;
import static com.taj.doorunlock.helper.GlobalClass.outputdateformat;


public class ActiveBookingAdapter extends RecyclerView.Adapter<ActiveBookingAdapter.ViewHolder> {


    private GlobalClass.AdapterClickListner clickListner;
    private Data mData;
    private Integer size;


    public ActiveBookingAdapter(Integer size,Data data,GlobalClass.AdapterClickListner clickListner) {
        this.clickListner = clickListner;
        this.mData = data;
        this.size=size;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activebooking_recycler_content,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {

                holder.tv_room.setText(mData.getRooms());


                    holder.tv_checkin_date_time.setText(outputdateformat.format(inputdateformat.parse(mData.getCheckinDate())));
                    holder.tv_checkout_date_time.setText(outputdateformat.format(inputdateformat.parse(mData.getCheckoutDate())));


              holder.tv_unlock_door.setOnClickListener(v->{
                  clickListner.onItemClickListener(position);
              });

            }catch (Exception e) {
                e.printStackTrace();
            }


    }

    @Override
    public int getItemCount() {
       if(mData!=null){
            return size;
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_room, tv_checkin_date_time,tv_unlock_door,
                tv_checkout_date_time;
        private LinearLayout content_lyt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_checkin_date_time=itemView.findViewById(R.id.tv_checkin_date_time);
            tv_checkout_date_time=itemView.findViewById(R.id.tv_checkout_date_time);
            content_lyt=itemView.findViewById(R.id.content_lyt);
            tv_room=itemView.findViewById(R.id.tv_room);
            tv_unlock_door=itemView.findViewById(R.id.tv_unlock_door);
        }
    }
}

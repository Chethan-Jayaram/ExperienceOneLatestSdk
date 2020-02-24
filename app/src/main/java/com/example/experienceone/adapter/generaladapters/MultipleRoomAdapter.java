package com.example.experienceone.adapter.generaladapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.pojo.loginmpin.Room;

import java.util.List;

public class MultipleRoomAdapter extends RecyclerView.Adapter<MultipleRoomAdapter.ViewHolder> {


    private List<Room> mRoom;
    private RoomSelectListner mRoomSelectListner;
    public MultipleRoomAdapter(List<Room> room,RoomSelectListner roomSelectListner) {
        mRoomSelectListner=roomSelectListner;
        mRoom=room;
    }

    public interface RoomSelectListner { // create an interface
        void onItemClickListener(String room_no,Boolean switchState); // create callback function
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mutliple_room_recycler_content,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            holder.tv_room_no.setText("Room No : "+mRoom.get(position).getRoom().getRoomNo());
            holder.room_toggle_switch.setOnClickListener(v->{
                mRoomSelectListner.onItemClickListener(mRoom.get(position).getRoom().getRoomNo()
                        ,holder.room_toggle_switch.isChecked());
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (mRoom.size()>0)
            return mRoom.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_room_no;
        private Switch room_toggle_switch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_room_no=itemView.findViewById(R.id.tv_room_no);
            room_toggle_switch=itemView.findViewById(R.id.room_toggle_switch);
        }
    }
}

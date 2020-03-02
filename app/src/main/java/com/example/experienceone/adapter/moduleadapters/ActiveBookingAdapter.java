package com.example.experienceone.adapter.moduleadapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;
import com.example.experienceone.fragment.general.TicketsList;
import com.example.experienceone.fragment.mystay.BookingHistory;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.mystay.ActiveBookingPojo;
import com.example.experienceone.pojo.mystay.BookingHistoryPojo;

import java.util.List;


public class ActiveBookingAdapter extends RecyclerView.Adapter<ActiveBookingAdapter.ViewHolder> {

    private List<ActiveBookingPojo> activeBookingList;
    private List<BookingHistoryPojo> bookingHistories;
    private GlobalClass.AdapterClickListner clickListner;

    public ActiveBookingAdapter(Context context,List<ActiveBookingPojo> activeBookingList,GlobalClass.AdapterClickListner clickListner) {
        this.activeBookingList=activeBookingList;
        this.clickListner=clickListner;
    }

    public ActiveBookingAdapter(List<BookingHistoryPojo> bookingHistory,GlobalClass.AdapterClickListner clickListner) {
        this.bookingHistories=bookingHistory;
        this.clickListner=clickListner;
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
                if(activeBookingList!=null) {
              holder.tv_ticket_status.setText("BOOKING STATUS : Active");
                    StringBuilder roomno = new StringBuilder();
              if(activeBookingList.get(0).getRoom().size()>1) {
                  for (int i = 0; i < activeBookingList.get(0).getRoom().size(); i++) {
                      roomno.append(activeBookingList.get(0).getRoom().get(i).getRoom().getRoomNo() + ",");
                  }
              }else{
                  roomno.append(activeBookingList.get(0).getRoom().get(0).getRoom().getRoomNo());
              }
              holder.tv_room.setText(roomno.toString().substring(0, roomno.length() - 1));
                    holder.tv_username.setText(activeBookingList.get(position).getGuest().getFirstName() + " " +
                            activeBookingList.get(position).getGuest().getLastName());
                    holder.tv_user_email.setText(activeBookingList.get(position).getGuest().getEmail());
                    holder.tv_user_contact_no.setText(activeBookingList.get(position).getGuest().getContactNumber());
                    holder.tv_checkin_date_time.setText(GlobalClass.outputdateformat.format(GlobalClass.inputdateformat.parse(activeBookingList.get(position).getCheckinDateTime())));
                    holder.tv_checkout_date_time.setText(GlobalClass.outputdateformat.format(GlobalClass.inputdateformat.parse(activeBookingList.get(position).getCheckoutDateTime())));
                }else if(bookingHistories!=null){
                    holder.tv_ticket_status.setText("BOOKING STATUS : Completed");
                    StringBuilder roomno = new StringBuilder();
                    if(bookingHistories.get(0).getRoom().size()>1) {
                        for (int i = 0; i < bookingHistories.get(0).getRoom().size(); i++) {
                            roomno.append(bookingHistories.get(0).getRoom().get(i).getRoom().getRoomNo() + ",");
                        }
                    }else{
                        roomno.append(bookingHistories.get(0).getRoom().get(0).getRoom().getRoomNo());
                    }
                    holder.tv_room.setText(roomno);
                    holder.tv_username.setText(bookingHistories.get(position).getGuest().getFirstName() + " " +
                            bookingHistories.get(position).getGuest().getLastName());
                    holder.tv_user_email.setText(bookingHistories.get(position).getGuest().getEmail());
                    holder.tv_user_contact_no.setText(bookingHistories.get(position).getGuest().getContactNumber());
                    holder.tv_checkin_date_time.setText(GlobalClass.outputdateformat.format(GlobalClass.inputdateformat.parse(bookingHistories.get(position).getCheckinDateTime())));
                    holder.tv_checkout_date_time.setText(GlobalClass.outputdateformat.format(GlobalClass.inputdateformat.parse(bookingHistories.get(position).getCheckoutDateTime())));
                }
                holder.content_lyt.setOnClickListener(v->{
                   clickListner.onItemClickListener(position);
                });

            }catch (Exception e) {
                e.printStackTrace();
            }


    }

    @Override
    public int getItemCount() {
        if(activeBookingList!=null){
            return activeBookingList.size();
        }else if(bookingHistories!=null){
            return bookingHistories.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_ticket_status,tv_room,tv_username,
                tv_user_email,tv_user_contact_no,tv_checkin_date_time,
                tv_checkout_date_time;
        private LinearLayout content_lyt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username=itemView.findViewById(R.id.tv_username);
            tv_user_email=itemView.findViewById(R.id.tv_user_email);
            tv_user_contact_no=itemView.findViewById(R.id.tv_user_contact_no);
            tv_checkin_date_time=itemView.findViewById(R.id.tv_checkin_date_time);
            tv_checkout_date_time=itemView.findViewById(R.id.tv_checkout_date_time);
            content_lyt=itemView.findViewById(R.id.content_lyt);
            tv_room=itemView.findViewById(R.id.tv_room);
            tv_ticket_status=itemView.findViewById(R.id.tv_ticket_status);


        }
    }
}

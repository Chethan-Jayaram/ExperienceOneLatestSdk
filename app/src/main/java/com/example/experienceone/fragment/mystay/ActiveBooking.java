package com.example.experienceone.fragment.mystay;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.adapter.moduleadapters.ActiveBookingAdapter;
import com.example.experienceone.fragment.general.TicketsList;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.mystay.ActiveBookingPojo;
import com.example.experienceone.pojo.mystay.MyStayPojo;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class ActiveBooking extends Fragment {

    private List<?> activeBooking;
    private  RecyclerView recycler_active_booking;
    private  Context mContext;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_booking, container, false);
        try {
             mContext = view.getContext();
             recycler_active_booking = view.findViewById(R.id.recycler_active_booking);
            Bundle data = getArguments();
            if (data != null) {
             activeBooking = data.getParcelableArrayList("list");
            }
            setadapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void setadapter() {

        ActiveBookingAdapter adapter = new ActiveBookingAdapter(mContext,(List<ActiveBookingPojo>) activeBooking,v->{
           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new TicketsList()).addToBackStack(null).commit();
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recycler_active_booking.setLayoutManager(mLayoutManager);
        recycler_active_booking.setNestedScrollingEnabled(false);
        recycler_active_booking.setItemAnimator(new DefaultItemAnimator());
        recycler_active_booking.setAdapter(adapter);
    }
}

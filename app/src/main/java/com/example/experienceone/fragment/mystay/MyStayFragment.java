package com.example.experienceone.fragment.mystay;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.mystay.ActiveBookingPojo;
import com.example.experienceone.pojo.mystay.BookingHistoryPojo;
import com.example.experienceone.pojo.mystay.MyStayPojo;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


public class MyStayFragment extends Fragment implements ApiListener {

    private Context mContext;
    private  ViewPager view_pager;
    private  ArrayList<ActiveBookingPojo> activeBookings;
    private  ArrayList<BookingHistoryPojo> bookingHistories;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mystay,container,false);
        mContext=view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("My Stay");
        view_pager = view.findViewById(R.id.view_pager);
        TabLayout tab_lyt = view.findViewById(R.id.tab_lyt);
        tab_lyt.setupWithViewPager(view_pager);
        loading=view.findViewById(R.id.loading);

        getBooking();

        return view;
    }
    private void getBooking() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<MyStayPojo> hotelDirectory = api.getBookingDetails(headerMap);
        APIResponse.getCallRetrofit(hotelDirectory, "bookings",mContext , this);
    }




    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            MyStayPojo myStayPojo = (MyStayPojo) response.body();
            if (apiCallName.equalsIgnoreCase("bookings")) {
                if (myStayPojo.getStatus().equalsIgnoreCase("Success")) {
                    activeBookings = (ArrayList<ActiveBookingPojo>) myStayPojo.getResult().getActiveBooking();
                    bookingHistories = (ArrayList<BookingHistoryPojo>) myStayPojo.getResult().getBookingHistory();
                    setupViewPager(view_pager);
                } else {
                    GlobalClass.showErrorMsg(mContext, myStayPojo.getError());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {
        try{
            loading.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getChildFragmentManager());
        adapter.addFragment(new ActiveBooking(), "Active Bookings", activeBookings);
        adapter.addFragment(new BookingHistory(), "Booking History",  bookingHistories);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title,ArrayList<?> bookings) {
            try {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) bookings);
                fragment.setArguments(bundle);
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

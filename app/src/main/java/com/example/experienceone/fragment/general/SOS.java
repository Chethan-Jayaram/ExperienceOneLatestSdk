package com.example.experienceone.fragment.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.helper.OnDragTouchListener;
import com.example.experienceone.helper.OnDragTouchListener.OnDragActionListener;
import com.example.experienceone.pojo.GeneralPojo;
import com.example.experienceone.pojo.GuestDeatils;
import com.example.experienceone.pojo.sos.SoSDetails;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class SOS extends Fragment implements ApiListener {

    private Context mContext;
    private RelativeLayout swipe_lyt;
    private Boolean istriggered = false;
    private ImageView destination, initial;
    private ProgressBar loading;


    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sos_fragment, container, false);
        try {
            mContext = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);

            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            toolbar_title.setText("SOS");
            initial = view.findViewById(R.id.initial);
            destination = view.findViewById(R.id.destination);
            swipe_lyt = view.findViewById(R.id.swipe_lyt);
            loading = view.findViewById(R.id.loading);
            swipe_lyt.setVisibility(View.GONE);
            getSOSstatus();

            initial.setOnTouchListener(new OnDragTouchListener(initial, new OnDragActionListener() {
                @Override
                public void onDragStart(View view) {
                }

                @Override
                public void onDragEnd(View view, Boolean max) {
                    try {
                        if (max){
                            GradientDrawable drawable = (GradientDrawable) swipe_lyt.getBackground();
                            drawable.setColor(Color.RED);
                            if (!istriggered) {
                                triggerSoS();
                                destination.setImageResource(R.drawable.sos);
                                initial.setImageResource(0);
                            }
                        }else {
                            initial.setX(0);
                            initial.setY(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void getSOSstatus() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<SoSDetails> guest = api.getGuestSoS(headerMap);
        APIResponse.callBackgroundRetrofit(guest, "getGuest", mContext, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.iv_sos).setVisibility(View.GONE);
        GlobalClass.mPreviousRouteName="";
        for (int i = 0; i < GlobalClass.headerList.size(); i++) {
            GlobalClass.headerList.get(i).setSelected(false);
            if (!GlobalClass.headerList.get(i).getRoutesSubcategory().isEmpty()) {
                for (int j = 0; j < GlobalClass.headerList.get(i).getRoutesSubcategory().size(); j++) {
                    GlobalClass.headerList.get(i).getRoutesSubcategory().get(j).setSelected(false);
                }
            }
        }
    }

    private void triggerSoS() {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Map<String, String> body = new HashMap<>();
        body.put("guest", String.valueOf(GlobalClass.Guest_id));
        body.put("sos_status", "true");
        Call<GeneralPojo> houseKeeping = api.triggerSOS(headerMap, body);
        APIResponse.postCallRetrofit(houseKeeping, "triggerSOS", mContext, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("triggerSOS")) {
                GeneralPojo generalPojo = (GeneralPojo) response.body();
                if (generalPojo.getStatus().equalsIgnoreCase("Success")) {
                    GlobalClass.ShowAlet(mContext, "Alert", "SOS Alert Triggered successfully");
                    istriggered = true;
                } else {
                    GlobalClass.showErrorMsg(mContext, generalPojo.getError());

                }
            } else if (apiCallName.equalsIgnoreCase("getGuest")) {
                SoSDetails details = (SoSDetails) response.body();
                if (details.getStatus().equalsIgnoreCase("Success")) {
                    swipe_lyt.setVisibility(View.VISIBLE);
                    if(details.getResult().size()!=0) {
                        istriggered = details.getResult().get(0).getSosStatus();
                        if (istriggered) {
                            GradientDrawable drawable = (GradientDrawable) swipe_lyt.getBackground();
                            drawable.setColor(Color.RED);
                            destination.setImageResource(R.drawable.sos);
                        } else {
                            GradientDrawable drawable = (GradientDrawable) swipe_lyt.getBackground();
                            drawable.setColor(Color.GREEN);
                            initial.setImageResource(R.drawable.sos);
                        }
                    }else{
                        GradientDrawable drawable = (GradientDrawable) swipe_lyt.getBackground();
                        drawable.setColor(Color.GREEN);
                        initial.setImageResource(R.drawable.sos);
                    }
                } else {
                    GlobalClass.showErrorMsg(mContext, details.getError());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {
        try {
            loading.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (GlobalClass.hasActiveBooking) {
            getActivity().findViewById(R.id.iv_sos).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.iv_sos).setVisibility(View.GONE);
        }
    }
}

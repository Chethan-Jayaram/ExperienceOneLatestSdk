package com.example.experienceone.fragment.general;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.adapter.generaladapters.MultipleRoomAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.TravelModel;
import com.example.experienceone.model.dinningmodel.DinningSegmentModel;
import com.example.experienceone.model.foreignexchange.ForeignExchangemodel;
import com.example.experienceone.model.raisingticketmodel.ModuleSegmentModel;
import com.example.experienceone.pojo.multiplerooms.MultipleRoomNumber;
import com.example.experienceone.pojo.posttickets.TicketID;
import com.example.experienceone.pojo.support.Support;
import com.example.experienceone.pojo.ticketdetails.TicketDetailsPojo;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class MultipleRoomDialougFragment extends BottomSheetDialogFragment implements ApiListener {

    private ProgressBar mLoading;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private String url = "";
    private ForeignExchangemodel exchangemodel;
    private ModuleSegmentModel houseKeepingModel;
    private DinningSegmentModel dinningSegmentModel;
    private ArrayList<String> room;
    private String mPostCallDecider;
    private Fragment fragment;
    private TravelModel mModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multiple_room_fragment, container, false);
        mContext = view.getContext();
        mLoading = view.findViewById(R.id.loading);
        mRecyclerView = view.findViewById(R.id.multiple_room_recyclerview);
        TextView tv_confirm_button = view.findViewById(R.id.tv_confirm_button);
        room = new ArrayList<>();
        Bundle data = getArguments();
        url = data.getString("url");
        houseKeepingModel = data.getParcelable("subcategory");
        mPostCallDecider=data.getString("module");
        mModel=data.getParcelable("travelCategory");
        exchangemodel=data.getParcelable("ForeginCategory");
        dinningSegmentModel=data.getParcelable("DinningModel");
        getRoomNumbers();
        tv_confirm_button.setOnClickListener(v -> {
            if(room.size()>0) {
                StringBuilder room_no = new StringBuilder();
                for (String s : room) {
                    room_no.append(s + " , ");
                }
                if(mPostCallDecider.equalsIgnoreCase("HouseKeeping")){
                    houseKeepingModel.setRoom_no(room_no.toString().substring(0, room_no.length() - 2));
                    postHosueKeepingList(houseKeepingModel);
                }else if(mPostCallDecider.equalsIgnoreCase("Emergency")){
                    houseKeepingModel.setRoom_no(room_no.toString().substring(0, room_no.length() - 2));
                    postEmergencyRequest(houseKeepingModel);
                }else if(mPostCallDecider.equalsIgnoreCase("travel")){
                    mModel.setRoomNo(room_no.toString().substring(0, room_no.length() - 2));
                    postTravelRequest(mModel);
                }else if(mPostCallDecider.equalsIgnoreCase("foreginExchange")){
                    exchangemodel.setRoom_no(room_no.toString().substring(0, room_no.length() - 2));
                    postforeignExchnage(exchangemodel);
                }else if(mPostCallDecider.equalsIgnoreCase("Dining")){
                    dinningSegmentModel.setRoom_no(room_no.toString().substring(0, room_no.length() - 2));
                    postDinningList(dinningSegmentModel);
            }else if(mPostCallDecider.equalsIgnoreCase("Support")){
                    houseKeepingModel.setRoom_no(room_no.toString().substring(0, room_no.length() - 2));
                    postSupport(houseKeepingModel);
                }
            }else {
                GlobalClass.ShowAlet(mContext,"Message","Please select at least one room number");
            }
        });

        return view;
    }


    private void getRoomNumbers() {
        mLoading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        HashMap headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketDetailsPojo> ticketDetailsPojoCall = api.getRoomNumbers(headerMap);
        APIResponse.getCallRetrofit(ticketDetailsPojoCall, "rooms", mContext, this);
    }

    private void postHosueKeepingList(ModuleSegmentModel houseKeepingModel) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> TicketID = api.postSegmentModule(url, headerMap, houseKeepingModel);
        APIResponse.postCallRetrofit(TicketID, "moduleSegment", mContext, this);
    }

    private void postEmergencyRequest(ModuleSegmentModel houseKeepingModel) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> emergencyCall = api.postEmergencyRequst(headerMap,houseKeepingModel);
        APIResponse.postCallRetrofit(emergencyCall, "postemergency", mContext, this);
    }

    private void postTravelRequest(TravelModel model) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> travelCall = api.postTravelRequst(headerMap,model);
        APIResponse.postCallRetrofit(travelCall, "travelPost", mContext, this);
    }

    private void postforeignExchnage(ForeignExchangemodel exchangemodel) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> TicketID = api.postforeignExchange("foreign-exchange-book-ticket/", headerMap, exchangemodel);
        APIResponse.postCallRetrofit(TicketID, "foreignExchange", mContext, this);
    }
    private void postSupport(ModuleSegmentModel houseKeepingModel) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        HashMap headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> supportCall = api.postSupport(headerMap,houseKeepingModel);
        APIResponse.postCallRetrofit(supportCall, "postSupport", mContext, this);
    }
    private void postDinningList(DinningSegmentModel dinningModuleSegment) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> TicketID = api.postDinningSegment(headerMap, dinningModuleSegment);
        APIResponse.postCallRetrofit(TicketID, "DinningSegment", mContext, this);
    }


    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            mLoading.setVisibility(View.GONE);
            if(apiCallName.equalsIgnoreCase("postemergency") ||apiCallName.equalsIgnoreCase("moduleSegment")||
                    apiCallName.equalsIgnoreCase("DinningSegment")) {
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")) {
                    Bundle bundle = new Bundle();
                    fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt", ticketDetails.getResult().getLayout());
                    bundle.putString("type", "");
                    fragment.setArguments(bundle);
                }else {
                    GlobalClass.showErrorMsg(mContext,ticketDetails.getError());
                }
            }
            if (apiCallName.equalsIgnoreCase("rooms")) {
                MultipleRoomNumber booking = (MultipleRoomNumber) response.body();
                if (booking.getStatus().equalsIgnoreCase("Success")) {
                    MultipleRoomAdapter adapter = new MultipleRoomAdapter(booking.getResult().getActiveBooking().get(0).getRoom(), (room_no, switchState) -> {
                        if (switchState) {
                            room.add(room_no);
                        } else {
                            room.remove(room_no);
                        }
                    });
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerView.setAdapter(adapter);

                } else {
                    GlobalClass.showErrorMsg(mContext, booking.getError());
                }
            } else if (apiCallName.equalsIgnoreCase("moduleSegment")) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
              closeBottomSheetFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
            }else if(apiCallName.equalsIgnoreCase("postemergency")){
                    getActivity().getSupportFragmentManager().popBackStack();
                closeBottomSheetFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
            }else if(apiCallName.equalsIgnoreCase("travelPost")){
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")){
                    Bundle bundle = new Bundle();
                    fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt",ticketDetails.getResult().getLayout());
                    bundle.putString("type","Cab booking");
                    fragment.setArguments(bundle);
                    Fragment backstack = getActivity().getSupportFragmentManager().findFragmentByTag("localtour");
                    if(backstack != null)
                        getActivity().getSupportFragmentManager().popBackStack();
                    closeBottomSheetFragment();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                }else {
                    GlobalClass.showErrorMsg(mContext,ticketDetails.getError());
                }
            }else if (apiCallName.equalsIgnoreCase("foreignExchange")) {
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")) {
                    Bundle bundle = new Bundle();
                    fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt", ticketDetails.getResult().getLayout());
                    bundle.putString("type", "Foreign Exchange");
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().popBackStack();
                    closeBottomSheetFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                } else {
                    GlobalClass.showErrorMsg(mContext, ticketDetails.getError());
                }
            }else if (apiCallName.equalsIgnoreCase("DinningSegment")) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    closeBottomSheetFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
            }else if(apiCallName.equalsIgnoreCase("postSupport")){
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")) {
                    Bundle bundle = new Bundle();
                    Fragment fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt", ticketDetails.getResult().getLayout());
                    bundle.putString("type", "Support");
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().popBackStack();
                    closeBottomSheetFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                }else {
                    GlobalClass.showErrorMsg(mContext,ticketDetails.getError());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeBottomSheetFragment() {
        Fragment myfrag = getActivity().getSupportFragmentManager().findFragmentByTag(GlobalClass.BOTTOM_VIEW);
        if(myfrag != null)
            getActivity().getSupportFragmentManager().beginTransaction().remove(myfrag).commit();
    }

    @Override
    public void onErrorListner() {
        mLoading.setVisibility(View.GONE);
    }
}


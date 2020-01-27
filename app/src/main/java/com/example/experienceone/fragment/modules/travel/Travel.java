package com.example.experienceone.fragment.modules.travel;

import android.content.Context;
import android.os.Build;
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
import com.example.experienceone.adapter.moduleadapters.travel.TravelModuleAdapter;
import com.example.experienceone.fragment.general.TicketDetails;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.helper.PaginationScrollListener;
import com.example.experienceone.model.TravelModel;
import com.example.experienceone.pojo.posttickets.TicketID;
import com.example.experienceone.pojo.travel.CategoryItem;
import com.example.experienceone.pojo.travel.TravelPojo;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.example.experienceone.services.ObjectListner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;

public class Travel extends Fragment implements ApiListener {

    private RecyclerView travel_recycler;
    private Context context;
    private ArrayList<CategoryItem> details=new ArrayList<>();
    private TravelModel model;
    private ProgressBar loading;


    @Override
    public void onResume() {
        super.onResume();
        details.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel, container, false);
        try {
            context = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            toolbar_title.setText("Travel");
            travel_recycler = view.findViewById(R.id.travel_recycler);
            TextView tv_request_cab = view.findViewById(R.id.tv_request_cab);
            loading=view.findViewById(R.id.loading);

            model = new TravelModel();

            getHosueKeepingList();


            tv_request_cab.setOnClickListener(v -> {
                try {
                    model.setTitle("Cab booking");
                    model.setBooking(GlobalClass.Booking_id);
                    List<CategoryItem> categoryItems = GlobalClass.removeDuplicates(details);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        categoryItems = categoryItems.stream().filter(p -> Integer.parseInt(p.getQuantity()) != 0).collect(Collectors.toList());
                    } else {
                        for (int i = 0; i < categoryItems.size(); i++) {
                            if (Integer.parseInt(categoryItems.get(i).getQuantity()) == 0) {
                                categoryItems.remove(categoryItems.get(i));
                            }
                        }
                    }
                    model.setDetails(categoryItems);
                    if (categoryItems.size() > 0) {
                        postTravelRequest(model);
                    } else {
                        GlobalClass.ShowAlet(context, "Alert", "please select a car to book");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }
    private void getHosueKeepingList() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TravelPojo> travelCall = api.getTravelList(headerMap);
        APIResponse.getCallRetrofit(travelCall, "Travel", context, this);
    }
    private void postTravelRequest(TravelModel model) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> travelCall = api.postTravelRequst(headerMap,model);
        APIResponse.postCallRetrofit(travelCall, "travelPost", context, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("Travel")) {
                TravelPojo travel = (TravelPojo) response.body();
                if (travel.getStatus().equalsIgnoreCase("Success")) {
                    List<ObjectListner> mObjectlistner = new ArrayList<>();
                    for (int i = 0; i < travel.getResult().size(); i++) {
                        mObjectlistner.add(travel.getResult().get(i));
                        for (int j = 0; j < travel.getResult().get(i).getCategoryItem().size(); j++) {
                            mObjectlistner.add(travel.getResult().get(i).getCategoryItem().get(j));
                        }
                    }
                    TravelModuleAdapter adapter = new TravelModuleAdapter(mObjectlistner,context, mObjectListenr -> {
                        CategoryItem item = (CategoryItem) mObjectListenr;
                        details.add(item);
                    });
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                    travel_recycler.setLayoutManager(mLayoutManager);
                    travel_recycler.setNestedScrollingEnabled(false);
                    travel_recycler.setItemAnimator(new DefaultItemAnimator());
                    travel_recycler.setAdapter(adapter);
                } else {
                    GlobalClass.showErrorMsg(context, travel.getError());
                }
            }else if(apiCallName.equalsIgnoreCase("travelPost")){
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")){
                    Bundle bundle = new Bundle();
                    Fragment fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt",ticketDetails.getResult().getLayout());
                    bundle.putString("type","Cab booking");
                    fragment.setArguments(bundle);
                    Fragment backstack = getActivity().getSupportFragmentManager().findFragmentByTag("localtour");
                    if(backstack != null)
                        getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                }else {
                    GlobalClass.showErrorMsg(context,ticketDetails.getError());
                }
            }
        } catch (Exception e) {
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
}
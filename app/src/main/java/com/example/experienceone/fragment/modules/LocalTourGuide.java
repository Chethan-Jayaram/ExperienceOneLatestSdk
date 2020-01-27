package com.example.experienceone.fragment.modules;

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
import com.example.experienceone.adapter.moduleadapters.TourGuideAdapter;
import com.example.experienceone.fragment.modules.travel.Travel;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.tourguide.TourGuidePojo;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class LocalTourGuide extends Fragment implements ApiListener {


    private TourGuideAdapter adapter;
    private Context mContext;
    private RecyclerView tour_guide_recycler;
    private ProgressBar loading;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_local_tour_guide,container,false);
        mContext=view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Local Tour Guide");
        tour_guide_recycler = view.findViewById(R.id.tour_guide_recycler);
        loading=view.findViewById(R.id.loading);

        getLocalTourGuideList();


        return view;
    }


    private void getLocalTourGuideList() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TourGuidePojo> tourGuideList = api.getTourGuideList(headerMap);
        APIResponse.getCallRetrofit(tourGuideList, "tourGuideList", mContext, this);

    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {

        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("tourGuideList")) {
                TourGuidePojo tourGuidePojo = (TourGuidePojo) response.body();
                if (tourGuidePojo.getStatus().equalsIgnoreCase("Success")) {
                    TourGuideAdapter adapter= new TourGuideAdapter(mContext,tourGuidePojo.getResult(), position->{

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new Travel(),"localtour").addToBackStack(null).commit();
                    });
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    tour_guide_recycler.setLayoutManager(mLayoutManager);
                    tour_guide_recycler.setNestedScrollingEnabled(false);
                    tour_guide_recycler.setItemAnimator(new DefaultItemAnimator());
                    tour_guide_recycler.setAdapter(adapter);
                } else {
                    GlobalClass.showErrorMsg(mContext, tourGuidePojo.getError());
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

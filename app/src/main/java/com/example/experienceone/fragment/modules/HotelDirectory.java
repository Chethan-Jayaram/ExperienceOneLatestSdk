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
import com.example.experienceone.adapter.moduleadapters.HotelDirectoryAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.hoteldirectory.HotelDirectoryPojo;
import com.example.experienceone.pojo.hoteldirectory.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class HotelDirectory extends Fragment implements ApiListener {

    private Context context;
    private HotelDirectoryAdapter adapter;
    private RecyclerView recycler_hotel_directory;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_directory, container, false);
        context=view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Hotel Directory");
        recycler_hotel_directory=view.findViewById(R.id.recycler_hotel_directory);
        loading=view.findViewById(R.id.loading);

        getHotelDirectory();
        return view;
    }


    private void getHotelDirectory() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<HotelDirectoryPojo> hotelDirectory = api.getHotelDirectory(headerMap);
        APIResponse.getCallRetrofit(hotelDirectory, "hotelDirectory",context , this);
    }
    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            HotelDirectoryPojo hotelDirectory = (HotelDirectoryPojo) response.body();
            if (apiCallName.equalsIgnoreCase("hotelDirectory")) {
                if (hotelDirectory.getStatus().equalsIgnoreCase("Success")) {
                    List<Result> results = hotelDirectory.getResult();
                    adapter = new HotelDirectoryAdapter(results);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    recycler_hotel_directory.setLayoutManager(mLayoutManager);
                    recycler_hotel_directory.setItemAnimator(new DefaultItemAnimator());
                    recycler_hotel_directory.setAdapter(adapter);
                } else {
                    GlobalClass.showErrorMsg(context, hotelDirectory.getError());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {

    }
}
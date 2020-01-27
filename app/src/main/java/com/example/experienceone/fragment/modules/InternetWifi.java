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
import com.example.experienceone.adapter.moduleadapters.WifiAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.wifi.Result;
import com.example.experienceone.pojo.wifi.Wifi;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;

public class InternetWifi extends Fragment implements ApiListener {


    private RecyclerView wifiRecycler;
    private WifiAdapter adapter;
    private Context context;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internet_wifi, container, false);
        context=view.getContext();
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Internet Wifi");
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        wifiRecycler=view.findViewById(R.id.wifiRecycler);
        loading=view.findViewById(R.id.loading);

        getWifiCredentials();
        return view;
    }

    private void getWifiCredentials() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<Wifi> wifiCred = api.getWifiCred(headerMap);
        APIResponse.getCallRetrofit(wifiCred, "wifiCred",context , this);
    }
    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            Wifi wifi = (Wifi) response.body();
            if (apiCallName.equalsIgnoreCase("wifiCred")) {
                if (wifi.getStatus().equalsIgnoreCase("Success")) {
                    List<Result> results = wifi.getResult();
                    adapter = new WifiAdapter(results);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    wifiRecycler.setLayoutManager(mLayoutManager);
                    wifiRecycler.setItemAnimator(new DefaultItemAnimator());
                    wifiRecycler.setAdapter(adapter);
                } else {
                    GlobalClass.showErrorMsg(context, wifi.getError());
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
}

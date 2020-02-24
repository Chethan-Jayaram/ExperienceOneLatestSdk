package com.example.experienceone.adapter.pagination;


import android.app.Application;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.HouseKeeping.HouseKeepingPojo;
import com.example.experienceone.pojo.HouseKeeping.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


public class ItemDataSource extends PageKeyedDataSource<Integer, Result> {
    private String url;
    private static final int FIRST_PAGE = 1;


    ItemDataSource(String url) {
        this.url = url;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Result> callback) {
        APIMethods apiMethods = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<HouseKeepingPojo> data = apiMethods.getHouseKeeping(url, headerMap);
        try {
            HouseKeepingPojo pojo = data.execute().body();
            callback.onResult(pojo.getResult(), null, FIRST_PAGE + 1);
            url = pojo.getNext() != null ? pojo.getNext() : "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Result> callback) {

      /*  APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<HouseKeepingPojo> call = api.getHouseKeeping(url, headerMap);
        try {
            HouseKeepingPojo pojo = call.execute().body();
            Integer key = (params.key > 1) ? params.key - 1 : null;
            url = pojo.getNext() != null ? pojo.getNext() : "";
            callback.onResult(pojo.getResult(), key);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Result> callback) {
        if (!url.isEmpty()) {
            APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Authorization", "bearer " + GlobalClass.token);
            Call<HouseKeepingPojo> call = api.getHouseKeeping(url, headerMap);
            try {
                HouseKeepingPojo pojo = call.execute().body();
                Integer key = pojo.getNext() != null ? params.key + 1 : null;
                url = pojo.getNext() != null ? pojo.getNext() : "";
                callback.onResult(pojo.getResult(), key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

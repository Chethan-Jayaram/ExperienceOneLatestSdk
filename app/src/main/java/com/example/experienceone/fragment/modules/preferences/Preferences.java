package com.example.experienceone.fragment.modules.preferences;

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
import com.example.experienceone.adapter.moduleadapters.PrefrencesAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.GeneralPojo;
import com.example.experienceone.pojo.preference.PreferencePojo;
import com.example.experienceone.pojo.preference.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class Preferences extends Fragment implements ApiListener {


    private RecyclerView pref_recycler_view;
    private Context mContext;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);
        mContext = view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        FloatingActionButton fab_pref_add = view.findViewById(R.id.fab_pref_add);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Preferences");
        pref_recycler_view = view.findViewById(R.id.pref_recycler_view);
        loading=view.findViewById(R.id.loading);

        getprefList();


        fab_pref_add.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Fragment fragment = new CreateUpdatePreferences();
            bundle.putString("Operation","Create");
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack("create").commit();
        });

        return view;
    }


    private void getprefList() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<PreferencePojo> getPref = api.getPrefrenceList(headerMap);
        APIResponse.getCallRetrofit(getPref, "getPref", mContext, this);
    }
    private void deletePrefrences(Integer id) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<GeneralPojo> putStatusPrefrence = api.deletePrefrence(String.valueOf(id), headerMap);
        APIResponse.postCallRetrofit(putStatusPrefrence, "deletePref", mContext, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("getPref")) {
                PreferencePojo preference = (PreferencePojo) response.body();
                if (preference.getStatus().equalsIgnoreCase("Success")) {
                    List<Result> results = preference.getResult();
                    PrefrencesAdapter adapter = new PrefrencesAdapter(results, (position, task, bool, id) -> {
                        if (task.equalsIgnoreCase("edit")) {
                            Bundle bundle = new Bundle();
                            Fragment fragment = new CreateUpdatePreferences();
                            bundle.putString("Operation", "Edit");
                            bundle.putParcelable("details", results.get(position));
                            bundle.putBoolean("bool", !bool);
                            bundle.putInt("id", id);
                            fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack("edit").commit();
                        } else if (task.equalsIgnoreCase("delete")) {
                            deletePrefrences(Integer.valueOf(results.get(position).getId()));
                        }
                    }, mContext);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                    pref_recycler_view.setLayoutManager(mLayoutManager);
                    pref_recycler_view.setItemAnimator(new DefaultItemAnimator());
                    pref_recycler_view.setAdapter(adapter);
                } else {
                    GlobalClass.showErrorMsg(mContext, preference.getError());
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
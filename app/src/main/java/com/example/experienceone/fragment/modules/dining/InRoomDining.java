package com.example.experienceone.fragment.modules.dining;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.experienceone.R;
import com.example.experienceone.adapter.moduleadapters.dinningadapters.DinningGridViewAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.dinning.CategoryItem;
import com.example.experienceone.pojo.dinning.InRoomDinning;
import com.example.experienceone.pojo.dinning.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class InRoomDining extends Fragment implements ApiListener {

    private GridView dinning_gridview;
    private Context context;
    private List<Result> item;
    private ArrayList <CategoryItem> submenuitems;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_room_dining, container, false);
        context=view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("In-Room Dining");
        dinning_gridview=view.findViewById(R.id.dinning_gridview);
        loading=view.findViewById(R.id.loading);

        getDinningElements();
        return view;
    }

    private void getDinningElements() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<InRoomDinning> dinningCall = api.getDinningElemnts(headerMap);
        APIResponse.getCallRetrofit(dinningCall, "Dinning", context, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("Dinning")) {
                InRoomDinning dinning = (InRoomDinning) response.body();
                if (dinning.getStatus().equalsIgnoreCase("Success")) {
                    item =  dinning.getResult();
                    DinningGridViewAdapter adapter = new DinningGridViewAdapter(context, item, position -> {
                        Bundle bundle = new Bundle();
                        submenuitems=new ArrayList<>();
                        Fragment fragment = new DiningMenuList();
                        submenuitems.addAll(item.get(position).getCategoryItem());
                        bundle.putParcelableArrayList("OutletMenus",submenuitems);
                        bundle.putString("SubMenuName",item.get(position).getMenuName());
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                    });
                    dinning_gridview.setAdapter(adapter);
                } else {
                    GlobalClass.showErrorMsg(context, dinning.getError());
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
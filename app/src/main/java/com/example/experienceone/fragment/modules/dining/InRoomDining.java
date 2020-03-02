package com.example.experienceone.fragment.modules.dining;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experienceone.R;
import com.example.experienceone.adapter.moduleadapters.dinningadapters.DinningGridViewAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.dinningmodel.DinningSegmentModel;
import com.example.experienceone.pojo.dinning.CategoryItem;
import com.example.experienceone.pojo.dinning.InRoomDinning;
import com.example.experienceone.pojo.dinning.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.example.experienceone.services.FragmentCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class InRoomDining extends Fragment implements ApiListener, FragmentCallback {

    private GridView dinning_gridview;
    private Context context;
    private List<Result> item;
    private ArrayList<CategoryItem> submenuitems;
    private ProgressBar loading;
    private ArrayList<CategoryItem> menuItems = new ArrayList<>();
    private DinningSegmentModel dinningSegmentModel;
    private DiningModuleSegment diningModuleSegment;
    private Double prices=0.0;
    private Integer count=0;
    private DiningMenuList moduleSegment;
    private Boolean isForward = false;
    private Bundle bundle;
    private TextView item_count,tv_item_price,tv_view_order;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in_room_dining, container, false);
        context = view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        LinearLayout bottom_view = view.findViewById(R.id.bottom_view);
        item_count = view.findViewById(R.id.item_count);
        tv_item_price = view.findViewById(R.id.tv_item_price);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        tv_view_order=view.findViewById(R.id.tv_view_order);
        toolbar_title.setText("In-Room Dining");
        dinning_gridview = view.findViewById(R.id.dinning_gridview);
        loading = view.findViewById(R.id.loading);

        if (!isForward) {
            dinningSegmentModel = new DinningSegmentModel();
            diningModuleSegment=new DiningModuleSegment();
            moduleSegment = new DiningMenuList();
            moduleSegment.setFragmentCallback(this);
            diningModuleSegment.setmInRoomCallBack(this);
            submenuitems = new ArrayList<>();
            for (int i = 0; i < submenuitems.size(); i++) {
                submenuitems.get(i).setQuantity(0);
            }

           /* bottom_view.setVisibility(View.GONE);*/
            getDinningElements();
        } else {
            setAdapterView();
            GlobalClass.scaleView(bottom_view,0,1);
            item_count.setText(count + " items");
            tv_item_price.setText(prices + " Rs");
        }
        if (dinningSegmentModel.getDetails() != null && dinningSegmentModel.getDetails().size() > 0) {
            bottom_view.setVisibility(View.VISIBLE);
        }else{
            bottom_view.setVisibility(View.GONE);
        }
        tv_view_order.setOnClickListener(v->{
            if (dinningSegmentModel.getDetails() != null && dinningSegmentModel.getDetails().size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("DinningModel", dinningSegmentModel);
                menuItems.addAll(this.dinningSegmentModel.getDetails());
                bundle.putParcelableArrayList("category", menuItems);
                bundle.putString("item_count", item_count.getText().toString());
                bundle.putString("item_price", tv_item_price.getText().toString());
                diningModuleSegment.setArguments(bundle);
                isForward = true;
                GlobalClass.mISVisible=false;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, diningModuleSegment).addToBackStack(null).commit();
            } else {
                GlobalClass.ShowAlet(context, "Alert", "please select at least one item");
            }
        });


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
                    item = dinning.getResult();
                    bundle = new Bundle();

                    setAdapterView();
                } else {
                    GlobalClass.showErrorMsg(context, dinning.getError());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapterView() {
        DinningGridViewAdapter adapter = new DinningGridViewAdapter(context, item, position -> {
            submenuitems.clear();
            submenuitems.addAll(item.get(position).getCategoryItem());
            bundle.putParcelableArrayList("OutletMenus", submenuitems);
            bundle.putString("SubMenuName", item.get(position).getMenuName());
            bundle.putParcelable("DinningModel", dinningSegmentModel);
            bundle.putString("item_count", String.valueOf(count));
            bundle.putString("item_price", String.valueOf(prices));
            bundle.putParcelable("diningModuleSegment",diningModuleSegment);

           /* if (!isForward) {

            }*/
            isForward = true;
            moduleSegment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, moduleSegment).addToBackStack(null).commit();
        });
        dinning_gridview.setAdapter(adapter);
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
    public void onDataSent(Double price, Integer count, DinningSegmentModel dinningSegmentModel) {

        prices = price;
        this.count = count;
        this.dinningSegmentModel = new DinningSegmentModel();
        this.dinningSegmentModel.setDetails(dinningSegmentModel.getDetails());

    }

}
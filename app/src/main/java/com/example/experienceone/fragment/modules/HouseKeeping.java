package com.example.experienceone.fragment.modules;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;
import com.example.experienceone.adapter.moduleadapters.housekeeping.HouseKeepingAdapter;
import com.example.experienceone.adapter.pagination.HouseKeepingViewModel;
import com.example.experienceone.adapter.pagination.MyViewModelFactory;
import com.example.experienceone.fragment.general.MultipleRoomDialougFragment;
import com.example.experienceone.fragment.general.moduleSegment;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.raisingticketmodel.ModuleSegmentModel;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.HouseKeeping.Result;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HouseKeeping extends Fragment {


    private RecyclerView house_keeping_recycler;
    private Context context;
    private ModuleSegmentModel houseKeepingModel;
    private ArrayList<CategoryItem> details=new ArrayList<>();
    private ArrayList<Result> results=new ArrayList<>();
    private  HouseKeepingViewModel itemViewModel;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_keeping, container, false);
        try {
            context = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            TextView tv_bottom = view.findViewById(R.id.tv_bottom);
            house_keeping_recycler = view.findViewById(R.id.house_keeping_recycler);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            toolbar_title.setText("HouseKeeping");
            loading = view.findViewById(R.id.loading);

            loading.setVisibility(View.VISIBLE);
            itemViewModel = ViewModelProviders.of(this, new MyViewModelFactory(getActivity().getApplication(), "guest-housekeeping-category/")).get(HouseKeepingViewModel.class);

            houseKeepingModel = new ModuleSegmentModel();
            // getHosueKeepingList();
            tv_bottom.setOnClickListener(v -> {
                try {
                    houseKeepingModel.setTitle("Housekeeping");
                    houseKeepingModel.setBooking(GlobalClass.Booking_id);
                    List<CategoryItem> categoryItems = GlobalClass.removeDuplicates(details);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        categoryItems = categoryItems.stream().filter(p -> Integer.parseInt(p.getQuantity()) != 0).collect(Collectors.toList());
                    } else {
                        for (int i = 0; i <
                                categoryItems.size(); i++) {
                            if (Integer.parseInt(categoryItems.get(i).getQuantity()) == 0) {
                                categoryItems.remove(categoryItems.get(i));
                            }
                        }
                    }
                    if(categoryItems.size()>0){
                        houseKeepingModel.setDetails(categoryItems);
                        Bundle bundle = new Bundle();
                        Fragment fragment = new moduleSegment();
                        bundle.putString("url", "housekeeping-book-ticket/");
                        bundle.putString("type", "");
                        bundle.putString("toolbar_title","Housekeeping");
                        bundle.putParcelable("subcategory", houseKeepingModel);
                        bundle.putParcelableArrayList("category", results);
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                    }else{
                        GlobalClass.ShowAlet(context,"Alert","please select at least one item");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            itemViewModel.getPagedListLiveData().observe(this, items -> {
                loading.setVisibility(View.GONE);
                results.addAll(items);
                showonRecyclerView(items);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private void showonRecyclerView(PagedList<Result> items) {
        try {
            final HouseKeepingAdapter adapter = new HouseKeepingAdapter(context, details1 -> {
                details.add(details1);
            });
            house_keeping_recycler.setLayoutManager(new LinearLayoutManager(context));
            house_keeping_recycler.setHasFixedSize(true);
            house_keeping_recycler.setAdapter(adapter);

            adapter.submitList(items);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*private void getHosueKeepingList() {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<HouseKeepingPojo> houseKeeping = api.getHouseKeeping(url, headerMap);
        APIResponse.callRetrofit(houseKeeping, "houseKeeping", context, this);
    }


    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            if (apiCallName.equalsIgnoreCase("houseKeeping")) {
                HouseKeepingPojo houseKeepingPojo = (HouseKeepingPojo) response.body();
                if (houseKeepingPojo.getStatus().equalsIgnoreCase("Success")) {

                    List<ObjectListner> mObjectlistner = new ArrayList<>();
                    results=new ArrayList<>();
                    houseKeepingModel=new ModuleSegmentModel();

                    details=new ArrayList<>();
                    for (int i = 0; i < houseKeepingPojo.getResult().size(); i++) {
                        results.add(houseKeepingPojo.getResult().get(i));
                        mObjectlistner.add(houseKeepingPojo.getResult().get(i));
                        mObjectlistner.addAll(houseKeepingPojo.getResult().get(i).getCategoryItem());
                    }
                    ServiceModuleAdapter adapter = new ServiceModuleAdapter(mObjectlistner, mObjectListenr -> {
                        CategoryItem item = (CategoryItem) mObjectListenr;
                        details.add(item);
                    });
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    house_keeping_recycler.setLayoutManager(mLayoutManager);
                    house_keeping_recycler.setNestedScrollingEnabled(false);
                    house_keeping_recycler.setItemAnimator(new DefaultItemAnimator());
                    house_keeping_recycler.setAdapter(adapter);
                } else {
                    GlobalClass.showErrorMsg(context, houseKeepingPojo.getError());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();
        itemViewModel.getPagedListLiveData().observe(this, items ->{
            results.addAll(items);
            results= GlobalClass.removeDuplicates(results);
        });
    }
}
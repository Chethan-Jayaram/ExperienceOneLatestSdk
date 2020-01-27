package com.example.experienceone.fragment.modules;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.adapter.generaladapters.ServiceModuleAdapter;
import com.example.experienceone.adapter.moduleadapters.housekeeping.HouseKeepingAdapter;
import com.example.experienceone.adapter.pagination.HouseKeepingViewModel;
import com.example.experienceone.adapter.pagination.MyViewModelFactory;
import com.example.experienceone.fragment.general.moduleSegment;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.services.ObjectListner;
import com.example.experienceone.model.raisingticketmodel.ModuleSegmentModel;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.HouseKeeping.HouseKeepingPojo;
import com.example.experienceone.pojo.HouseKeeping.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;

public class Laundry extends Fragment {
    private RecyclerView laundry_recycler;
    private Context context;
    private ArrayList<Result> results=new ArrayList<>();
    private ModuleSegmentModel houseKeepingModel;
    private ArrayList<CategoryItem> details=new ArrayList<>();
    private HouseKeepingViewModel itemViewModel;
    private ProgressBar laun_loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laundry, container, false);
        try {
            context = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            TextView tv_bottom = view.findViewById(R.id.tv_bottom);
            laundry_recycler = view.findViewById(R.id.laundry_recycler);
            laun_loading = view.findViewById(R.id.loading);
            laun_loading.setVisibility(View.VISIBLE);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            toolbar_title.setText("Laundry");
            itemViewModel = ViewModelProviders.of(this, new MyViewModelFactory(getActivity().getApplication(), "guest-laundry-category/")).get(HouseKeepingViewModel.class);

            houseKeepingModel = new ModuleSegmentModel();
            tv_bottom.setOnClickListener(v -> {
                try {
                    houseKeepingModel.setTitle("Laundry");
                    houseKeepingModel.setBooking(GlobalClass.Booking_id);
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
                    if(categoryItems.size()>0){
                        houseKeepingModel.setDetails(categoryItems);
                        Bundle bundle = new Bundle();
                        Fragment fragment = new moduleSegment();
                        bundle.putString("url", "laundry-book-ticket/");
                        bundle.putString("type", "");
                        bundle.putString("toolbar_title","Laundry");
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
                results.addAll(items);
                showonRecyclerView(items);
                laun_loading.setVisibility(View.GONE);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private void showonRecyclerView(PagedList<Result> items) {
        final HouseKeepingAdapter adapter = new HouseKeepingAdapter(context, details1-> {
            details.add(details1);
        });
        laundry_recycler.setLayoutManager(new LinearLayoutManager(context));
        laundry_recycler.setHasFixedSize(true);
        laundry_recycler.setAdapter(adapter);
        adapter.submitList(items);
    }
  /*  private void getHosueKeepingList() {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<HouseKeepingPojo> houseKeeping = api.getLaundry(headerMap);
        APIResponse.callRetrofit(houseKeeping, "laundry", context, this);
    }


    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            if (apiCallName.equalsIgnoreCase("laundry")) {
         HouseKeepingPojo houseKeepingPojo = (HouseKeepingPojo) response.body();
                if (houseKeepingPojo.getStatus().equalsIgnoreCase("Success")) {
                    mObjectlistner = new ArrayList<>();
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
                    laundry_recycler.setLayoutManager(mLayoutManager);
                    laundry_recycler.setNestedScrollingEnabled(false);
                    laundry_recycler.setItemAnimator(new DefaultItemAnimator());
                    laundry_recycler.setAdapter(adapter);
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

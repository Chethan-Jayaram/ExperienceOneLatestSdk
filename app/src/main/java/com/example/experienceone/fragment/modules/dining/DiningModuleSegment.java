package com.example.experienceone.fragment.modules.dining;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.adapter.moduleadapters.dinningadapters.DinningMenuItemAdapter;
import com.example.experienceone.fragment.general.MultipleRoomDialougFragment;
import com.example.experienceone.fragment.general.TicketDetails;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.dinningmodel.DinningSegmentModel;
import com.example.experienceone.pojo.dinning.CategoryItem;
import com.example.experienceone.pojo.posttickets.TicketID;
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

public class DiningModuleSegment extends Fragment implements ApiListener, Parcelable {

    private ArrayList<CategoryItem> menuItems = new ArrayList<>();
    private Context context;
    private DinningSegmentModel dinningSegmentModel;
    private FragmentCallback fragmentCallback,mInRoomCallBack;
    private Double price=0.0;
    private Integer count=0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinning_segment, container, false);
        try {
            context = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            TextView tv_view_order = view.findViewById(R.id.tv_view_order);
            RecyclerView house_keeping_segment_recycler = view.findViewById(R.id.house_keeping_segment_recycler);
            TextView item_count = view.findViewById(R.id.item_count);
            TextView tv_item_price = view.findViewById(R.id.tv_item_price);
            EditText et_special_inst = view.findViewById(R.id.et_special_inst);
            LinearLayout bottom_view = view.findViewById(R.id.bottom_view);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            toolbar_title.setText("Your Order");

            Bundle data = getArguments();
            dinningSegmentModel = data.getParcelable("DinningModel");
            dinningSegmentModel.setTitle("In-Room-Dinning");
            dinningSegmentModel.setBooking(GlobalClass.Booking_id);
            bottom_view.setVisibility(dinningSegmentModel.getDetails().size() > 0 ? View.VISIBLE : View.GONE);

            List<CategoryItem> details = dinningSegmentModel.getDetails();
            /*item_count.setText(data.getString("item_count"));*/
            item_count.setText(dinningSegmentModel.getDetails().size() + " items");

            menuItems.addAll(dinningSegmentModel.getDetails());

            tv_item_price.setText(data.getString("item_price"));
            this.price = Double.valueOf(data.getString("item_price").replace(" Rs", ""));
            this.count = Integer.valueOf(data.getString("item_count").replace(" items", ""));
/*
        DinningModuleSegmentAdapter adapter=new DinningModuleSegmentAdapter(details);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        house_keeping_segment_recycler.setLayoutManager(mLayoutManager);
        house_keeping_segment_recycler.setItemAnimator(new DefaultItemAnimator());
        house_keeping_segment_recycler.setAdapter(adapter);*/


            DinningMenuItemAdapter adapter = new DinningMenuItemAdapter(details, count, price, (categoryItem, price, pos) -> {
                try {
                    /*    dinningSegmentModel.getDetails().get(pos).setQuantity(counts);*/
                    menuItems.add(categoryItem);
                    dinningSegmentModel.setDetails(GlobalClass.removeDuplicateItems(menuItems));
                    item_count.setText(dinningSegmentModel.getDetails().size() + " items");
                    tv_item_price.setText(GlobalClass.decimalFormat.format(Math.abs(price)) + " Rs");
                    this.price = price;
                    this.count = dinningSegmentModel.getDetails().size();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            house_keeping_segment_recycler.setLayoutManager(mLayoutManager);
            house_keeping_segment_recycler.setNestedScrollingEnabled(false);
            house_keeping_segment_recycler.setItemAnimator(new DefaultItemAnimator());
            house_keeping_segment_recycler.setAdapter(adapter);

            tv_view_order.setOnClickListener(v -> {

          /*  List<CategoryItem> menu_item=dinningSegmentModel.getDetails();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                menu_item=  menu_item.stream().filter(p->p.getQuantity()!=0).collect(Collectors.toList());
            }else{
                for(int i=0;i<menu_item.size();i++){
                    if(menu_item.get(i).getQuantity()==0){
                        menu_item.remove(menu_item.get(i));
                    }
                }
            }*/
                if (dinningSegmentModel.getDetails() != null && dinningSegmentModel.getDetails().size() > 0) {
                    if (et_special_inst.getText().toString().length() < 200) {
                        dinningSegmentModel.setSpecialinstructions(et_special_inst.getText().toString());
                        // dinningSegmentModel.setDetails(menuItems);
                        if(GlobalClass.MY_ROOMS.size()==1){
                            dinningSegmentModel.setRoom_no(GlobalClass.MY_ROOMS.get(0).getRoom().getRoomNo());
                            postHosueKeepingList(dinningSegmentModel);
                        }else{
                            MultipleRoomDialougFragment bottom = new MultipleRoomDialougFragment();
                            data.putString("module","Dining");
                            bottom.setArguments(data);
                            bottom.show(getActivity().getSupportFragmentManager(),
                                    GlobalClass.BOTTOM_VIEW);
                        }
                    } else {
                        GlobalClass.ShowAlet(context, "Alert", "Instructions cannot be more than 200 character");
                    }
                } else {
                    GlobalClass.ShowAlet(context, "Alert", "please select at least one item");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    void setFragmentCallback(FragmentCallback callback) {
        this.fragmentCallback = callback;
    }


    void setmInRoomCallBack (FragmentCallback callBack){
        this.mInRoomCallBack=callBack;
    }
    private void postHosueKeepingList(DinningSegmentModel dinningModuleSegment) {

        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> TicketID = api.postDinningSegment(headerMap, dinningModuleSegment);
        APIResponse.postCallRetrofit(TicketID, "DinningSegment", context, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            if (apiCallName.equalsIgnoreCase("DinningSegment")) {
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")) {
                    Bundle bundle = new Bundle();
                    Fragment fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt", ticketDetails.getResult().getLayout());
                    bundle.putString("type", "");
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();

                } else {
                    GlobalClass.showErrorMsg(context, ticketDetails.getError());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        if (fragmentCallback != null) {
            fragmentCallback.onDataSent(this.price , this.count , dinningSegmentModel);
        }
        if(mInRoomCallBack!=null){
            mInRoomCallBack.onDataSent(this.price , this.count , dinningSegmentModel);
        }
        super.onDestroy();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
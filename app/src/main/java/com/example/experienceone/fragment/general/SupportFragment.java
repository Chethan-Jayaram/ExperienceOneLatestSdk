package com.example.experienceone.fragment.general;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.raisingticketmodel.ModuleSegmentModel;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.posttickets.TicketID;
import com.example.experienceone.pojo.support.Support;
import com.example.experienceone.pojo.ticketdetails.TicketDetailsPojo;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SupportFragment extends Fragment implements ApiListener {

    private Spinner support_spinner;
    private Context mContext;
    private List<String> SupportList;
    private HashMap<String,Integer> SupportMapping;
    private String mSelectedItem;
    private ModuleSegmentModel houseKeepingModel;
    private ArrayList<CategoryItem> details=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        try {
            mContext = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            EditText et_support = view.findViewById(R.id.et_support);
            Button btn_support = view.findViewById(R.id.btn_support);
            support_spinner = view.findViewById(R.id.support_spinner);
            toolbar_title.setText("Support Services");


            getSupportSpinnerList();


            support_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mSelectedItem = parent.getItemAtPosition(position).toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getMessage();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            btn_support.setOnClickListener(v -> {
                if (SupportMapping.get(mSelectedItem)!=0) {
                    if(!et_support.getText().toString().isEmpty()) {
                        houseKeepingModel = new ModuleSegmentModel();
                        CategoryItem categoryItem = new CategoryItem();
                        details.clear();
                        houseKeepingModel.setTitle("Support");
                        houseKeepingModel.setBooking(GlobalClass.Booking_id);
                        categoryItem.setTitle(mSelectedItem);
                        categoryItem.setId(SupportMapping.get(mSelectedItem));
                        categoryItem.setDescription(et_support.getText().toString());
                        details.add(categoryItem);
                        houseKeepingModel.setDetails(details);
                        if (GlobalClass.MY_ROOMS.size() == 1) {
                            houseKeepingModel.setRoom_no(GlobalClass.MY_ROOMS.get(0).getRoom().getRoomNo());
                            postSupport(houseKeepingModel);
                        } else {
                            MultipleRoomDialougFragment bottom = new MultipleRoomDialougFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("module", "Support");
                            bundle.putParcelable("subcategory", houseKeepingModel);
                            bottom.setArguments(bundle);
                            bottom.show(getActivity().getSupportFragmentManager(),
                                    GlobalClass.BOTTOM_VIEW);
                        }
                    }else{
                        GlobalClass.ShowAlet(mContext,"Message","Please enter vaild message..");
                    }
                }else{
                    GlobalClass.ShowAlet(mContext,"Message","Please Select at least one category..");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void postSupport(ModuleSegmentModel houseKeepingModel) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        HashMap headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> supportCall = api.postSupport(headerMap,houseKeepingModel);
        APIResponse.postCallRetrofit(supportCall, "postSupport", mContext, this);
    }

    private void getSupportSpinnerList() {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        HashMap headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<Support> supportCall = api.getSupport(headerMap);
        APIResponse.callBackgroundRetrofit(supportCall, "GetSupport", mContext, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            if (apiCallName.equalsIgnoreCase("GetSupport")) {
                Support support = (Support) response.body();
                if (support.getStatus().equalsIgnoreCase("Success")) {
                    SupportList = new ArrayList<>();
                    SupportMapping = new HashMap();
                    SupportList.add("Please select any one from the list");
                    SupportMapping.put(SupportList.get(0), 0);
                    for (int i = 0; i < support.getResult().size(); i++) {
                        SupportList.add(support.getResult().get(i).getTitle());
                        SupportMapping.put(support.getResult().get(i).getTitle(), support.getResult().get(i).getId());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, SupportList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    support_spinner.setAdapter(dataAdapter);
                } else {
                    GlobalClass.showErrorMsg(mContext, support.getError());
                }
            }else if(apiCallName.equalsIgnoreCase("postSupport")){
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")) {
                    Bundle bundle = new Bundle();
                    Fragment fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt", ticketDetails.getResult().getLayout());
                    bundle.putString("type", "Support");
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                }else {
                    GlobalClass.showErrorMsg(mContext,ticketDetails.getError());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {

    }
}

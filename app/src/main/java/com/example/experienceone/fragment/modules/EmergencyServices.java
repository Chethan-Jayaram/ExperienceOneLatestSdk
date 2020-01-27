package com.example.experienceone.fragment.modules;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.experienceone.R;
import com.example.experienceone.adapter.moduleadapters.EmergencyGridViewAdapter;
import com.example.experienceone.fragment.general.TicketDetails;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.raisingticketmodel.ModuleSegmentModel;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.emergency.Emergency;
import com.example.experienceone.pojo.emergency.Result;
import com.example.experienceone.pojo.posttickets.TicketID;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class EmergencyServices extends Fragment implements ApiListener {

    private GridView emergency_gridview;
    private List<Result> item;
    private Context context;
    private Integer Id;
    private String title;
    private ModuleSegmentModel houseKeepingModel;
    private ArrayList<CategoryItem> details=new ArrayList<>();
    private ProgressBar loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_services, container, false);
        context=view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Emergency Services");
        emergency_gridview=view.findViewById(R.id.emergency_gridview);
        TextView tv_emergency_btn = view.findViewById(R.id.tv_emergency_btn);
        Id=null;
        loading=view.findViewById(R.id.loading);
        getEmegencyDashBoardElements();


        tv_emergency_btn.setOnClickListener(v->{
            if(Id!=null) {
                houseKeepingModel = new ModuleSegmentModel();
                CategoryItem categoryItem = new CategoryItem();
                details.clear();
                houseKeepingModel.setTitle("Emergency Service");
                houseKeepingModel.setBooking(GlobalClass.Booking_id);
                categoryItem.setTitle(title);
                categoryItem.setId(Id);
                details.add(categoryItem);
                houseKeepingModel.setDetails(details);
                ShowAlet(context, "Please Confirm", "We are concerned about your saftey during our stay, please confirm by clicking OK or CANCEL");
            }else{
                GlobalClass.ShowAlet(context,"Alert","Please select an option to raise a ticket");
            }
        });

        return view;
    }


    private void getEmegencyDashBoardElements() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<Emergency> emergencyCall = api.getEmergencyDashBoardElemnts(headerMap);
        APIResponse.getCallRetrofit(emergencyCall, "emergencyCall", context, this);
    }

    private void postEmergencyRequest(ModuleSegmentModel houseKeepingModel) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> emergencyCall = api.postEmergencyRequst(headerMap,houseKeepingModel);
        APIResponse.postCallRetrofit(emergencyCall, "postemergency", context, this);
    }


    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("emergencyCall")) {
                Emergency emergency = (Emergency) response.body();
                if (emergency.getStatus().equalsIgnoreCase("Success")) {
                    item = emergency.getResult();
                    EmergencyGridViewAdapter adapter = new EmergencyGridViewAdapter(context, item, position -> {
                        Id=item.get(position).getId();
                        title=item.get(position).getTitle();
                    });
                    emergency_gridview.setAdapter(adapter);
                }
            }else if(apiCallName.equalsIgnoreCase("postemergency")){
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")){
                    Bundle bundle = new Bundle();
                    Fragment fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt",ticketDetails.getResult().getLayout());
                    bundle.putString("type","");
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                }else {
                    GlobalClass.showErrorMsg(context,ticketDetails.getError());
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

    private void ShowAlet(Context context, String title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        postEmergencyRequest(houseKeepingModel);
                    })
                    .setNegativeButton("CANCEL", (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
            Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setTextColor(context.getResources().getColor(R.color.black));
            negativeButton.setTextColor(context.getResources().getColor(R.color.black));
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }


}
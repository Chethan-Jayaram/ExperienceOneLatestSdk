package com.example.experienceone.fragment.general;

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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.experienceone.R;
import com.example.experienceone.adapter.generaladapters.ServiceModuleSegmentAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.helper.IdSorter;
import com.example.experienceone.model.raisingticketmodel.ModuleSegmentModel;
import com.example.experienceone.pojo.HouseKeeping.CategoryItem;
import com.example.experienceone.pojo.HouseKeeping.Result;

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

public class moduleSegment extends Fragment implements ApiListener {
    private Context context;
    private ModuleSegmentModel houseKeepingModel;
    private String url;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_keeping_segment, container, false);
        context = view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        TextView tv_confirm_button = view.findViewById(R.id.tv_confirm_button);
        RecyclerView house_keeping_segment_recycler = view.findViewById(R.id.house_keeping_segment_recycler);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);

        Bundle data =getArguments();
        url=data.getString("url");
        houseKeepingModel=data.getParcelable("subcategory");

        toolbar_title.setText(data.getString("toolbar_title"));
        tv_confirm_button.setVisibility(houseKeepingModel.getDetails().size()>0 ? View.VISIBLE: View.GONE );

        List<CategoryItem> details = houseKeepingModel.getDetails();
        ArrayList<Result> results = data.getParcelableArrayList("category");
        details.sort(new IdSorter());
        ServiceModuleSegmentAdapter adapter=new ServiceModuleSegmentAdapter(details, results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        house_keeping_segment_recycler.setLayoutManager(mLayoutManager);
        house_keeping_segment_recycler.setItemAnimator(new DefaultItemAnimator());
        house_keeping_segment_recycler.setAdapter(adapter);

        tv_confirm_button.setOnClickListener(v -> {
            postHosueKeepingList(houseKeepingModel);
        });
        return view;
    }

    private void postHosueKeepingList(ModuleSegmentModel houseKeepingModel) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> TicketID = api.postSegmentModule(url,headerMap,houseKeepingModel);
        APIResponse.postCallRetrofit(TicketID, "moduleSegment", context, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {

        try {
            if (apiCallName.equalsIgnoreCase("moduleSegment")) {
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")){
                    Bundle bundle = new Bundle();
                    Fragment fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt",ticketDetails.getResult().getLayout());
                    bundle.putString("type","");
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                }else {
                    GlobalClass.showErrorMsg(context,ticketDetails.getError());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {

    }
}
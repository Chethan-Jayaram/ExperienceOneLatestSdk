package com.example.experienceone.fragment.general;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.experienceone.adapter.ticketadapters.TicketListAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.helper.PaginationScrollListener;
import com.example.experienceone.pojo.ticketdetails.TicketDetailsPojo;
import com.example.experienceone.pojo.ticketdetails.TicketDetailsSocketPojo;
import com.example.experienceone.pojo.ticketlist.Result;
import com.example.experienceone.pojo.ticketlist.TicketList;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;

public class TicketsList extends Fragment implements ApiListener {

    private Context context;
    private WebSocketClient mWebSocketClient;
    private List<Result> mResult=new ArrayList<>();
    private TicketListAdapter adapter;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private Float TOTAL_PAGES;
    private int currentPage = 1;
    private String url="";
    private TicketList ticketList;
    private ProgressBar loading;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ticket_list, container, false);
        context = view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        RecyclerView ticket_recyler_list = view.findViewById(R.id.ticket_recyler_list);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Active Tickets");
        loading=view.findViewById(R.id.loading);

        adapter = new TicketListAdapter(position -> {
            Bundle bundle = new Bundle();
            Fragment fragment = new TicketDetails();
            bundle.putString("id", String.valueOf(ticketList.getResult().get(position).getId()));
            bundle.putString("lyt", ticketList.getResult().get(position).getLayout());
            bundle.putString("type", ticketList.getResult().get(position).getTitle());
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).commit();
        });

       LinearLayoutManager  linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        ticket_recyler_list.setLayoutManager(linearLayoutManager);

        ticket_recyler_list.setItemAnimator(new DefaultItemAnimator());

        ticket_recyler_list.setAdapter(adapter);

        ticket_recyler_list.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(() -> {
                    if(!url.isEmpty()){
                        addMoreItems(url);
                    }
                }, 1000);
            }

            @Override
            public Float getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        getTicketList();

        return view;
    }

    private void getTicketList() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketList> ticketDetailsPojoCall = api.getticketList(headerMap);
        APIResponse.getCallRetrofit(ticketDetailsPojoCall, "ticketList", context, this);
    }


    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
         ticketList = (TicketList) response.body();
        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("ticketList")) {
                if (ticketList.getStatus().equalsIgnoreCase("Success")) {
                    mResult = ticketList.getResult();
                    TOTAL_PAGES = ticketList.getCount()+0.0f;
                    url=ticketList.getNext()!=null?ticketList.getNext():"";
                    adapter.addAll(mResult);
                    if (!(currentPage <= TOTAL_PAGES)) {
                        isLastPage = true;
                    }
                } else {
                    GlobalClass.showErrorMsg(context, ticketList.getError());
                }
            }else if(apiCallName.equalsIgnoreCase("nextPage")) {
                if (ticketList.getStatus().equalsIgnoreCase("Success")) {
                    isLoading = false;
                    url=ticketList.getNext()!=null?ticketList.getNext():"";
                    mResult = ticketList.getResult();;
                    adapter.addAll(mResult);
                    if (currentPage == TOTAL_PAGES){
                        isLastPage = true;
                    }
                } else {
                    GlobalClass.showErrorMsg(context, ticketList.getError());
                }
            }
        }catch (Exception e) {
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

    private void addMoreItems(String url) {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketList> ticketDetailsNextCall = api.getticketNextPage(url,headerMap);
        APIResponse.getCallRetrofit(ticketDetailsNextCall, "nextPage", context, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            connectWebSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://176.9.127.214:8003/ws/bookticket/guest-ticket/"+GlobalClass.token+"/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.d("handshanke","sucessfull");
            }
            @Override
            public void onMessage(String message) {
                try {
                    getActivity().runOnUiThread(() -> {
                            Gson gson = new Gson();
                            TicketDetailsSocketPojo socketPojo = gson.fromJson(message, TicketDetailsSocketPojo.class);
                            for(int i=0;i<mResult.size();i++){
                                if (mResult.get(i).getId()==socketPojo.getMessage().getId()) {
                                    mResult.get(i).getCurrentStatus().getEventStyle().getTicketStatusPills().setBackground(socketPojo.getMessage().getCurrentStatus().getEventStyle().getTicketStatusPills().getBackground());
                                    mResult.get(i).getCurrentStatus().setName(socketPojo.getMessage().getCurrentStatus().getName());
                                }
                            }
                        adapter.notifyDataSetChanged();
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {

            }

            @Override
            public void onError(Exception e) {
                try {
                    connectWebSocket();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        };
        mWebSocketClient.connect();
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            mWebSocketClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

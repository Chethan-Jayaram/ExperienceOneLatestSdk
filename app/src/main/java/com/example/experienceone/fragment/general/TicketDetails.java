package com.example.experienceone.fragment.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.adapter.ticketadapters.TicketDetailsAdapter;
import com.example.experienceone.adapter.ticketadapters.TicketListAdapter;
import com.example.experienceone.adapter.ticketadapters.TicketStatusAdapter;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.ticketdetails.TicketDetailsPojo;
import com.example.experienceone.pojo.ticketdetails.TicketDetailsSocketPojo;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class TicketDetails extends Fragment implements ApiListener {

    private WebSocketClient mWebSocketClient;
    private Context context;
    private TextView tv_ticket_number, tv_ticket_status, tv_module_category,
            tv_guest_name, tv_guest_room_no, tv_quantity, tv_price;
    private RecyclerView tikcet_details_recylcer, ticket_status_adapter;
    private String lyt, type;
    private TicketStatusAdapter statusAdapter;
    private String id;
    private TextView toolbar_title, tv_special_instruction;
    private LinearLayout special_instruction_lyt;
    private ProgressBar loading;
    private RelativeLayout lyt_tkt_dtls;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ticket_layout, container, false);
        context = view.getContext();
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nav_menu).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_sos).setVisibility(View.GONE);

        toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        tv_ticket_number = view.findViewById(R.id.tv_ticket_number);
        tv_ticket_status = view.findViewById(R.id.tv_ticket_status);
        tv_module_category = view.findViewById(R.id.tv_module_category);
        tv_guest_name = view.findViewById(R.id.tv_guest_name);
        tv_guest_room_no = view.findViewById(R.id.tv_guest_room_no);
        tikcet_details_recylcer = view.findViewById(R.id.tikcet_details_recylcer);
        ticket_status_adapter = view.findViewById(R.id.ticket_status_adapter);
        tv_price = view.findViewById(R.id.tv_price);
        tv_quantity = view.findViewById(R.id.tv_quantity);
        special_instruction_lyt = view.findViewById(R.id.special_instruction_lyt);
        tv_special_instruction = view.findViewById(R.id.tv_special_instruction);
        lyt_tkt_dtls= view.findViewById(R.id.lyt_tkt_dtls);
        loading=view.findViewById(R.id.loading);
        lyt_tkt_dtls.setVisibility(View.INVISIBLE);

        special_instruction_lyt.setVisibility(View.GONE);
        Bundle data = getArguments();
        id = data.getString("id");
        lyt = data.getString("lyt");
        type = data.getString("type");
        layoutDecider(lyt);
        getTicketDetails(id);
        return view;
    }

    private void layoutDecider(String lyt) {
        switch (lyt) {
            case "TBL-PO":
                tv_quantity.setVisibility(View.GONE);
                break;
            case "TBL-QO":
                tv_price.setVisibility(View.GONE);
                break;
            case "TBL-IO":
                tv_price.setVisibility(View.GONE);
                tv_quantity.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void getTicketDetails(String id) {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketDetailsPojo> ticketDetailsPojoCall = api.getticketDetails(headerMap, id);
        APIResponse.getCallRetrofit(ticketDetailsPojoCall, "ticketDetails", context, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            lyt_tkt_dtls.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("ticketDetails")) {
                TicketDetailsPojo ticketDetails = (TicketDetailsPojo) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")) {
                    setUiComponents(ticketDetails);
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
        try{
            loading.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUiComponents(TicketDetailsPojo ticketDetails) {
        String ticket_no = " #" + ticketDetails.getResult().getTicketNumber();
        toolbar_title.setText("Ticket Details" + ticket_no);
        tv_ticket_number.setText(ticket_no);
        tv_ticket_status.setText(ticketDetails.getResult().getCurrentStatus().getName());
        GradientDrawable drawable = (GradientDrawable) tv_ticket_status.getBackground();
        drawable.setColor(Color.parseColor(ticketDetails.getResult().getCurrentStatus().getEventStyle().getTicketStatusPills().getBackground()));
        tv_module_category.setText("> " + ticketDetails.getResult().getTitle());
        tv_guest_room_no.setText(ticketDetails.getResult().getBooking().getRoom());
        tv_guest_name.setText(ticketDetails.getResult().getBooking().getGuest().getFirstName() +
                " " + ticketDetails.getResult().getBooking().getGuest().getLastName());
        if (!ticketDetails.getResult().getSpecial_instructions().isEmpty()) {
            special_instruction_lyt.setVisibility(View.VISIBLE);
            tv_special_instruction.setText(ticketDetails.getResult().getSpecial_instructions());
        }
        TicketDetailsAdapter adapter = new TicketDetailsAdapter(ticketDetails.getResult().getDetails(), lyt, type);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        tikcet_details_recylcer.setLayoutManager(mLayoutManager);
        tikcet_details_recylcer.setItemAnimator(new DefaultItemAnimator());
        tikcet_details_recylcer.setAdapter(adapter);

        statusAdapter = new TicketStatusAdapter(ticketDetails.getResult().getTicketActivity());
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(context);
        ticket_status_adapter.setLayoutManager(LayoutManager);
        ticket_status_adapter.setItemAnimator(new DefaultItemAnimator());
        ticket_status_adapter.setAdapter(statusAdapter);
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
            uri = new URI("ws://176.9.127.214:8003/ws/bookticket/guest-ticket/" + GlobalClass.token + "/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {

            }

            @Override
            public void onMessage(String message) {
                try {
                    getActivity().runOnUiThread(() -> {
                        Gson gson = new Gson();
                        TicketDetailsSocketPojo socketPojo = gson.fromJson(message, TicketDetailsSocketPojo.class);
                        if (id.equalsIgnoreCase(String.valueOf(socketPojo.getMessage().getId()))) {
                            tv_ticket_status.setText(socketPojo.getMessage().getCurrentStatus().getName());
                            tv_ticket_status.setBackgroundColor(Color.parseColor(socketPojo.getMessage().getCurrentStatus().getEventStyle().getTicketStatusPills().getBackground()));
                            statusAdapter = new TicketStatusAdapter(socketPojo.getMessage().getTicketActivity());
                            ticket_status_adapter.setAdapter(statusAdapter);
                        }
                    });
                } catch (Exception e) {
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
                } catch (Exception ex) {
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

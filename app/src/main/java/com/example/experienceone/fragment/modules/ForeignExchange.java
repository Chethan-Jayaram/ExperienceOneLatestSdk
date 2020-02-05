package com.example.experienceone.fragment.modules;


import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.experienceone.R;
import com.example.experienceone.adapter.moduleadapters.ForeignExchangeAdapter;
import com.example.experienceone.fragment.general.TicketDetails;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.model.foreignexchange.ForeignExchangemodel;
import com.example.experienceone.pojo.currencyexchnage.CurrencyExchange;
import com.example.experienceone.pojo.foreignexchange.ForeignExchangePojo;
import com.example.experienceone.pojo.foreignexchange.Result;
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

public class ForeignExchange extends Fragment implements ApiListener {


    private EditText et_amount, et_from_currency, et_to_currency;
    private TextView tv_from_to;
    private TextView tv_updated_date;
    private TextView exchange_result;
    private TextView tv_from_standard, tv_to_standard;
    private Context context;
    private List<Result> mFromList, mToList;
    private Result mFromresult, mToResult;
    private ForeignExchangemodel exchangemodel;
    private CurrencyExchange currencyExchange;
    private Boolean isCurrencyConverted;
    private Integer mFromId,mToID;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foreign_exchange, container, false);
        try {
            context = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            et_amount = view.findViewById(R.id.et_amount);
            et_from_currency = view.findViewById(R.id.et_from_currency);
            et_to_currency = view.findViewById(R.id.et_to_currency);
            tv_from_to = view.findViewById(R.id.tv_from_to);
            tv_updated_date = view.findViewById(R.id.tv_updated_date);
            exchange_result = view.findViewById(R.id.exchange_result);
            tv_from_standard = view.findViewById(R.id.tv_from_standard);
            tv_to_standard = view.findViewById(R.id.tv_to_standard);
            ImageView img_swap_category = view.findViewById(R.id.img_swap_category);
            TextView btn_convert = view.findViewById(R.id.btn_convert);
            TextView btn_exchange = view.findViewById(R.id.btn_exchange);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            loading = view.findViewById(R.id.loading);
            toolbar_title.setText("Foreign Exchange");
            isCurrencyConverted = false;
            getExcahngeItems();
            btn_convert.setOnClickListener(v -> {
                try {
                    if (!et_amount.getText().toString().isEmpty()) {
                        convertCurrency();
                    } else {
                        GlobalClass.ShowAlet(context, "Alert", "Amount Cannot be empty");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            et_to_currency.setOnClickListener(v -> {
                showCustomDialog(mToList, false);
            });
            et_from_currency.setOnClickListener(v -> {
                showCustomDialog(mFromList, true);
            });
            btn_exchange.setOnClickListener(v -> {
                if (isCurrencyConverted) {
                    prepareModelClass(currencyExchange);
                    postforeignExchnage(exchangemodel);
                } else {
                    GlobalClass.ShowAlet(context, "Alert", "Convert currecny before raising ticket");
                }
            });

            et_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    restForeignExchnage();
                }
            });
            img_swap_category.setOnClickListener(v -> {
                restForeignExchnage();

                mFromId=mToResult.getId();
                mToID=mFromresult.getId();
                for(int i=0;i<mFromList.size();i++){
                    if(mFromId.equals(mFromList.get(i).getId())){
                        mFromresult=mFromList.get(i);
                        et_from_currency.setText(mFromresult.getName());
                        getExcahngeItemsbyId(mFromresult.getId());
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void restForeignExchnage() {
        isCurrencyConverted = false;
        exchangemodel = null;
        tv_from_to.setText("");
        tv_updated_date.setText("");
        exchange_result.setText("");
        tv_from_standard.setText("");
        tv_to_standard.setText("");
    }

    private void getExcahngeItemsbyId(Integer id) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<ForeignExchangePojo> curencyExchange = api.getexchangeById(headerMap, id);
        APIResponse.postCallRetrofit(curencyExchange, "currencyExchangeById", context, this);
    }

    private void getExcahngeItems() {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<ForeignExchangePojo> curencyExchange = api.getexchange(headerMap);
        APIResponse.callBackgroundRetrofit(curencyExchange, "currencyExchange", context, this);
    }

    private void convertCurrency() {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Map<String, String> body = new HashMap<>();
        body.put("from_currency", String.valueOf(mFromresult.getId()));
        body.put("to_currency", String.valueOf(mToResult.getId()));
        body.put("amount", et_amount.getText().toString());
        Call<CurrencyExchange> currencyExchange = api.postForeignExchnage(headerMap, body);
        APIResponse.postCallRetrofit(currencyExchange, "postConvertCurrency", context, this);
    }

    private void postforeignExchnage(ForeignExchangemodel exchangemodel) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<TicketID> TicketID = api.postforeignExchange("foreign-exchange-book-ticket/", headerMap, exchangemodel);
        APIResponse.postCallRetrofit(TicketID, "foreignExchange", context, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("currencyExchange")) {
                ForeignExchangePojo foreignExchangePojo = (ForeignExchangePojo) response.body();
                if (foreignExchangePojo.getStatus().equalsIgnoreCase("Success")) {
                    mFromList = new ArrayList<>();
                    mFromList = foreignExchangePojo.getResult();
                    mFromresult = mFromList.get(0);
                    et_from_currency.setText(mFromresult.getName());
                    getExcahngeItemsbyId(mFromresult.getId());
                } else {
                    GlobalClass.showErrorMsg(context, foreignExchangePojo.getError());
                }
            } else if (apiCallName.equalsIgnoreCase("currencyExchangeById")) {
                ForeignExchangePojo foreignExchangePojo = (ForeignExchangePojo) response.body();
                if (foreignExchangePojo.getStatus().equalsIgnoreCase("Success")) {
                    mToList = new ArrayList<>();
                    mToList = foreignExchangePojo.getResult();
                    if(mToID==null){
                        mToResult = mToList.get(0);
                    }else{
                        for(int i=0;i<mToList.size();i++){
                            if(mToID!=null && mToID.equals(mToList.get(i).getId())){
                                mToID=null;
                                mToResult=mToList.get(i);
                                break;
                            }
                        }
                    }
                    et_to_currency.setText(mToResult.getName());
                } else {
                    GlobalClass.showErrorMsg(context, foreignExchangePojo.getError());
                }
            } else if (apiCallName.equalsIgnoreCase("postConvertCurrency")) {
                currencyExchange = (CurrencyExchange) response.body();
                if (currencyExchange.getStatus().equalsIgnoreCase("Success")) {
                    isCurrencyConverted = true;
                    tv_from_to.setText(et_from_currency.getText().toString() + " " + mFromresult.getSymbol() + " to " + et_to_currency.getText().toString() + " " + mToResult.getSymbol());
                    tv_updated_date.setText("Last updated: " + GlobalClass.outputdateformat.format(GlobalClass.inputdateformat.parse(currencyExchange.getResult().getLastActivityOn())));
                    exchange_result.setText(et_amount.getText().toString() + " " + mFromresult.getSymbol() + " = " + currencyExchange.getResult().getTotalAmount() + " " + mToResult.getSymbol());
                    tv_from_standard.setText("1 " + mFromresult.getSymbol() + " = " + currencyExchange.getResult().getExchangeRate() + " " + mToResult.getSymbol());
                    tv_to_standard.setText("1 " + mToResult.getSymbol() + " = " + currencyExchange.getResult().getCommissionRate() + " " + mFromresult.getSymbol());
                } else {
                    GlobalClass.showErrorMsg(context, currencyExchange.getError());
                }
            } else if (apiCallName.equalsIgnoreCase("foreignExchange")) {
                TicketID ticketDetails = (TicketID) response.body();
                if (ticketDetails.getStatus().equalsIgnoreCase("Success")) {
                    Bundle bundle = new Bundle();
                    Fragment fragment = new TicketDetails();
                    bundle.putString("id", String.valueOf(ticketDetails.getResult().getId()));
                    bundle.putString("lyt", ticketDetails.getResult().getLayout());
                    bundle.putString("type", "Foreign Exchange");
                    fragment.setArguments(bundle);
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

    private void prepareModelClass(CurrencyExchange currencyExchange) {
        exchangemodel = new ForeignExchangemodel();
        exchangemodel.setBooking(GlobalClass.Booking_id);
        exchangemodel.setTitle("Foreign Exchange");

        ArrayList<Map> details = new ArrayList<>();

        Map<String, String> d1 = new HashMap<>();
        d1.put("label", "From");
        d1.put("title", et_from_currency.getText().toString());
        d1.put("curency_symbol", mFromresult.getSymbol());
        d1.put("price", et_amount.getText().toString());
        details.add(d1);

        Map<String, String> d2 = new HashMap<>();
        d2.put("label", "To");
        d2.put("title", et_to_currency.getText().toString());
        d2.put("curency_symbol", mToResult.getSymbol());
        d2.put("price", String.valueOf(currencyExchange.getResult().getConversionAmount()));
        details.add(d2);


        Map<String, String> d3 = new HashMap<>();
        d3.put("title", "Commission Earned");
        d3.put("curency_symbol", mToResult.getSymbol());
        d3.put("price", String.valueOf(currencyExchange.getResult().getCommissionRate()));
        details.add(d3);


        Map<String, String> d4 = new HashMap<>();
        d4.put("title", "Net Payable Amount");
        d4.put("curency_symbol", mToResult.getSymbol());
        d4.put("price", String.valueOf(currencyExchange.getResult().getTotalAmount()));
        details.add(d4);

        exchangemodel.setDetails(details);
    }


    private void showCustomDialog(List<Result> result, Boolean flag) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.exchange_dialoug, viewGroup, false);
        RecyclerView foreign_exchange_recycler = dialogView.findViewById(R.id.foreign_exchange_recycler);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        ForeignExchangeAdapter adapter = new ForeignExchangeAdapter(result, position -> {
            if (flag) {
                et_from_currency.setText(result.get(position).getName());
                mFromresult = result.get(position);
                getExcahngeItemsbyId(mFromresult.getId());
            } else {
                et_to_currency.setText(result.get(position).getName());
                mToResult = result.get(position);
            }
            alertDialog.dismiss();
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        foreign_exchange_recycler.setLayoutManager(mLayoutManager);
        foreign_exchange_recycler.setNestedScrollingEnabled(false);
        foreign_exchange_recycler.setItemAnimator(new DefaultItemAnimator());
        foreign_exchange_recycler.setAdapter(adapter);

        //finally creating the alert dialog and displaying it

        alertDialog.show();
    }


}

package com.example.experienceone.helper;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.experienceone.R;
import com.example.experienceone.services.ApiListener;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIResponse {
    public static <ResponseType> void getCallRetrofit(Call<ResponseType> call, String apiCallName, Context context, final ApiListener apiListener) {
        call.enqueue(new Callback<ResponseType>() {
            @Override
            public void onResponse(Call<ResponseType> call, Response<ResponseType> response) {
                try {
                    if (response.isSuccessful()) {
                        apiListener.success(response, apiCallName);
                    } else {
                        apiListener.onErrorListner();
                        CustomMessageHelper showDialog = new CustomMessageHelper(context);
                        showDialog.showCustomMessage((Activity) context, "Alert!!", response.errorBody().toString(), false, false);
                    }
                } catch (Exception e) {
                    apiListener.onErrorListner();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseType> call, Throwable t) {
                try {
                    if (t instanceof SocketTimeoutException) {
                        apiListener.onErrorListner();
                        CustomMessageHelper showDialog = new CustomMessageHelper(context);
                        showDialog.showCustomMessage((Activity) context, "Alert!!", context.getString(R.string.SOCKET_ISSUE), false, false);
                    } else {
                        apiListener.onErrorListner();
                        CustomMessageHelper showDialog = new CustomMessageHelper(context);
                        showDialog.showCustomMessage((Activity) context, "Alert!!", context.getString(R.string.NETWORK_ISSUE), false, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static <ResponseType> void postCallRetrofit(Call<ResponseType> call, String apiCallName, Context context, final ApiListener apiListener) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        call.enqueue(new Callback<ResponseType>() {
            @Override
            public void onResponse(Call<ResponseType> call, Response<ResponseType> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        apiListener.success(response, apiCallName);
                    } else {
                        progressDialog.dismiss();
                        CustomMessageHelper showDialog = new CustomMessageHelper(context);
                        showDialog.showCustomMessage((Activity) context, "Alert!!", response.raw().toString(), false, false);
                    }
                } catch (Exception e) {
                    Log.d(apiCallName,e.getMessage());
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseType> call, Throwable t) {
                try {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        CustomMessageHelper showDialog = new CustomMessageHelper(context);
                        showDialog.showCustomMessage((Activity) context, "Alert!!", context.getString(R.string.SOCKET_ISSUE), false, false);
                    } else {
                        progressDialog.dismiss();
                        CustomMessageHelper showDialog = new CustomMessageHelper(context);
                        showDialog.showCustomMessage((Activity) context, "Alert!!", context.getString(R.string.NETWORK_ISSUE), false, false);
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }


    public static <ResponseType> void callBackgroundRetrofit(Call<ResponseType> call, String apiCallName, Context context, final ApiListener apiListener) {
        call.enqueue(new Callback<ResponseType>() {
            @Override
            public void onResponse(Call<ResponseType> call, Response<ResponseType> response) {
                if (response.isSuccessful()) {
                    try {
                        apiListener.success(response, apiCallName);
                    } catch (Exception e) {
                        apiListener.onErrorListner();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseType> call, Throwable t) {
                try {
                    apiListener.onErrorListner();
                    CustomMessageHelper showDialog = new CustomMessageHelper(context);
                    showDialog.showCustomMessage((Activity) context, "Alert!!", context.getString(R.string.SOCKET_ISSUE), false, false);
                } catch (Exception e) {
                    apiListener.onErrorListner();
                    e.printStackTrace();
                }
            }
        });
    }


}

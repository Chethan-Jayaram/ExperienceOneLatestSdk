package com.taj.doorunlock.helper;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.taj.doorunlock.R;
import com.taj.doorunlock.services.ApiListener;

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
                        GlobalClass.ShowAlet(context,"Alert",context.getString(R.string.SOCKET_ISSUE));
                    } else  {
                        apiListener.onErrorListner();
                        GlobalClass.ShowAlet(context,"Alert","Something went wrong please try again later");
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
                        apiListener.onErrorListner();


                    }
                } catch (Exception e) {
                    Log.d(apiCallName, e.getMessage());
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseType> call, Throwable t) {
                try {
                    if (t instanceof SocketTimeoutException) {
                        progressDialog.dismiss();
                        GlobalClass.ShowAlet(context,"Alert",context.getString(R.string.SOCKET_ISSUE));
                    } else if (t instanceof JsonSyntaxException) {
                        apiListener.onErrorListner();
                        GlobalClass.ShowAlet(context,"Alert","Something went wrong please try again later");
                    } else {
                        t.printStackTrace();
                        Log.d("api failed",t.getMessage());
                        progressDialog.dismiss();
                       // GlobalClass.ShowAlet(context,"Alert",t.getMessage());
                        GlobalClass.ShowAlet(context,"Alert",context.getString(R.string.NETWORK_ISSUE));
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
                    apiListener.success(response, apiCallName);
                } else {
                    apiListener.onErrorListner();


                }
            }

            @Override
            public void onFailure(Call<ResponseType> call, Throwable t) {
                try {


                    if (t instanceof SocketTimeoutException) {
                        apiListener.onErrorListner();
                        GlobalClass.ShowAlet(context,"Alert",context.getString(R.string.SOCKET_ISSUE));
                    } else if (t instanceof JsonSyntaxException) {
                        apiListener.onErrorListner();
                        GlobalClass.ShowAlet(context,"Alert","Something went wrong please try again later");
                    } else {

                        apiListener.onErrorListner();
                        GlobalClass.ShowAlet(context,"Alert",context.getString(R.string.NETWORK_ISSUE));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}

package com.example.experienceone.fragment.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.CustomMessageHelper;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.userauthentication.AuthenticateMobile;

import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


public class UserAuthenticationFragment extends Fragment implements ApiListener {
    private Context context;
    private ProgressBar loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_auth, container, false);
        try {
            context = view.getContext();
            loading=view.findViewById(R.id.loading);
            EditText et_phone_no = view.findViewById(R.id.et_phone_no);
            Button btn_send_otp = view.findViewById(R.id.btn_send_otp);

            TextView mpin_Login=view.findViewById(R.id.mpin_Login);
            TextView tv_register_now=view.findViewById(R.id.tv_register_now);
            mpin_Login.setOnClickListener(v->{
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginMpinFragment()).commit();

            });
            tv_register_now.setOnClickListener(v->{
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegistrationFragment()).addToBackStack(null).commit();

            });
           getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.GONE);
            btn_send_otp.setOnClickListener(v -> {
                if (et_phone_no.getText().length() > 0) {
                    MobileLogin(et_phone_no.getText().toString());
                } else {
                    CustomMessageHelper showDialog = new CustomMessageHelper(context);
                    showDialog.showCustomMessage((Activity) context, "Message", "Please enter your phone number", false, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    private void MobileLogin(String mobile_no) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map map = new HashMap();
        map.put("mobile_number", mobile_no);
        map.put("country_code", "+91");
        Call<AuthenticateMobile> authenticateMobilecall = api.userAuthentication(map);
        APIResponse.postCallRetrofit(authenticateMobilecall, "userAuth", context, this);
    }

    private void navigate(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }


    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {

        try {
            AuthenticateMobile authenticateMobile = (AuthenticateMobile) response.body();
            if (apiCallName.equalsIgnoreCase("userAuth")) {
                if (authenticateMobile.getStatus().equalsIgnoreCase("Success")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("token", authenticateMobile.getResult().getToken());
                    Fragment fragment = new VerifyOtpFragment();
                    fragment.setArguments(bundle);
                    navigate(fragment);
                } else {
                    GlobalClass.showErrorMsg(context, authenticateMobile.getError());
                }
            }
        } catch (Exception e) {
            Log.d("auth",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {



    }

}

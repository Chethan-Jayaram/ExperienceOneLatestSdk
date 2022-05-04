package com.taj.doorunlock.activity.loginfragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hbb20.CountryCodePicker;
import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.APIResponse;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.pojo.GeneralPojo;
import com.taj.doorunlock.services.ApiListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


public class UserAuthenticationFragment extends Fragment implements ApiListener {

    private Context mContext;
    private Boolean isEmailSelected=false;
    private EditText et_phone_no;
    private CountryCodePicker country_code_picker;
    private Button btn_email_ph_text;
    private TextView tv_register_now;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_auth, container, false);
        try{
            mContext = view.getContext();
            Button btn_send_otp = view.findViewById(R.id.btn_send_otp);
            TextView mpin_Login=view.findViewById(R.id.mpin_Login);
            et_phone_no = view.findViewById(R.id.et_phone_no);
            country_code_picker = view.findViewById(R.id.country_code_picker);
            btn_email_ph_text= view.findViewById(R.id.btn_email_ph_text);

            tv_register_now= view.findViewById(R.id.tv_register_now);
            btn_email_ph_text.setText("Login via E-mail");
            et_phone_no.setHint("Enter Your Phone Number");
            et_phone_no.requestFocus();
          //  et_phone_no.setInputType(InputType.TYPE_CLASS_NUMBER);

            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.GONE);

            mpin_Login.setOnClickListener(v->{
                Fragment fragment=new LoginMpinFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            });


            btn_send_otp.setOnClickListener(v -> {
                if (et_phone_no.getText().length() > 0) {
                    MobileLogin(et_phone_no.getText().toString().trim());
                } else {
                    GlobalClass.ShowAlet(mContext,"Alert","Please enter your phone number");
                }
            });
            btn_email_ph_text.setOnClickListener(v->{
                et_phone_no.setText("");
                if(isEmailSelected){
                    isEmailSelected=false;
                    et_phone_no.setInputType(InputType.TYPE_CLASS_NUMBER);
                    country_code_picker.setVisibility(View.VISIBLE);
                    btn_email_ph_text.setText("Login via E-mail");
                    et_phone_no.setHint("Enter Your Phone Number");
                }else{
                    isEmailSelected=true;
                    country_code_picker.setVisibility(View.GONE);
                    et_phone_no.setInputType(InputType.TYPE_CLASS_TEXT);
                    btn_email_ph_text.setText("Login via Phone Number");
                    et_phone_no.setHint("Enter Your E-mail ID");
                }
            });


            tv_register_now.setOnClickListener(v->{
                Fragment fragment1=new RegistrationFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment1).addToBackStack(null).commit();

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void MobileLogin(String mobile_no) {
        Map map = new HashMap();
        if(isEmailSelected) {
            map.put("mobile","");
            map.put("country_code", "");
            map.put("email",mobile_no);
        }else{
            map.put("mobile", mobile_no);
            map.put("country_code", country_code_picker.getSelectedCountryCode());
            map.put("email","");
        }
        map.put("device_id", GlobalClass.mPrefix+GlobalClass.android_id +GlobalClass.mSufix);
        Call<GeneralPojo> authenticateMobilecall = GlobalClass.mApi.userAuthentication(map);
        APIResponse.postCallRetrofit(authenticateMobilecall, "userAuth", mContext, this);
    }

    private void navigate(Fragment fragment) {
        et_phone_no.setText("");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }


    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {

        try {
            GeneralPojo generalPojo = (GeneralPojo) response.body();
            if (apiCallName.equalsIgnoreCase("userAuth")) {
                if (generalPojo.getStatus()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("request_id", generalPojo.getData().getRequestId());
                /*   GlobalClass.edit.clear();
                    GlobalClass.edit.apply();*/
                    if(isEmailSelected) {
                        bundle.putString("mobile_no",et_phone_no.getText().toString());
                    }else{
                        bundle.putString("mobile_no","+"+country_code_picker.getSelectedCountryCode()+"-"+et_phone_no.getText().toString());
                    }
                    Fragment fragment = new VerifyOtpFragment();
                    fragment.setArguments(bundle);
                    navigate(fragment);
                } else {
                    GlobalClass.ShowAlet(mContext,"Alert", generalPojo.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {
        GlobalClass.ShowAlet(mContext, "Alert", "Please provide a valid phone number or email");

    }

    @Override
    public void onPause() {
        super.onPause();
        if(GlobalClass.alert!=null && GlobalClass.alert.isShowing()){
            GlobalClass.alert.dismiss();
        }
    }
}

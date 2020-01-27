package com.example.experienceone.fragment.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.CustomMessageHelper;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.GeneralPojo;
import com.example.experienceone.pojo.userauthentication.AuthenticateMobile;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class RegistrationFragment extends Fragment implements ApiListener {

    private TextView tv_fname,tv_lname,tv_contact_no,tv_email;
    private Button btn_register;
    private CountryCodePicker reg_country_code_picker;
    private Context mCtx;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.guest_register_fragment,container,false);
        mCtx=view.getContext();
        getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nav_menu).setVisibility(View.GONE);
        getActivity().findViewById(R.id.iv_sos).setVisibility(View.GONE);
        TextView toolbar_title= getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Registration");

        tv_fname=view.findViewById(R.id.tv_fname);
        tv_lname=view.findViewById(R.id.tv_lname);
        tv_contact_no=view.findViewById(R.id.tv_contact_no);
        btn_register=view.findViewById(R.id.btn_register);
        tv_email=view.findViewById(R.id.tv_email);
        reg_country_code_picker=view.findViewById(R.id.reg_country_code_picker);



        btn_register.setOnClickListener(v->{
            if (tv_contact_no.getText().length() > 0 &&  !tv_fname.getText().toString().isEmpty()
            && !tv_email.getText().toString().isEmpty() && ! tv_lname.getText().toString().isEmpty()){
                if( tv_email.getText().toString().matches(emailPattern)){
                    registerUser(tv_contact_no.getText(),
                            tv_fname.getText().toString(),
                            tv_lname.getText().toString(),
                            tv_email.getText().toString());
                }else{
                    CustomMessageHelper showDialog = new CustomMessageHelper(mCtx);
                    showDialog.showCustomMessage((Activity) mCtx, "Message", "Invalid email address", false, false);
                }
            } else {
                CustomMessageHelper showDialog = new CustomMessageHelper(mCtx);
                showDialog.showCustomMessage((Activity) mCtx, "Message", "Fields can't be left blank", false, false);
            }
        });
        return view;
    }

    private void registerUser(CharSequence no, String fname, String lname, String email) {

        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String,String> map = new HashMap<>();
        map.put("first_name",fname);
        map.put("last_name",lname);
        map.put("email",email);
        map.put("contact_number", String.valueOf(no));
        Call<AuthenticateMobile> register = api.regUser(map);
        APIResponse.postCallRetrofit(register,"regUser", mCtx, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            AuthenticateMobile authenticateMobile = (AuthenticateMobile) response.body();
            if (apiCallName.equalsIgnoreCase("regUser")) {
                if (authenticateMobile.getStatus().equalsIgnoreCase("Success")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("token", authenticateMobile.getResult().getToken());
                    Fragment fragment = new VerifyOtpFragment();
                    fragment.setArguments(bundle);
                    navigate(fragment);
                }else {
                    GlobalClass.showErrorMsg(mCtx, authenticateMobile.getError());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {

    }

    private void navigate(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

}

package com.example.experienceone.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.loginmpin.LoginMpin;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import android.provider.Settings.Secure;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;

public class LoginMpinFragment extends Fragment implements ApiListener {
    private EditText et_1,et_2,et_3,et_4;
    private TextView tv_forgot_mpin;
    private Button btn_login;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_loginmpin,container,false);
        try {
            context = view.getContext();
            et_1 = view.findViewById(R.id.et_1);
            et_2 = view.findViewById(R.id.et_2);
            et_3 = view.findViewById(R.id.et_3);
            et_4 = view.findViewById(R.id.et_4);
            tv_forgot_mpin = view.findViewById(R.id.tv_forgot_mpin);
            btn_login = view.findViewById(R.id.btn_login);
            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.GONE);
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }
    private void init() {
        et_1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before,int count) { }
        });


        et_2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_3.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et_1.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before,int count) { }
        });
        et_3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_4.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et_2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        et_4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_4.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
                if (s.toString().isEmpty()) {
                    et_3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before,int count) { }
        });
        btn_login.setOnClickListener(v->{
            String android_id = Secure.getString(getContext().getContentResolver(),
                    Secure.ANDROID_ID);
            String mpin=et_1.getText().toString()+
                    et_2.getText().toString()+
                    et_3.getText().toString()+
                    et_4.getText().toString();
if(mpin.length()==4){
    LoginMpin(mpin,android_id);
}else{
    GlobalClass.ShowAlet(context,"Alert","Please enter valid Mpin");
}

        });
        tv_forgot_mpin.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserAuthenticationFragment()).commit();
        });
    }

    private void LoginMpin(String mpin,String device_id){
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String,String> map = new HashMap<>();
        map.put("mpin",mpin);
        map.put("device_id",device_id);
        Call<LoginMpin> loginMpincall = api.loginMpin(map);
        APIResponse.postCallRetrofit(loginMpincall,"loginMpin", context, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response,String apiCallName) {

            try {
            LoginMpin loginMpin= (LoginMpin) response.body();
            if(apiCallName.equalsIgnoreCase("loginMpin")) {
                if (loginMpin.getStatus().equalsIgnoreCase("Success")) {
                    GlobalClass.token=loginMpin.getResult().getToken();
                    GlobalClass.putsharedpreference(context,loginMpin.getResult().getGuest().getFirstName(),
                            loginMpin.getResult().getGuest().getLastName(),
                            loginMpin.getResult().getGuest().getContactNumber(),
                            loginMpin.getResult().getGuest().getEmail(),
                            loginMpin.getResult().getGuest().getMarital_status(),
                            loginMpin.getResult().getGuest().getGender(),
                            loginMpin.getResult().getGuest().getImage());
                    Intent intent = new Intent();
                    intent.setClassName(context, "com.example.experienceone.activity.HomeScreenActivity");
                    GlobalClass.loacation=loginMpin.getResult().getLocation().getLocationName();
                    startActivity(intent);
                    getActivity().finish();
                    GlobalClass.Guest_id=loginMpin.getResult().getGuest().getId()!=null?loginMpin.getResult().getGuest().getId():0;
                    if(loginMpin.getResult().getActiveBooking().size()>0){
                        GlobalClass.hasActiveBooking=true;
                        GlobalClass.Booking_id= loginMpin.getResult().getActiveBooking().get(0).getId()!=null?loginMpin.getResult().getActiveBooking().get(0).getId():0;
                    }else{
                        GlobalClass.hasActiveBooking=false;
                    }
                }else{
                    GlobalClass.showErrorMsg(context,loginMpin.getError());
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

package com.example.experienceone.fragment.login;

import android.app.Activity;
import android.content.Context;
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
import android.provider.Settings.Secure;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;
import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.CustomMessageHelper;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.creatempin.CreateMpin;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;


public class CreateMpinFragment extends Fragment implements ApiListener{

    private EditText et1_one,et1_two,et1_three,et1_four,et2_one,et2_two,et2_three,et2_four;
    private Context context;
    private String token;
    private LottieAnimationView animationView;
    private Button btn_mpin;



  /*  private RelativeLayout dialoug;
   private  TextView sucess_btn,sucess_message;
    private LinearLayout lyt_set_pin;*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_creatempin,container,false);
        try {
            context = view.getContext();
            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.VISIBLE);
            animationView = view.findViewById(R.id.animation_view);
            et1_one = view.findViewById(R.id.et1_one);
            et1_two = view.findViewById(R.id.et1_two);
            et1_three = view.findViewById(R.id.et1_three);
            et1_four = view.findViewById(R.id.et1_four);
            et2_one = view.findViewById(R.id.et2_one);
            et2_two = view.findViewById(R.id.et2_two);
            et2_three = view.findViewById(R.id.et2_three);
            et2_four = view.findViewById(R.id.et2_four);
            btn_mpin = view.findViewById(R.id.btn_mpin);

     /*   dialoug=view.findViewById(R.id.dialoug);
        sucess_btn=view.findViewById(R.id.sucess_btn);
        lyt_set_pin=view.findViewById(R.id.lyt_set_pin);
          dialoug.setVisibility(View.GONE);
        sucess_message=view.findViewById(R.id.sucess_message);*/
            et1_one.requestFocus();
            token = getArguments().getString("token", "");
            init();
       /* sucess_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    lyt_set_pin.setVisibility(View.VISIBLE);
                    dialoug.setVisibility(View.GONE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }


    private void init() {
        et1_two.setEnabled(false);
        et1_three.setEnabled(false);
        et1_four.setEnabled(false);
        et2_one.setEnabled(false);
        et2_two.setEnabled(false);
        et2_three.setEnabled(false);
        et2_four.setEnabled(false);
        // enter_otp_btn = view.findViewById(R.id.enter_otp_btn);
        et1_one.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et1_two.setEnabled(true);
                    et1_two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });


        et1_two.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et1_three.setEnabled(true);
                    et1_three.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et1_one.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        et1_three.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et1_four.setEnabled(true);
                    et1_four.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et1_two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        et1_four.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et2_one.setEnabled(true);
                    et2_one.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et1_three.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        et2_one.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et2_two.setEnabled(true);
                    et2_two.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et1_four.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });


        et2_two.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et2_three.setEnabled(true);
                    et2_three.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et2_one.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        et2_three.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et2_four.setEnabled(true);
                    et2_four.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et2_two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        et2_four.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.length()==1){
                    et2_four.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
                if (s.toString().isEmpty()) {
                    et2_three.setEnabled(true);
                    et2_three.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        btn_mpin.setOnClickListener(v1->{
             String android_id = Secure.getString(getContext().getContentResolver(),
                    Secure.ANDROID_ID);
             String  mpin=et1_one.getText().toString()+
                     et1_two.getText().toString()+
                     et1_three.getText().toString()+
                     et1_four.getText().toString();
            String  confirMpin=et2_one.getText().toString()+
                    et2_two.getText().toString()+
                    et2_three.getText().toString()+
                    et2_four.getText().toString();
            if(mpin.length()==4) {
                if (mpin.equals(confirMpin)) {
                    createMpin(mpin, confirMpin, android_id);
                } else {
                    GlobalClass.ShowAlet(context,"Alert","Mpin does not match");
                }
            }else{
                GlobalClass.ShowAlet(context,"Alert","Please enter valid Mpin");
            }
        });
    }

    private void createMpin(String mpin,String confirm_mpin,String device_id) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String,String> map = new HashMap<>();
        map.put("mpin",mpin);
        map.put("confirm_mpin",confirm_mpin);
        map.put("device_id",device_id);
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","bearer "+ token);
        Call<CreateMpin> mpinApiCall =api.createMpin(headerMap,map);
        APIResponse.postCallRetrofit(mpinApiCall,"createMpin", context, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response,String apiCallName) {
        CreateMpin createMpin= (CreateMpin) response.body();
        if(apiCallName.equalsIgnoreCase("createMpin")) {
            if (createMpin.getStatus().equalsIgnoreCase("Success")) {
                GlobalClass.isMpinSetupComplete = true;
                putsharedpreference();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginMpinFragment()).commit();
            }else{
                GlobalClass.showErrorMsg(context,createMpin.getError());
            }
        }
    }

    @Override
    public void onErrorListner() {

    }

    private void putsharedpreference() {
        GlobalClass.sharedPreferences = context.getSharedPreferences(GlobalClass.shredPrefName, 0);
        GlobalClass.edit = GlobalClass.sharedPreferences.edit();
        GlobalClass.edit.putBoolean("isMpinSetupComplete", true);
        GlobalClass.edit.apply();
    }
}

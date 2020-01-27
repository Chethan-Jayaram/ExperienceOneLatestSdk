package com.example.experienceone.fragment.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.example.experienceone.helper.SMSReceiver;
import com.example.experienceone.pojo.userauthentication.AuthenticateMobile;
import com.example.experienceone.pojo.userauthentication.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;

public class VerifyOtpFragment extends Fragment implements SMSReceiver.OTPReceiveListener, ApiListener {

private EditText et_one,et_two,et_three,et_four,et_five,et_six;
private Context context;
private String token;
private Handler handler;
private Button btn_otp;
private TextView tv_resend_otp,tv_timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_verifyotp,container,false);
        try {
            context = view.getContext();
            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.nav_menu).setVisibility(View.GONE);
            getActivity().findViewById(R.id.iv_sos).setVisibility(View.GONE);
            TextView toolbar_title= getActivity().findViewById(R.id.toolbar_title);
            tv_resend_otp=view.findViewById(R.id.tv_resend_otp);
            tv_timer=view.findViewById(R.id.tv_timer);
            et_one = view.findViewById(R.id.et_one);
            et_two = view.findViewById(R.id.et_two);
            et_three = view.findViewById(R.id.et_three);
            et_four = view.findViewById(R.id.et_four);
            et_five = view.findViewById(R.id.et_five);
            et_six = view.findViewById(R.id.et_six);
            toolbar_title.setText("Verify OTP");
            btn_otp = view.findViewById(R.id.btn_otp);
            tv_resend_otp.setVisibility(View.GONE);
            tv_timer.setVisibility(View.VISIBLE);

            et_one.requestFocus();
            token = getArguments().getString("token", "");
            init();
            startTimer();
            startSMSListener();
            tv_resend_otp.setOnClickListener(v -> {
                tv_resend_otp.setVisibility(View.GONE);
                tv_timer.setVisibility(View.VISIBLE);
                resendOtpAPI(token);
                startTimer();
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }
    private void init() {
        et_two.setEnabled(false);
        et_three.setEnabled(false);
        et_four.setEnabled(false);
        et_five.setEnabled(false);
        et_six.setEnabled(false);
        // enter_otp_btn = view.findViewById(R.id.enter_otp_btn);
        et_one.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_two.setEnabled(true);
                    et_two.requestFocus();

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });


        et_two.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_three.setEnabled(true);
                    et_three.requestFocus();

                }
                if (s.toString().isEmpty()) {
                    et_one.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        et_three.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_four.setEnabled(true);
                    et_four.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et_two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        et_four.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_five.setEnabled(true);
                    et_five.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et_three.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        et_five.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_six.setEnabled(true);
                    et_six.requestFocus();
                }
                if (s.toString().isEmpty()) {
                    et_four.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        et_six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()==1)
                {
                    btn_otp.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
                if (editable.toString().isEmpty()) {
                    et_five.requestFocus();
                }
            }
        });
        btn_otp.setOnClickListener(v->{
            String otp=et_one.getText().toString()+
                    et_two.getText().toString()+
                    et_three.getText().toString()+
                    et_four.getText().toString()+
                    et_five.getText().toString()+
                    et_six.getText().toString();
            if(otp.length()==6){
                verifyOTP(otp,token);
            }else{
                CustomMessageHelper showDialog = new CustomMessageHelper(context);
                showDialog.showCustomMessage((Activity) context, "Message", "Please fill in the OTP", false, false);
            }

        });
    }
    private void startTimer() {
        try {
            new CountDownTimer(60000, 1000) {
                public void onTick(long millisUntilFinished) {
                    tv_timer.setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds");
                }
                public void onFinish() {
                    tv_timer.setVisibility(View.GONE);
                    tv_resend_otp.setVisibility(View.VISIBLE);
                    tv_resend_otp.setText("Click here to resend code");
                }

            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSMSListener() {
        try {
            SMSReceiver smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            context.registerReceiver(smsReceiver, intentFilter);
            SmsRetrieverClient client = SmsRetriever.getClient(context);
            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(aVoid -> {
                // API successfully started
            });
            task.addOnFailureListener(e -> {
                // Fail to start API
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOTPReceived(String otp) {
        try {
            et_one.setText(otp.charAt(1) + "");
            et_two.setText(otp.charAt(2) + "");
            et_three.setText(otp.charAt(3) + "");
            et_four.setText(otp.charAt(4) + "");
            et_five.setText(otp.charAt(5) + "");
            et_six.setText(otp.charAt(6) + "");
            handler = new Handler();
            handler.postDelayed(() -> {
                btn_otp.performClick();

            }, 2000);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }
    private void verifyOTP(String Otp,String Token) {

        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map map = new HashMap();
        map.put("otp",Otp);
        map.put("token",Token);
        Call<AuthenticateMobile> authenticateMobileCall = api.verifyOTP(map);
        APIResponse.postCallRetrofit(authenticateMobileCall,"verifyOTP", context, this);
    }

    private void resendOtpAPI(String token) {
        try {
            Map map = new HashMap();
            map.put("token",token);
            APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
            Call<AuthenticateMobile> authenticateMobileCall = api.resendOTP(map);
            APIResponse.postCallRetrofit(authenticateMobileCall,"resendOTP", context, this);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response,String apiCallName) {

        try {
            AuthenticateMobile authenticateMobile = (AuthenticateMobile) response.body();
            authenticateMobile = (AuthenticateMobile) response.body();
            Result result=authenticateMobile.getResult();
            Bundle bundle = new Bundle();
            if (apiCallName.equalsIgnoreCase("resendOTP")) {
                if (authenticateMobile.getStatus().equalsIgnoreCase("Success")) {
                    GlobalClass.showErrorMsg(context,"Otp Sent Successfully");
                }else{
                    GlobalClass.showErrorMsg(context,authenticateMobile.getError());
                }
            } else if (apiCallName.equalsIgnoreCase("verifyOTP")) {
                    if (authenticateMobile.getStatus().equalsIgnoreCase("Success")) {
                        if (result.getStatus().equalsIgnoreCase("Otp Validated")) {
                            bundle.putString("token", authenticateMobile.getResult().getToken());
                            navFragment(bundle);
                        }
                    }else{
                        GlobalClass.showErrorMsg(context,authenticateMobile.getError());
                    }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {

    }

    private void navFragment(Bundle bundle) {
        Fragment fragment = new CreateMpinFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

}

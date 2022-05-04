package com.taj.doorunlock.activity.loginfragments;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.onesignal.OneSignal;
import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.APIResponse;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.pojo.GeneralPojo;
import com.taj.doorunlock.retrofit.ClientServiceGenerator;
import com.taj.doorunlock.services.APIMethods;
import com.taj.doorunlock.services.ApiListener;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class VerifyOtpFragment extends Fragment implements  ApiListener {

    private EditText et_one, et_two, et_three, et_four, et_five, et_six;
    private Context mContext;
    private String mRequest_id;
    private Button btn_otp;
    private TextView tv_resend_otp;
    private TextView tv_timer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verifyotp, container, false);
        try {
            mContext = view.getContext();
            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.nav_menu).setVisibility(View.GONE);
            getActivity().findViewById(R.id.iv_sos).setVisibility(View.GONE);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            tv_resend_otp = view.findViewById(R.id.tv_resend_otp);
            tv_timer = view.findViewById(R.id.tv_timer);
            et_one = view.findViewById(R.id.et_one);
            et_two = view.findViewById(R.id.et_two);
            et_three = view.findViewById(R.id.et_three);
            et_four = view.findViewById(R.id.et_four);
            et_five = view.findViewById(R.id.et_five);
            et_six = view.findViewById(R.id.et_six);
            TextView user_ph_no = view.findViewById(R.id.user_ph_no);
            toolbar_title.setText("Verify OTP");
            btn_otp = view.findViewById(R.id.btn_otp);
            tv_resend_otp.setVisibility(View.GONE);
            tv_timer.setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.GONE);

            et_one.requestFocus();
            mRequest_id = getArguments().getString("request_id", "");
            String mobile_no = getArguments().getString("mobile_no", "");
            user_ph_no.setText("to " + mobile_no);
            init();
            startTimer();
            startSMSListener();
            tv_resend_otp.setOnClickListener(v -> {
                tv_resend_otp.setVisibility(View.GONE);
                tv_timer.setVisibility(View.VISIBLE);
                resendOtpAPI(mRequest_id);
                startTimer();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void init() {
        et_one.requestFocus();
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
                if (editable.length() == 1) {
                    btn_otp.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
                if (editable.toString().isEmpty()) {
                    et_five.requestFocus();
                }
            }
        });
        btn_otp.setOnClickListener(v -> {
            String otp = et_one.getText().toString() +
                    et_two.getText().toString() +
                    et_three.getText().toString() +
                    et_four.getText().toString() +
                    et_five.getText().toString() +
                    et_six.getText().toString();
            if (otp.length() == 6) {
                verifyOTP(otp, mRequest_id);
            } else {
                GlobalClass.ShowAlet(mContext, "Message", "Please enter valid OTP");
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


    public void onOTPReceived(String otp) {
        try {
            et_one.setText(otp.charAt(0) + "");
            et_two.setText(otp.charAt(1) + "");
            et_three.setText(otp.charAt(2) + "");
            et_four.setText(otp.charAt(3) + "");
            et_five.setText(otp.charAt(4) + "");
            et_six.setText(otp.charAt(5) + "");
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                btn_otp.performClick();

            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void verifyOTP(String Otp, String request_id) {
        Map<String, String> map = new HashMap<>();
        map.put("otp", Otp);
        map.put("request_id", request_id);
        Call<GeneralPojo> authenticateMobileCall = GlobalClass.mApi.verifyOTP(map);
        APIResponse.postCallRetrofit(authenticateMobileCall, "verifyOTP", mContext, this);
    }

    private void resendOtpAPI(String request_id) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("request_id", request_id);
            APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
            Call<GeneralPojo> authenticateMobileCall = api.resendOTP(map);
            APIResponse.postCallRetrofit(authenticateMobileCall, "resendOTP", mContext, this);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            GeneralPojo generalPojo = (GeneralPojo) response.body();

            Bundle bundle = new Bundle();
            if (apiCallName.equalsIgnoreCase("resendOTP")) {
                if (generalPojo.getStatus()) {
                    GlobalClass.ShowAlet(mContext, "Alert", "Otp Sent Successfully");
                } else {
                    GlobalClass.ShowAlet(mContext, "Alert", generalPojo.getMessage());
                }
            } else if (apiCallName.equalsIgnoreCase("verifyOTP")) {
                if (generalPojo.getStatus()) {
                    bundle.putString("token", generalPojo.getData().getToken());
                    navFragment(bundle);
                } else {
                    GlobalClass.ShowAlet(mContext, "Message", generalPojo.getMessage());
                        et_six.setText("");
                        et_five.setText("");
                        et_four.setText("");
                        et_three.setText("");
                        et_two.setText("");
                        et_one.setText("");
                        new Handler().postDelayed(() -> et_one.requestFocus(),200);
                }
            }
        } catch (Exception e) {
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
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(smsVerificationReceiver);
        if(GlobalClass.alert!=null && GlobalClass.alert.isShowing()){
            GlobalClass.alert.dismiss();
        }
    }


    private void startSMSListener() {
        try {
            Task<Void> task = SmsRetriever.getClient(mContext).startSmsUserConsent(null);
            IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
            getActivity().registerReceiver(smsVerificationReceiver,intentFilter,SmsRetriever.SEND_PERMISSION,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startSMSListener();
    }

    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // ...
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                    //  String oneTimeCode = message); // define this function


                    String[] OTP= message.split("is ",6);

                    onOTPReceived(OTP[1].trim().substring(0,6).trim());
                    // send one time code to the server
                } else {
                    // Consent canceled, handle the error ...
                }
                break;
        }
    }

}

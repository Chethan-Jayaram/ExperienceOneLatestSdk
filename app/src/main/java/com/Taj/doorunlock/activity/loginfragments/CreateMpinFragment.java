package com.taj.doorunlock.activity.loginfragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onesignal.OneSignal;
import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.APIResponse;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.pojo.GeneralPojo;
import com.taj.doorunlock.services.ApiListener;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;


public class CreateMpinFragment extends Fragment implements ApiListener {


    private Context mContext;
    private String mToken;
    private String mpin;
    private String confirMpin;
    private EditText et1_one, et1_two, et1_three, et1_four, et2_one, et2_two, et2_three, et2_four;
    private Button btn_mpin;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creatempin, container, false);
        try {
            mContext = view.getContext();
            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.VISIBLE);
            et1_one = view.findViewById(R.id.et1_one);
            et1_two = view.findViewById(R.id.et1_two);
            et1_three = view.findViewById(R.id.et1_three);
            et1_four = view.findViewById(R.id.et1_four);
            et2_one = view.findViewById(R.id.et2_one);
            et2_two = view.findViewById(R.id.et2_two);
            et2_three = view.findViewById(R.id.et2_three);
            et2_four = view.findViewById(R.id.et2_four);
            btn_mpin = view.findViewById(R.id.btn_mpin);
            getActivity().findViewById(R.id.toolbar_cart).setVisibility(View.GONE);


            et1_one.requestFocus();


            mToken = getArguments().getString("token", "");
            init();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    private void init() {
        et1_one.requestFocus();
        et1_two.setEnabled(false);
        et1_three.setEnabled(false);
        et1_four.setEnabled(false);
        et2_one.setEnabled(false);
        et2_two.setEnabled(false);
        et2_three.setEnabled(false);
        et2_four.setEnabled(false);
        et1_one.requestFocus();

        // enter_otp_btn = view.findViewById(R.id.enter_otp_btn);
        et1_one.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                new Handler().postDelayed(() -> {
                    if (s.length() == 1) {
                        et1_two.setEnabled(true);
                        et1_two.requestFocus();
                    }
                }, GlobalClass.mDelay);
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
                new Handler().postDelayed(() -> {
                    if (s.length() == 1) {
                        et1_three.setEnabled(true);
                        et1_three.requestFocus();
                    }
                    if (s.toString().isEmpty()) {
                        et1_one.requestFocus();
                    }
                }, GlobalClass.mDelay);

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
                new Handler().postDelayed(() -> {
                    if (s.length() == 1) {
                        et1_four.setEnabled(true);
                        et1_four.requestFocus();
                    }
                    if (s.toString().isEmpty()) {
                        et1_two.requestFocus();
                    }
                }, GlobalClass.mDelay);

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
                new Handler().postDelayed(() -> {
                    if (s.length() == 1) {
                        et2_one.setEnabled(true);
                        et2_one.requestFocus();
                    }
                    if (s.toString().isEmpty()) {
                        et1_three.requestFocus();
                    }
                }, GlobalClass.mDelay);

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
                new Handler().postDelayed(() -> {
                    if (s.length() == 1) {
                        et2_two.setEnabled(true);
                        et2_two.requestFocus();
                    }
                    if (s.toString().isEmpty()) {
                        et1_four.requestFocus();
                    }
                }, GlobalClass.mDelay);

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
                new Handler().postDelayed(() -> {
                    if (s.length() == 1) {
                        et2_three.setEnabled(true);
                        et2_three.requestFocus();
                    }
                    if (s.toString().isEmpty()) {
                        et2_one.requestFocus();
                    }
                }, GlobalClass.mDelay);

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

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        et2_four.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
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

        btn_mpin.setOnClickListener(v1 -> {

            mpin = et1_one.getText().toString() +
                    et1_two.getText().toString() +
                    et1_three.getText().toString() +
                    et1_four.getText().toString();
            confirMpin = et2_one.getText().toString() +
                    et2_two.getText().toString() +
                    et2_three.getText().toString() +
                    et2_four.getText().toString();
            if (mpin.length() == 4) {
                if (mpin.equals(confirMpin)) {
                    createMpin(mpin, confirMpin,     GlobalClass.android_id , mToken);
                } else {
                    GlobalClass.ShowAlet(mContext, "Alert", "Mpin does not match");
                    ClearFields();
                }
            } else {
                GlobalClass.ShowAlet(mContext, "Alert", "Please enter valid Mpin");
            }
        });
    }


    private void ClearFields() {
        et1_one.setText("");
        et1_two.setText("");
        et1_three.setText("");
        et1_four.setText("");
        et2_one.setText("");
        et2_two.setText("");
        et2_three.setText("");
        et2_four.setText("");
        new Handler().postDelayed(() -> et1_one.requestFocus(), GlobalClass.mDelay);
    }



    private void createMpin(String mpin, String confirm_mpin, String device_id, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("mpin", mpin);
        map.put("confirm_mpin", confirm_mpin);
        map.put("device_id", GlobalClass.mPrefix+GlobalClass.android_id +GlobalClass.mSufix);
        map.put("token", token);
        map.put("player_id", OneSignal.getDeviceState().getUserId());
        map.put("source","Android:"+ Build.MODEL);
        Call<GeneralPojo> mpinApiCall = GlobalClass.mApi.createMpin(map);
        APIResponse.postCallRetrofit(mpinApiCall, "createMpin", mContext, this);
    }



    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {

            if (apiCallName.equalsIgnoreCase("createMpin")) {
                GeneralPojo generalPojo = (GeneralPojo) response.body();
                if (generalPojo.getStatus()) {
                    putSharedPreference(generalPojo.getData().getToken());
                    Fragment fragment=new LoginMpinFragment();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                } else {
                    GlobalClass.ShowAlet(mContext, "Alert", generalPojo.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onErrorListner() {
        GlobalClass.ShowAlet(mContext, "Alert","M-pin did not match, please try again");
    }



    private void putSharedPreference(String token) {
        GlobalClass.edit.putBoolean("isMpinSetupComplete", true);
        GlobalClass.edit.putBoolean("hasIvitationCode", false);
        GlobalClass.edit.putString("user_token",token);
        GlobalClass.edit.apply();
    }


    @Override
    public void onPause() {
        super.onPause();
        if(GlobalClass.alert!=null && GlobalClass.alert.isShowing()){
            GlobalClass.alert.dismiss();
        }
    }


}

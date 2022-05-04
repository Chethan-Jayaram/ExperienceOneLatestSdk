package com.taj.doorunlock.activity.loginfragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hbb20.CountryCodePicker;
import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.APIResponse;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.pojo.GeneralPojo;
import com.taj.doorunlock.retrofit.ClientServiceGenerator;
import com.taj.doorunlock.services.APIMethods;
import com.taj.doorunlock.services.ApiListener;

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

    private String[] mSalutation = {"--Please Select--","Mr.", "Ms.","Mrs.","Dr."};
    private Spinner et_saluation_type;
    private String mSelectedSaluationType;


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
        et_saluation_type=view.findViewById(R.id.et_saluation_type);

        ArrayAdapter arrayAdapter = new ArrayAdapter(mCtx, R.layout.spinner_item, mSalutation);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        et_saluation_type.setAdapter(arrayAdapter);

        et_saluation_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                try {
                    mSelectedSaluationType = mSalutation[pos];
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_register.setOnClickListener(v->{
            if (!mSelectedSaluationType.equalsIgnoreCase("--Please Select--")&&
                    tv_contact_no.getText().length() > 0 &&  !tv_fname.getText().toString().isEmpty()
            && !tv_email.getText().toString().isEmpty() && ! tv_lname.getText().toString().isEmpty()){
                if( tv_email.getText().toString().matches(emailPattern)){
                    registerUser(mSelectedSaluationType,tv_contact_no.getText(),
                            tv_fname.getText().toString(),
                            tv_lname.getText().toString(),
                            tv_email.getText().toString());
                }else{
                    GlobalClass.ShowAlet(mCtx,"Alert",   "Invalid email address");
                }
            } else {
                GlobalClass.ShowAlet(mCtx,"Alert",  "Fields can't be left blank");
            }
        });
        return view;
    }

    private void registerUser(String mSelectedSaluationType, CharSequence no, String fname, String lname, String email) {

        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String,String> map = new HashMap<>();
        map.put("salutation",mSelectedSaluationType);
        map.put("firstname",fname);
        map.put("lastname",lname);
        map.put("email",email);
        map.put("mobile", String.valueOf(no));
        map.put("country_code", "+"+reg_country_code_picker.getSelectedCountryCode());
        map.put("device_id", GlobalClass.mPrefix+GlobalClass.android_id +GlobalClass.mSufix);
        Call<GeneralPojo> register = api.userRegistration(map);
        APIResponse.postCallRetrofit(register,"regUser", mCtx, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            GeneralPojo generalPojo = (GeneralPojo) response.body();
            if (apiCallName.equalsIgnoreCase("regUser")) {
                if (generalPojo.getStatus()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("request_id", generalPojo.getData().getRequestId());
                    bundle.putString("mobile_no",tv_contact_no.getText().toString());
                    Fragment fragment = new VerifyOtpFragment();
                    fragment.setArguments(bundle);
                    navigate(fragment);
                }else {
                    GlobalClass.ShowAlet(mCtx,"Alert", generalPojo.getMessage());
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

package com.example.experienceone.fragment.general;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.CustomMessageHelper;
import com.example.experienceone.helper.GlobalClass;


import com.example.experienceone.pojo.GuestDeatils;
import com.example.experienceone.pojo.loginmpin.Guest;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.myhexaville.smartimagepicker.ImagePicker;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment implements ApiListener {

    private Context mContext;
    private ImageView img_profile;
    private EditText et_fst_name, et_lst_name, et_mob_no, et_email;
    private TextView status_spinner, gender_spinner;
    private Bitmap bitmap;
    private Boolean isProfilePicChanged = false;
    private String firstName, lastName, contactNumber, email, martialStatus, gender, img;
    private ImagePicker imagePicker;
    private Uri uri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_profile_fragment, container, false);
        try {
            mContext = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.GONE);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.nav_menu).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.iv_sos).setVisibility(View.GONE);


            toolbar_title.setText("My Profile");
            img_profile = view.findViewById(R.id.img_profile);
            et_fst_name = view.findViewById(R.id.et_fst_name);
            et_lst_name = view.findViewById(R.id.et_lst_name);
            et_mob_no = view.findViewById(R.id.et_mob_no);
            et_email = view.findViewById(R.id.et_email);

            status_spinner = view.findViewById(R.id.status_spinner);
            gender_spinner = view.findViewById(R.id.gender_spinner);
            Button btn_edit_profile = view.findViewById(R.id.btn_edit_profile);
            imagePicker = new ImagePicker(getActivity(), this, v -> {
                try {
                    isProfilePicChanged = true;
                    uri=v;

                    //img_profile.setImageBitmap(bitmap);
                    img_profile.setImageURI(v);

                    bitmap=((BitmapDrawable)img_profile.getDrawable()).getBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            getGuestProfileDetails();
            et_fst_name.setText(firstName);
            et_lst_name.setText(lastName);
            et_email.setText(email);
            et_mob_no.setText(contactNumber);
            if (!martialStatus.isEmpty()) {
                status_spinner.setText(martialStatus);
            } else {
                status_spinner.setText("-Please Select--");
            }

            if (!gender.isEmpty()) {
                gender_spinner.setText(gender);
            } else {
                gender_spinner.setText("-Please Select--");
            }

            status_spinner.setOnClickListener(v -> {
                showCustomDialog("Single", "Married", 1);
            });
            gender_spinner.setOnClickListener(v -> {
                showCustomDialog("Male", "Female", 0);
            });
            img_profile.setOnClickListener(v -> {
                try {
                    imagePicker.choosePicture(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            btn_edit_profile.setOnClickListener(v -> {
                try {
                    if (!et_fst_name.getText().toString().isEmpty()
                            && !et_email.getText().toString().isEmpty()
                            && !et_mob_no.getText().toString().isEmpty()
                            && !et_lst_name.getText().toString().isEmpty()) {

                        String img_base64_st = "";
                        if (bitmap != null) {
                            // bitmap = new Compressor(mContext).compressToFile(bitmap);

                            img_base64_st = GlobalClass.encodeTobase64(bitmap);
                        }
                        prepareProfileData(et_fst_name.getText().toString(),
                                et_lst_name.getText().toString(),
                                et_email.getText().toString(),
                                et_mob_no.getText().toString(),
                                img_base64_st);
                    } else {
                        CustomMessageHelper showDialog = new CustomMessageHelper(mContext);
                        showDialog.showCustomMessage((Activity) mContext, "Message", "Fields can't be left blank", false, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            if (!img.isEmpty()) {
                Glide.with(mContext).load(img).error(R.drawable.profile_image).into(img_profile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    private void prepareProfileData(String firstName, String lastName, String email, String contactNumber, String img_base64_st) {
        Map<String, String> map = new HashMap<>();
        map.put("first_name", firstName);
        map.put("last_name", lastName);
        map.put("gender", gender_spinner.getText().toString());
        map.put("marital_status", status_spinner.getText().toString());
        map.put("email", email);
        map.put("contact_number", contactNumber);
        if (isProfilePicChanged) {
            map.put("image", "data:image/jpeg;base64," + img_base64_st);
        }
        postProfileDetails(map);
    }

    private void postProfileDetails(Map<String, String> map) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<GuestDeatils> editprofile = api.putProfileDeatils(String.valueOf(GlobalClass.Guest_id), headerMap, map);
        APIResponse.postCallRetrofit(editprofile, "editprofile", mContext, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {

        try {
            if (apiCallName.equalsIgnoreCase("editprofile")) {

                GuestDeatils guestDeatils = (GuestDeatils) response.body();
                if (guestDeatils.getStatus().equalsIgnoreCase("Success")) {
                    GlobalClass.putsharedpreference(mContext, guestDeatils.getResult().getFirstName(),
                            guestDeatils.getResult().getLastName(),
                            guestDeatils.getResult().getContactNumber(),
                            guestDeatils.getResult().getEmail(),
                            guestDeatils.getResult().getMarital_status(),
                            guestDeatils.getResult().getGender(),
                            guestDeatils.getResult().getImage());
                    GlobalClass.showErrorMsg(mContext, "Profile updated successfully");
                } else {
                    GlobalClass.showErrorMsg(mContext, guestDeatils.getError());
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onErrorListner() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }



    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.img_et_btn).setVisibility(View.GONE);
    }

    private void showCustomDialog(String s1, String s2, int view) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.spinner_content, viewGroup, false);

        RadioGroup rd_group = dialogView.findViewById(R.id.rd_group);
        RadioButton rd1 = dialogView.findViewById(R.id.rd_1);
        RadioButton rd2 = dialogView.findViewById(R.id.rd_2);

        rd1.setText(s1);
        rd2.setText(s2);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();


        rd_group.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            RadioButton rb = dialogView.findViewById(checkedId);
            if (view == 0) {
                gender_spinner.setText(rb.getText());
            } else if (view == 1) {
                status_spinner.setText(rb.getText());
            }
            alertDialog.dismiss();
        });
        //finally creating the alert dialog and displaying it
        alertDialog.show();
    }

    public void getGuestProfileDetails() {
        GlobalClass.edit = GlobalClass.sharedPreferences.edit();
        firstName = GlobalClass.sharedPreferences.getString("fName", "");
        lastName = GlobalClass.sharedPreferences.getString("lName", "");
        contactNumber = GlobalClass.sharedPreferences.getString("cNumber", "");
        email = GlobalClass.sharedPreferences.getString("eMail", "");
        martialStatus = GlobalClass.sharedPreferences.getString("mStatus", "");
        gender = GlobalClass.sharedPreferences.getString("gender", "");
        img = GlobalClass.sharedPreferences.getString("img", "");
    }


}

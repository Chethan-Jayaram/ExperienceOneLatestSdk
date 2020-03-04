package com.example.experienceone.fragment.general;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.example.experienceone.R;
import com.example.experienceone.adapter.generaladapters.MobileCheckinImagePickerAdapter;
import com.example.experienceone.helper.GlobalClass;


import java.util.List;

import static com.example.experienceone.helper.GlobalClass.PICKER_REQUEST_CODE;

public class MobileCheckInFragment extends Fragment {


    private LinearLayout lyt_upload;
    private Context mContext;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile_checkin, container, false);
        try {
            mContext = view.getContext();
            getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            lyt_upload = view.findViewById(R.id.lyt_upload);
            mRecyclerView = view.findViewById(R.id.mobile_checkin_recycler);
            toolbar_title.setText("Mobile Check-In");


            lyt_upload.setOnClickListener(v -> {
                ImagePicker.create(this)
                        .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                        .folderMode(true) // folder mode (false by default)
                        .toolbarFolderTitle("Folder") // folder selection title
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                        .multi()
                        .limit(5) // max images can be selected (99 by default)
                        .showCamera(false) // show camera or not (true by default)
                        .start(); // start image picker activity with request code

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                // Get a list of picked images
                List<Image> images = ImagePicker.getImages(data);
                MobileCheckinImagePickerAdapter adapter = new MobileCheckinImagePickerAdapter(images, images::remove);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(adapter);
            }
            super.onActivityResult(requestCode, resultCode, data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


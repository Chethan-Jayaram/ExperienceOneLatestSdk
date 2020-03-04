package com.example.experienceone.fragment.general;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.experienceone.R;

public class HotelInformation extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.hotel_information,container,false);
        getActivity().findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Hotel Information");
        return view;
    }
}

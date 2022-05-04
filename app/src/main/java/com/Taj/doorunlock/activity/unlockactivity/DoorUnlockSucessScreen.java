package com.taj.doorunlock.activity.unlockactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.taj.doorunlock.R;
import com.taj.doorunlock.activity.BookingDetailsListActivity;

public class DoorUnlockSucessScreen extends Fragment {

    private TextView tv_room_no;
    private Handler handler;

    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doorlock_sucess, container, false);
       /* Bundle data = getArguments();

        tv_room_no=view.findViewById(R.id.tv_room_no);

        tv_room_no.setText(data.getString("room_no",""));*/
        mContext=view.getContext();

        handler = new Handler();
        handler.postDelayed(() -> {
            try {
                Intent intent = new Intent(mContext, BookingDetailsListActivity.class);
                startActivity(intent);
                getActivity().finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 2000);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}

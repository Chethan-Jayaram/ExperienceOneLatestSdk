package com.example.experienceone.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.experienceone.R;
import com.example.experienceone.fragment.general.HomeGridFragment;
import com.example.experienceone.helper.CustomMessageHelper;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            final String action = intent.getAction();
            int status = NetworkUtil.getConnectivityStatusString(context);
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    CustomMessageHelper showDialog = new CustomMessageHelper(context);
                    showDialog.showCustomMessage((Activity) context, "Alert!!", context.getString(R.string.NETWORK_ISSUE), false, false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
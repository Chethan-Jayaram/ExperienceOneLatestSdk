package com.taj.doorunlock.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taj.doorunlock.R;

import com.taj.doorunlock.helper.GlobalClass;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            final String action = intent.getAction();
            int status = NetworkUtil.getConnectivityStatusString(context);
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    GlobalClass.ShowAlet(context,"Alert",context.getString(R.string.NETWORK_ISSUE));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
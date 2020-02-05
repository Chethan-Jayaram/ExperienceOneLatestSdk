package com.example.experienceone.utils;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.experienceone.activity.HomeScreenActivity;
import com.example.experienceone.fragment.general.HomeGridFragment;
import com.example.experienceone.helper.GlobalClass;


public class BluetoothChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_OFF) {
                    Intent ble = new Intent(context, HomeScreenActivity.class);
                    ble.putExtra("changes", "ble");
                    ble.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(ble);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
package com.taj.doorunlock.utils;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taj.doorunlock.activity.BookingDetailsListActivity;


public class BluetoothChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_OFF) {
                    Intent ble = new Intent(context, BookingDetailsListActivity.class);
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
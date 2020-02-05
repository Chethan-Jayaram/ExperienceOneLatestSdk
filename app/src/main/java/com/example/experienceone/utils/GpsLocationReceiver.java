package com.example.experienceone.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.experienceone.activity.HomeScreenActivity;

public class GpsLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                Intent loc = new Intent(context, HomeScreenActivity.class);
                loc.putExtra("changes", "loc");
                loc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(loc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
package com.taj.doorunlock.helper;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;


import com.onesignal.OSNotification;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.taj.doorunlock.activity.SplashActivity;
import com.taj.doorunlock.unlock.Taj;

import org.json.JSONObject;

public class NotificationHandler implements OneSignal.NotificationOpenedHandler, OneSignal.NotificationReceivedHandler {

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        try {

            Context context = Taj.getAppContext();
            Intent intent = new Intent(context, SplashActivity.class);

            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(intent);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;

        if (data != null) {
            customKey = data.optString("custom", null);
            if (customKey != null)
            {
                //I do sth here
                NotificationManager nMgr = (NotificationManager) Taj.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancelAll();
            }
        }
    }
}
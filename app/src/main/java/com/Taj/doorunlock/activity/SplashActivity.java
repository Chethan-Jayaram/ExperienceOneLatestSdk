package com.taj.doorunlock.activity;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.GlobalClass;

import static com.taj.doorunlock.helper.GlobalClass.getPermissions;
import static com.taj.doorunlock.helper.GlobalClass.sharedPreferences;
import static com.taj.doorunlock.helper.GlobalClass.showPermissionDialoug;


public class SplashActivity extends AppCompatActivity {

    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    private  int SPLASH_DISPLAY_LENGTH = 2500;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {






        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, UseAuthenticationActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
      /*  if (GlobalClass.hasLocationPermissions(this)) {
            new Handler().postDelayed(() -> {
                Intent mainIntent = new Intent(SplashActivity.this, UseAuthenticationActivity.class);
                startActivity(mainIntent);
                finish();
            }, SPLASH_DISPLAY_LENGTH);

        }else{
            if (!sharedPreferences.getBoolean("requestedRuntimePermission", false)) {
              showDialog(this);
            }

        }*/
    }


    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.location_dailoug);

        Button btn_enable = dialog.findViewById(R.id.btn_enable);


        btn_enable.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(activity,
                    getPermissions(),
                    MY_PERMISSIONS_REQUEST_LOCATION);
            dialog.dismiss();

                }

        );

        dialog.show();
        dialog.setCancelable(false);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

            new Handler().postDelayed(() -> {
                Intent mainIntent = new Intent(SplashActivity.this, UseAuthenticationActivity.class);
                startActivity(mainIntent);
                finish();
                GlobalClass.edit.putBoolean("requestedRuntimePermission", true);
                GlobalClass.edit.apply();
            }, SPLASH_DISPLAY_LENGTH);


        }
    }

}

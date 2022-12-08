// -----------------------------------------------------------------------------
// Copyright© 2017 LEGIC® Identsystems AG, CH-8623 Wetzikon
// Confidential. All rights reserved!
// -----------------------------------------------------------------------------

package com.taj.doorunlock.activity.unlockactivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legic.mobile.sdk.api.exception.LegicMobileSdkException;
import com.legic.mobile.sdk.api.types.LcMessageMode;
import com.legic.mobile.sdk.api.types.LegicMobileSdkStatus;
import com.legic.mobile.sdk.api.types.LegicNeonFile;
import com.legic.mobile.sdk.api.types.LegicNeonFileDefaultMode;
import com.legic.mobile.sdk.api.types.LegicNeonFileState;
import com.legic.mobile.sdk.api.types.RfInterface;

import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.helper.ProgressBarAnimation;
import com.taj.doorunlock.unlock.doormakaba.BLEDataHandler;
import com.taj.doorunlock.unlock.doormakaba.BaseActivity;

import java.util.List;

import static com.taj.doorunlock.helper.GlobalClass.sharedPreferences;

import androidx.core.content.ContextCompat;


public class DoormakabaUnlockActivity extends BaseActivity {

    // this code can be any number, it is just a "marker"
    public static final String EXTRA_PUSH = "PushReceived";

    private BluetoothAdapter mBluetoothAdapter;
     private TextView txt_room_sucess,txt_room_fail,txt_room_wait;
     private LinearLayout lyt_sucess,lyt_progress,lyt_fail;
     private RelativeLayout lyt_bottom_circular;
     private ImageView iv_status,iv_bg;
    private Handler mainHandler;
    private Handler handler;
    private Handler mChangeScreenHandler;
    private Vibrator vibrator;
    private ProgressDialog dialog;
    private ProgressBar mTimer_Progressbar;
    private ProgressBarAnimation anim;
    private String roomNumber;
    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doormakaba_unlock);

        lyt_sucess=  findViewById(R.id.lyt_sucess);
        lyt_progress=  findViewById(R.id.lyt_progress);
        lyt_fail=  findViewById(R.id.lyt_fail);

   /*     txt_room_sucess=  findViewById(R.id.txt_room_sucess);
        txt_room_fail=  findViewById(R.id.txt_room_fail);
        txt_room_wait=  findViewById(R.id.txt_room_wait);*/

        mTimer_Progressbar=  findViewById(R.id.timer_progressbar);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }

         /*   // Prompt for permission
           if(  !hasLocationPermissions()){

               statusCheck();
           }
*/

initSdk();
            getPermissions();
           /* if (!mManager.isRegisteredToBackend()) {
                redirectToStep1();
            }*/


            Intent intent = getIntent();
            roomNumber=intent.getStringExtra("room_no");
            if (intent.hasExtra(EXTRA_PUSH) && intent.getBooleanExtra(EXTRA_PUSH, false)) {
                // received intent that signaled a push notification
                log("Application started from notification, handling push message");
                mManager.handlePushMessage(intent);
            }

        } catch (LegicMobileSdkException e) {
            log(e.getLocalizedMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!sharedPreferences.getBoolean("isSyncComplete", false)) {
            mManager.synchronizeWithBackend();
            dialog = new ProgressDialog(this);
            dialog.setMessage("please wait while we Sycnchronise with backend...");
            dialog.setCancelable(false);
            dialog.show();
        }else{
            dialog = new ProgressDialog(this);
            dialog.setMessage("please wait while we activate your keys..");
            dialog.setCancelable(false);
            dialog.show();

            try {
                activateFile(mManager.getAllFiles());
            } catch (LegicMobileSdkException e) {
                e.printStackTrace();
            }
        }

    }

    public static String[] getPermissions() {

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ?
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION} :
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
        } else {
            return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT};
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        } else {
            return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
        }
    }


    //-----------------------------------------------------------------------------------------------------------------| 
    public void activateFile(List<LegicNeonFile> allFiles) {
        try {

            for (int i = 0; i < allFiles.size(); i++) {

                 LegicNeonFile f = allFiles.get(i);

                try {
                    Log.d("file state", String.valueOf(f.getFileState()));
                    if(f.getFileState()== LegicNeonFileState.DEPLOYED) {
                        mManager.activateFile(f);
                        // Toast.makeText(this,f.getDisplayName(),Toast.LENGTH_LONG).show();
                        mManager.setDefault(f, LegicNeonFileDefaultMode.LC_PROJECT_DEFAULT, true);


                        mainHandler = new Handler(getMainLooper());
                        // This is your code
                        Runnable myRunnable = () -> {
                            dialog.dismiss();
                            lyt_progress.setVisibility(View.VISIBLE);
                            handler = new Handler();
                            handler.postDelayed(() -> {
                                finish();
                            }, 20000);

                            anim = new ProgressBarAnimation(mTimer_Progressbar, 100, 0);
                            anim.setDuration(20000);
                            mTimer_Progressbar.startAnimation(anim);
                        };
                        mainHandler.post(myRunnable);
                    }
                } catch (LegicMobileSdkException e) {

                Handler    handler = new Handler(getMainLooper());
                    // This is your code
                    Runnable myRunnable = () -> {
                        dialog.dismiss();
                        GlobalClass.ShowAlet(this, "Alert!!","key activation failed");
                    };
                    handler.post(myRunnable);
                    e.getLocalizedMessage();

                }
            }
            dialog.dismiss();
        } catch (Exception e) {
            handler = new Handler(getMainLooper());
            handler.postDelayed(() -> {
                dialog.dismiss();
                GlobalClass.ShowAlet(this, "Alert!!","key activation failed");
            }, 10);

            e.printStackTrace();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------|
    public void deactivateAllFiles() {

            try {
                List<LegicNeonFile> files = mManager.getAllFiles();

                for (LegicNeonFile f : files) {
                    try {
                        mManager.deactivateFile(f);
                    } catch (LegicMobileSdkException e) {
                        log(e.getLocalizedMessage());
                    }
                }
            } catch (LegicMobileSdkException e) {
                GlobalClass.ShowAlet(this, "Alert!!","key deactivation failed");

                log(e.getLocalizedMessage());
            }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerLcMessageEvent(byte[] data, LcMessageMode lcMessageMode, RfInterface rfInterface) {
        super.readerLcMessageEvent(data, lcMessageMode, rfInterface);
        try {
            mChangeScreenHandler = new Handler(getMainLooper());
            // This is your code
            Runnable myRunnable = () -> {

                BLEDataHandler dataHandler = new BLEDataHandler(data);

                if (dataHandler.isAccessGranted()) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(200);
                    }
                    lyt_progress.setVisibility(View.GONE);
                    lyt_sucess.setVisibility(View.VISIBLE);
                    lyt_fail.setVisibility(View.GONE);
                } else {
                    lyt_progress.setVisibility(View.GONE);
                    lyt_sucess.setVisibility(View.GONE);
                    lyt_fail.setVisibility(View.VISIBLE);
                }
            };
            mChangeScreenHandler.post(myRunnable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void backendSynchronizeDoneEvent(LegicMobileSdkStatus status) {
        if (status.isSuccess()) {
            try {
                sharedPreferences.edit().putBoolean("isSyncComplete",true).commit();
                mChangeScreenHandler = new Handler(getMainLooper());
                // This is your code
                Runnable myRunnable = () -> {
                    dialog.setMessage("please wait while we activate your keys..");
                    try {
                        activateFile(mManager.getAllFiles());
                    } catch (LegicMobileSdkException e) {
                        e.printStackTrace();
                    }
                };
                mChangeScreenHandler.post(myRunnable);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            mChangeScreenHandler = new Handler(getMainLooper());
            // This is your code
            Runnable myRunnable = () -> {
                dialog.dismiss();
                GlobalClass.ShowAlet(this, "Alert!!","Synchronization failed");

            };
            mChangeScreenHandler.post(myRunnable);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            deactivateAllFiles();

            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            if(mChangeScreenHandler!=null){
                mChangeScreenHandler.removeCallbacks(null);
            }

            if (mainHandler != null) {
                mainHandler.removeCallbacks(null);
            }

            if(anim!=null){
                anim.cancel();
            }

            if(dialog!=null && dialog.isShowing()){
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasRequiredPermissions()
    {

        boolean permissionGranted = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionGranted &= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
            return permissionGranted;
        }

        permissionGranted &= ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionGranted &= ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        return permissionGranted;
    }


}


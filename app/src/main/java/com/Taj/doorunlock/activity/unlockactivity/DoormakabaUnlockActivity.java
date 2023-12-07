// -----------------------------------------------------------------------------
// Copyright© 2017 LEGIC® Identsystems AG, CH-8623 Wetzikon
// Confidential. All rights reserved!
// -----------------------------------------------------------------------------

package com.taj.doorunlock.activity.unlockactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.legic.mobile.sdk.api.exception.SdkException;
import com.legic.mobile.sdk.api.listener.BackendEventListener;
import com.legic.mobile.sdk.api.listener.LcMessageEventListener;
import com.legic.mobile.sdk.api.listener.NeonFileEventListener;
import com.legic.mobile.sdk.api.listener.ReaderEventListener;
import com.legic.mobile.sdk.api.listener.SdkEventListener;
import com.legic.mobile.sdk.api.types.AddressingMode;
import com.legic.mobile.sdk.api.types.LcMessageMode;

import com.legic.mobile.sdk.api.types.NeonFile;
import com.legic.mobile.sdk.api.types.NeonFileDefaultMode;
import com.legic.mobile.sdk.api.types.NeonFileState;
import com.legic.mobile.sdk.api.types.ReaderFoundReport;
import com.legic.mobile.sdk.api.types.RfInterface;

import com.legic.mobile.sdk.api.types.RfInterfaceState;
import com.legic.mobile.sdk.api.types.SdkStatus;
import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.helper.ProgressBarAnimation;
import com.taj.doorunlock.unlock.doormakaba.BLEDataHandler;
import com.taj.doorunlock.unlock.doormakaba.BaseActivity;
import com.taj.doorunlock.unlock.doormakaba.Settings;
import com.taj.doorunlock.unlock.doormakaba.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.legic.mobile.sdk.api.types.NeonFileDefaultMode.LC_PROJECT_DEFAULT;
import static com.taj.doorunlock.helper.GlobalClass.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class DoormakabaUnlockActivity extends BaseActivity implements BackendEventListener, LcMessageEventListener, SdkEventListener {

    // this code can be any number, it is just a "marker"
    public static final String EXTRA_PUSH = "PushReceived";
    private BluetoothAdapter mBluetoothAdapter;
    private TextView txt_room_sucess, txt_room_fail, txt_room_wait;
    private LinearLayout lyt_sucess, lyt_progress, lyt_fail;
    private RelativeLayout lyt_bottom_circular;
    private ImageView iv_status, iv_bg;
    private Handler mainHandler;
    private Handler handler;
    private Handler mChangeScreenHandler;
    private Vibrator vibrator;
    private ProgressDialog dialog;
    private ProgressBar mTimer_Progressbar;
    private ProgressBarAnimation anim;
    private String roomNumber;

    private BackendEventListener mBackendEventListener;



    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doormakaba_unlock);

        lyt_sucess = findViewById(R.id.lyt_sucess);
        lyt_progress = findViewById(R.id.lyt_progress);
        lyt_fail = findViewById(R.id.lyt_fail);

        try {
            initSdk();
        } catch (SdkException e) {
            throw new RuntimeException(e);
        }
        updateInterfaceStatus();
   /*     txt_room_sucess=  findViewById(R.id.txt_room_sucess);
        txt_room_fail=  findViewById(R.id.txt_room_fail);
        txt_room_wait=  findViewById(R.id.txt_room_wait);*/

        mTimer_Progressbar = findViewById(R.id.timer_progressbar);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        try {
            mManager.registerForBackendEvents(this);
            mManager.registerForLcMessageEvents(this);
            mManager.registerForSdkEvents(this);
        } catch (SdkException e) {
            throw new RuntimeException(e);
        }


        try {
            //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Log.d("permissions1","granted");
                Log.d("permissions2","granted");
            String[] requiredPermissions;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requiredPermissions = new String[]{
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.ACCESS_FINE_LOCATION
                };
            } else {
                requiredPermissions = new String[]{
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.ACCESS_FINE_LOCATION
                };
            }

            List<String> missingPermissions = new ArrayList<>();

            boolean allPermissionsOk = true;
            for (String p : requiredPermissions) {
                //Log.d(LOG, "checking Permission: " + p);
                if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                    //Log.d(LOG, "missing Permission: " + p);
                    allPermissionsOk = false;
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                        //Log.d(LOG, "User already denied permission once, he probably needs an explanation: " + p);
                    }
                    missingPermissions.add(p);
                    Log.d("missingPermissions",p);
                }
            }

            if (!allPermissionsOk) {
                // request all missing permissions
                ActivityCompat.requestPermissions(this, missingPermissions.toArray(new String[0]),
                        /* unused because no callback*/ 0);
            }
                //mBluetoothAdapter.enable();

         /*   // Prompt for permission
           if(  !hasLocationPermissions()){

               statusCheck();
           }
*/

            //initSdk();
            //getPermissions();
           /* if (!sdkManager.isRegisteredToBackend()) {
                redirectToStep1();
            }*/


            Intent intent = getIntent();
            //roomNumber=intent.getStringExtra("room_no");
            if (intent.hasExtra(EXTRA_PUSH) && intent.getBooleanExtra(EXTRA_PUSH, false)) {
                // received intent that signaled a push notification
                //log("Application started from notification, handling push message");
                mManager.handlePushMessage(intent);
            }


        } catch (SdkException e) {
            //log(e.getLocalizedMessage());
        }
    }

    @SuppressLint("LongLogTag")
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
            Log.d("please wait while we activate your keys..","true");
            dialog = new ProgressDialog(this);
            dialog.setMessage("please wait while we activate your keys..");
            dialog.setCancelable(false);
            dialog.show();

            try {
                activateFile(mManager.getAllNeonFiles());
            } catch (SdkException e) {
                e.printStackTrace();
            }
        }

    }



    //-----------------------------------------------------------------------------------------------------------------| 
    public void activateFile(List<NeonFile> allFiles) {
        try {
            Log.d("activateFilesSize", String.valueOf(allFiles.size()));
            for (int i = 0; i < allFiles.size(); i++) {

                 NeonFile f = allFiles.get(i);
                 Log.d("fileState", String.valueOf(f));

                /*int fileIndex = 1;

                for (NeonFile f1 : allFiles) {
                    String fileInfos = "Index: " + fileIndex++;
                    fileInfos += "\nState: " + f.getFileState().toString();

                    byte[] fileId = f1.getFileId();
                    if (fileId.length > 0) {
                        fileInfos += "\nFile Id: " + Utils.dataToByteString(f.getFileId());
                        for (String key : f.getMetaData().keySet()) {
                            fileInfos += "\n" + key + ": " + f.getMetaData().get(key).getStringValue();
                        }
                    }
                    Log.d("fileInfos",fileInfos);
                }*/

                try {
                    if(f.getFileState()== NeonFileState.DEPLOYED) {
                        Log.d("DeployedKey",Utils.dataToByteString(f.getFileId()));
                        mManager.setRfInterfaceActive(Settings.USE_HW_INTERFACE, true);
                        mManager.setNeonFileActive(f, true);
                        mManager.setNeonFileDefaultActive(f, LC_PROJECT_DEFAULT, true);

                        mainHandler = new Handler(getMainLooper());// This is your code
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
                } catch (SdkException e) {

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

            /*try {
                List<NeonFile> files = sdkManager.getAllNeonFiles();

                for (NeonFile f : files) {
                    try {
                        sdkManager.setNeonFileActive(f,false);
                    } catch (SdkException e) {
                        //log(e.getLocalizedMessage());
                    }
                }
            } catch (SdkException e) {
                GlobalClass.ShowAlet(this, "Alert!!","key deactivation failed");

                //log(e.getLocalizedMessage());
            }*/
        try {
            List<NeonFile> files = mManager.getAllNeonFiles();

            for (NeonFile f : files) {
                if (f.isActivated()) {
                    mManager.setNeonFileActive(f, false);
                }

                if(mManager.isNeonFileDefaultActive(f, LC_PROJECT_DEFAULT)) {
                    mManager.setNeonFileDefaultActive(f, LC_PROJECT_DEFAULT, false);
                }
            }

            if (mManager.isRfInterfaceActive(Settings.USE_HW_INTERFACE)) {
                mManager.setRfInterfaceActive(Settings.USE_HW_INTERFACE, false);
            }
        } catch (SdkException e) {
            //log(e.getLocalizedMessage());
            Log.d("errorText",e.getLocalizedMessage());
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    /*@Override
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
    }*/




    /*@Override
    public void backendSynchronizeDoneEvent(SdkStatus status) {
        if (status.isSuccess()) {
            try {
                sharedPreferences.edit().putBoolean("isSyncComplete",true).commit();
                mChangeScreenHandler = new Handler(getMainLooper());
                // This is your code
                Runnable myRunnable = () -> {
                    dialog.setMessage("please wait while we activate your keys..");
                    try {
                        activateFile(sdkManager.getAllNeonFiles());
                    } catch (SdkException e) {
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
    }*/
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



    @Override
    public void backendRegistrationInitializedEvent(SdkStatus sdkStatus) {

    }

    @Override
    public void backendRegistrationFinishedEvent(SdkStatus sdkStatus) {

    }

    @Override
    public void backendUnregisteredEvent(SdkStatus sdkStatus) {

    }

    @Override
    public void backendSynchronizeStartEvent() {
        Log.d("Synchronize started","yes");
        if (mBackendEventListener != null) {
            mBackendEventListener.backendSynchronizeStartEvent();
        }

    }

    @Override
    public void backendSynchronizeDoneEvent(SdkStatus sdkStatus) {
        Log.d("acc","cc");
        if (sdkStatus.isSuccess()) {
            try {
                sharedPreferences.edit().putBoolean("isSyncComplete",true).apply();
                mChangeScreenHandler = new Handler(getMainLooper());
                // This is your code
                Runnable myRunnable = () -> {
                    dialog.setMessage("please wait while we activate your keys..");
                    try {
                        activateFile(mManager.getAllNeonFiles());
                    } catch (SdkException e) {
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
    public void readerLcMessageEvent(byte[] data, LcMessageMode lcMessageMode, RfInterface rfInterface) {
        Log.d("readerLcMessageEvent","yes");
        Log.d("LC message event data: " , Utils.dataToByteString(data) + " mode: " + lcMessageMode
                + " on interface " + rfInterface);
        String receivedMessageFromLock = "";
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
                    Log.d("ReaderReport", "Interpreted Message from Lock:\n" + dataHandler.getInterpretedMessageString(this) + "\n");
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

    @SuppressLint("LongLogTag")
    @Override
    public void readerLcMessagePollingEvent(LcMessageMode lcMessageMode, RfInterface rfInterface) {
        Log.d("readerLcMessagePollingEvent" , lcMessageMode  + " on interface " + rfInterface);
    }

    @Override
    public void readerAddedLcMessageEvent(int i, RfInterface rfInterface) {

    }

    @Override
    public void readerPasswordRequestEvent(byte[] bytes, RfInterface rfInterface) {

    }
// SDKEventListener
    @Override
    public void rfInterfaceActivatedEvent(long l, AddressingMode addressingMode, RfInterface rfInterface) {
        Log.d("Interface activated" ,+ l + " mode: " + addressingMode
                + " interface:" + rfInterface);
        updateInterfaceStatus();
    }

    protected void updateInterfaceStatus() {
        runOnUiThread(() -> {
            try {
                if (mManager != null && mManager.isRegisteredToBackend()) {
                    mManager.isRfInterfaceHardwareEnabled(Settings.USE_HW_INTERFACE);
                }

            } catch (SdkException e) {
                Log.d("Exception: " , e.getLocalizedMessage());
            }
        });
    }


    @Override
    public void rfInterfaceDeactivatedEvent(long l, AddressingMode addressingMode, RfInterface rfInterface) {
        Log.d("Interface deactivated" , l + " mode: " + addressingMode
                + " interface:" + rfInterface);
        updateInterfaceStatus();
    }

    @Override
    public void rfInterfaceChangeEvent(RfInterface rfInterface, RfInterfaceState rfInterfaceState) {
        Log.d("State change" , rfInterface +  " \nNew state: " + rfInterfaceState);
        updateInterfaceStatus();
    }
}


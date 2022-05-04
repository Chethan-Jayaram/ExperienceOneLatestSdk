// -----------------------------------------------------------------------------
// Copyright© 2017 LEGIC® Identsystems AG, CH-8623 Wetzikon
// Confidential. All rights reserved!
// -----------------------------------------------------------------------------

package com.taj.doorunlock.unlock.doormakaba;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.legic.mobile.sdk.api.LegicMobileSdkManager;
import com.legic.mobile.sdk.api.exception.LegicMobileSdkException;
import com.legic.mobile.sdk.api.listener.LegicMobileSdkEventListener;
import com.legic.mobile.sdk.api.listener.LegicMobileSdkPasswordEventListener;
import com.legic.mobile.sdk.api.listener.LegicMobileSdkRegistrationEventListener;
import com.legic.mobile.sdk.api.listener.LegicMobileSdkSynchronizeEventListener;
import com.legic.mobile.sdk.api.listener.LegicNeonFileEventListener;
import com.legic.mobile.sdk.api.listener.LegicReaderEventListener;
import com.legic.mobile.sdk.api.types.LcMessageMode;
import com.legic.mobile.sdk.api.types.LegicMobileSdkErrorReason;
import com.legic.mobile.sdk.api.types.LegicMobileSdkFileAddressingMode;
import com.legic.mobile.sdk.api.types.LegicMobileSdkStatus;
import com.legic.mobile.sdk.api.types.LegicNeonFile;
import com.legic.mobile.sdk.api.types.RfInterface;
import com.legic.mobile.sdk.api.types.RfInterfaceState;

import java.util.ArrayList;
import java.util.List;

/**
 * Base Activity that implements all event listeners.
 */
public abstract class BaseActivity extends AppCompatActivity implements LegicMobileSdkSynchronizeEventListener,
        LegicMobileSdkRegistrationEventListener, LegicReaderEventListener, LegicNeonFileEventListener,
        LegicMobileSdkPasswordEventListener, LegicMobileSdkEventListener {

    private static final String LOG = "LEGIC-SDK-QUICKSTART";

    protected LegicMobileSdkManager mManager;

    protected LinearLayout logView;
    protected ScrollView logScrollView;


    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //checkPermissions();
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void onResume() {
        super.onResume();
        registerListeners();
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void onPause() {
        super.onPause();
        unregisterListeners();
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void backendFileChangedEvent(LegicNeonFile legicFile) {
        log("File changed -> file " + legicFile);
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void backendRequestAddFileDoneEvent(LegicMobileSdkStatus legicSdkStatus) {
        if (legicSdkStatus.isSuccess()) {
            log("Backend Request add file done with status " + legicSdkStatus);
        } else {
            handleSdkErrors(legicSdkStatus);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void backendRequestRemoveFileDoneEvent(LegicMobileSdkStatus status) {
        if (status.isSuccess()) {
            log("Backend Request remove file done with status " + status);
        } else {
            handleSdkErrors(status);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void readerReadFileEvent(LegicNeonFile legicFile, RfInterface rfInterface) {
        log("Reader read Event on file  " + legicFile + " on interface " + rfInterface);

        byte[] messageData = new byte[]{0, 1, 1};
        try {
            mManager.sendLcMessage(messageData, LcMessageMode.ENCRYPTED_MACED_FILE_KEYS, rfInterface);
        } catch (LegicMobileSdkException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void readerWriteFileEvent(LegicNeonFile legicFile, RfInterface rfInterface) {
        log("Reader write Event on file  " + legicFile + " on interface " + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void readerLcMessageEvent(byte[] data, LcMessageMode lcMessageMode, RfInterface rfInterface) {
        log("LC message event data: "+ Utils.dataToByteString(data) + " mode: " + lcMessageMode
                + " on interface " + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void readerLcMessagePollingEvent(LcMessageMode lcMessageMode, RfInterface rfInterface) {
        log("LC message polling event, mode: " + lcMessageMode  + " on interface " + rfInterface);

    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void readerConnectEvent(long Id, LegicMobileSdkFileAddressingMode mode, int readerType,
                                   RfInterface rfInterface) {
        log("Reader connect event, id : " + Id + "/" + mode
                + " Reader Type: " + readerType + " interface:" + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void backendRegistrationStartDoneEvent(LegicMobileSdkStatus status) {
        if (status.isSuccess()) {
            log("Registration Step 1 done with status " + status);
        } else {
            handleSdkErrors(status);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void backendRegistrationFinishedDoneEvent(LegicMobileSdkStatus status) {
        if (status.isSuccess()) {
            log("Registration Step 2 done with status " + status);
        } else {
            handleSdkErrors(status);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void backendUnregisterDoneEvent(LegicMobileSdkStatus status) {
        if (status.isSuccess()) {
            log("Unregister done with status " + status);
        } else {
            handleSdkErrors(status);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendSynchronizeStartEvent() {
        log("Synchronize started");
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendSynchronizeDoneEvent(LegicMobileSdkStatus status) {
        if (status.isSuccess()) {
            log("Synchronize done with status " + status);
        } else {
            handleSdkErrors(status);
        }
    }


    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void readerPasswordRequestEvent(byte[] data, RfInterface rfInterface) {
        log("Password request with bytes " + Utils.dataToByteString(data) + " interface:" + rfInterface);
    }


    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void sdkActivatedEvent(long identifier, LegicMobileSdkFileAddressingMode mode,
                                  RfInterface rfInterface) {
        log("Interface activated, Identifier: " + identifier + " mode: " + mode
                + " interface:" + rfInterface);
       // updateInterfaceStatus();
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void sdkDeactivatedEvent(long identifier, LegicMobileSdkFileAddressingMode mode,
                                    RfInterface rfInterface) {
        log("Interface deactivated, Identifier: " + identifier + " mode: " + mode
                + " interface:" + rfInterface);
       // updateInterfaceStatus();
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    @Override
    public void sdkRfInterfaceChangeEvent(RfInterface rfInterface,
                                          RfInterfaceState rfInterfaceState) {
        log("Interface changed interface:" + rfInterface +  " new state: " + rfInterfaceState);
       // updateInterfaceStatus();
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    public void clearLog(View view) {
        logView.removeAllViews();
    }

 /*   //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            onAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    //-----------------------------------------------------------------------------------------------------------------|
    private void onAbout() {
        StringBuilder msg = new StringBuilder();

        try {
            msg.append("----------------------------------");
            msg.append("\nSDK version: ");
            msg.append(mManager.getVersion());
            msg.append("\n----------------------------------");
            msg.append("\nregistered:  ");
            msg.append(mManager.isRegisteredToBackend());
            msg.append("\nBLE Role:    ");
            msg.append(mManager.getCurrentBleRole());
            msg.append("\nBLE Supported: ");
            msg.append(mManager.isRfInterfaceSupported(RfInterface.BLE));
            msg.append("\nBLE Enabled:  ");
            msg.append(mManager.isRfInterfaceHardwareEnabled(RfInterface.BLE));
            msg.append("\nBLE Active:   ");
            msg.append(mManager.isRfInterfaceActive(RfInterface.BLE));
            msg.append("\nDevice Id:   ");
            msg.append(mManager.isRfInterfaceActive(RfInterface.BLE));
            msg.append("\n----------------------------------\n");
            //updateInterfaceStatus();
        } catch(LegicMobileSdkException e) {
            msg.append("Exception during SDK call: ");
            msg.append(e.getLocalizedMessage());
        }

        log(msg.toString());
    }


    //-----------------------------------------------------------------------------------------------------------------| 
    protected void initSdk() throws LegicMobileSdkException {
        mManager = Utils.getSdkManager(getApplicationContext());

        registerListeners();

        if (!mManager.isStarted()) {
            mManager.start(Settings.mobileAppId, Settings.mobileAppTechUser, Settings.mobileAppTechPassword,
                    Settings.serverUrl);
        }

        mManager.setLcProjectAddressingMode(true);

        if (mManager.isRegisteredToBackend()) {
            if (mManager.isRfInterfaceSupported(RfInterface.BLE) && !mManager.isRfInterfaceActive(RfInterface.BLE)) {
                mManager.activateRfInterface(RfInterface.BLE);
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------| 
    protected void log(final String... inputLines) {
       /* for (String input : inputLines) {
                    Log.v(LOG, input);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (String input : inputLines) {
                    addLineToLogView(input);
                }

                // Insert empty line between log messages
                addLineToLogView("");
            }
        });*/
    }

/*
    //-----------------------------------------------------------------------------------------------------------------|
    protected void updateInterfaceStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (bleStatus != null) setBleStatus(mManager.isRfInterfaceActive(RfInterface.BLE));
                } catch (LegicMobileSdkException e) {
                    log("Exception: " + e.getLocalizedMessage());
                }
            }
        });
    }
*/

 /*   //-----------------------------------------------------------------------------------------------------------------|
    private void setBleStatus(boolean enabled) {
        bleStatus.setText(getString(R.string.ble_status_msg, (enabled?"ON":"OFF")));
    }*/

    //-----------------------------------------------------------------------------------------------------------------|
    private void addLineToLogView(String input) {
        TextView logText = new TextView(this);
        logText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        logText.setText(input);

      //  logView.addView(logText);

        scrollLogView();
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void scrollLogView() {
        logScrollView.post(new Runnable() {
            @Override
            public void run() {
                logScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void checkPermissions() {

        String[] requiredPermissions = new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        List<String> missingPermissions = new ArrayList<>();

        boolean allPermissionsOk = true;
        for(String p : requiredPermissions) {
            Log.d(LOG, "checking Permission: " + p);
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG, "missing Permission: " + p);
                allPermissionsOk = false;
                if  (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                    Log.d(LOG, "User already denied permission once, he probably needs an explanation: " + p);
                }
                missingPermissions.add(p);
            }
        }

        if(!allPermissionsOk) {
            // request all missing permissions
            ActivityCompat.requestPermissions(this, missingPermissions.toArray(new String[0]),
                    /* unused because no callback*/ 0);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void handleSdkErrors(LegicMobileSdkStatus status) {
        // this method logs only when status is not "OK"
        if (!status.isSuccess()) {

            // LegicMobileSdkErrorReason gives more insight about the cause
            LegicMobileSdkErrorReason reason = status.getReason();

            log("An action failed with the following error: " + status.getError().name());
            switch(reason.getReasonType()) {
                case SDK_ERROR:
                    log("SDK internal error:\n" +
                            "You probably tried actions that are not allowed (unsupported interfaces, " +
                            "activation of non-deployed files, invalid data).");

                    log("SDK error code: " + reason.getSdkErrorCode());
                    break;
                case BACKEND_ERROR:
                    log("Backend error:\n" +
                            "This is usually caused by invalid configuration data (invalid mobileAppId), " +
                            "incorrect requests (wrong state, not registered) or by problems on the backend system.");

                    log("Back-end error code (LEGIC Connect): " + reason.getErrorCode());
                    break;
                case HTTP_ERROR:
                    log("HTTP error:\n" +
                            "This could be caused by connection or authentication problems, please check " +
                            "your configuration and/or your network settings.");

                    log("HTTP Error code: " + reason.getErrorCode());
                    break;
                default:
                    log("Unknown error reason: " + reason.toString());
            }
            log("Full error description:\n"+ reason);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void registerListeners() {
        if (mManager == null) {return;}
        try {
            mManager.registerForSynchronizeEvents(this);
            mManager.registerForRegistrationEvents(this);
            mManager.registerForReaderEvents(this);
            mManager.registerForFileEvents(this);
            mManager.registerForPasswordEvents(this);
            mManager.registerForSdkEvents(this);
        } catch(LegicMobileSdkException e) {
            log("Could not register listener: " + e.getLocalizedMessage());
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void unregisterListeners() {
        if (mManager == null) {return;}
        mManager.unregisterAnyEvents(this);
    }
}


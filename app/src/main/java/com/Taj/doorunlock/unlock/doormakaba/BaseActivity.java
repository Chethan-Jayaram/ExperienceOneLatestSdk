// -----------------------------------------------------------------------------
// Copyright© 2019 LEGIC® Identsystems AG, CH-8623 Wetzikon
// Confidential. All rights reserved!
// -----------------------------------------------------------------------------

package com.taj.doorunlock.unlock.doormakaba;

import static com.taj.doorunlock.helper.GlobalClass.mManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.legic.mobile.sdk.api.LegicMobileSdkManager;
import com.legic.mobile.sdk.api.exception.SdkException;
import com.legic.mobile.sdk.api.listener.BackendEventListener;
import com.legic.mobile.sdk.api.listener.LcMessageEventListener;
import com.legic.mobile.sdk.api.listener.NeonFileEventListener;
import com.legic.mobile.sdk.api.listener.ReaderEventListener;
import com.legic.mobile.sdk.api.listener.SdkEventListener;
import com.legic.mobile.sdk.api.types.AddressingMode;
import com.legic.mobile.sdk.api.types.LcMessageMode;
import com.legic.mobile.sdk.api.types.NeonFile;
import com.legic.mobile.sdk.api.types.NeonSubFile;
import com.legic.mobile.sdk.api.types.ReaderFoundReport;
import com.legic.mobile.sdk.api.types.RfInterface;
import com.legic.mobile.sdk.api.types.RfInterfaceState;
import com.legic.mobile.sdk.api.types.SdkErrorReason;
import com.legic.mobile.sdk.api.types.SdkStatus;
import com.taj.doorunlock.R;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.unlock.Taj;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Base Activity that implements all common activity functions.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        BackendEventListener, ReaderEventListener, NeonFileEventListener,
        SdkEventListener, LcMessageEventListener {

    private static final String LOG = "LEGIC-SDK-QUICKSTART";
    protected LegicMobileSdkManager mManager;
    protected LinearLayout logView;
    protected ScrollView logScrollView;
    protected TextView bleStatus;
    protected EditText fileIndex;
    protected TextView mobileAppIdTextView;
    protected TextView bleDeviceIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterListeners();
    }

   /* public void clearLog(View view) {
        logView.removeAllViews();
    }*/

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            onAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onAbout() {
        StringBuilder msg = new StringBuilder();
        try {
            msg.append("----------------------------------");
            msg.append("\nSDK version: ");
            msg.append(mManager.getVersion());
            msg.append("\n----------------------------------");
            msg.append("\nRegistered:  ");
            msg.append(mManager.isRegisteredToBackend());
            msg.append("\nBLE Supported: ");
            msg.append(mManager.isRfInterfaceHardwareSupported(Settings.USE_HW_INTERFACE));
            msg.append("\nBLE Enabled: ");
            msg.append(mManager.isRfInterfaceHardwareEnabled(Settings.USE_HW_INTERFACE));
            msg.append("\nRF Interface Activate: ");
            msg.append(mManager.isRfInterfaceActive(Settings.USE_HW_INTERFACE));
            msg.append("\n----------------------------------\n");
            updateInterfaceStatus();
        } catch (SdkException e) {
            msg.append("Exception during SDK call: ");
            msg.append(e.getLocalizedMessage());
        }

        //log(msg.toString());
    }

    @SuppressLint("LongLogTag")
    protected void registerListeners() {
        if (mManager != null) {
            try {
                mManager.registerForBackendEvents(this);
                mManager.registerForReaderEvents(this);
                mManager.registerForNeonFileEvents(this);
                mManager.registerForLcMessageEvents(this);
                mManager.registerForSdkEvents(this);
            } catch (SdkException e) {
                Log.d("Could not register listener: " , e.getLocalizedMessage());
            }
        }
    }

    protected void unregisterListeners() {
        if (mManager == null) {
            return;
        }
        mManager.unregisterAnyEvents(this);
    }

    protected void initSdk() throws SdkException {
        mManager = Utils.getSdkManager(getApplicationContext());
        registerListeners();

        if (!mManager.isStarted()) {
            mManager.start(Settings.mobileAppId, Settings.mobileAppTechUser, Settings.mobileAppTechPassword,
                    Settings.serverUrl);
        }

        if (mManager.isRegisteredToBackend()) {
            if (mManager.isRfInterfaceHardwareSupported(Settings.USE_HW_INTERFACE) && !mManager.isRfInterfaceActive(Settings.USE_HW_INTERFACE)) {
                mManager.setLcProjectAddressingModeActive(true);
                mManager.setRfInterfaceActive(Settings.USE_HW_INTERFACE, true);
            }
        }
    }


    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }


    /*protected void log(final String... inputLines) {
        for (String input : inputLines) {
            Log.v(LOG, input);
        }

        runOnUiThread(() -> {
            for (String input : inputLines) {
                addLineToLogView(input);
            }

            // Insert empty line between log messages
            addLineToLogView("");
        });
    }*/


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


    /*private void setBleStatus(boolean enabled) {
        if (bleStatus != null) {
            bleStatus.setText(getString(R.string.ble_status_msg, (enabled ? "ON" : "OFF")));
        } else {
            bleStatus.setText(getString(R.string.ble_status_msg, "UNKNOWN"));
        }
    }

    // Shown the BLE status as UNKNOWN when there is an exception or when the status is null
    private void unknownBleStatus() {
        if (bleStatus != null) {
            bleStatus.setText(getString(R.string.ble_status_msg, "UNKNOWN"));
        }
    }*/


    private void addLineToLogView(String input) {
        TextView logText = new TextView(this);
        logText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        logText.setText(input);

        logView.addView(logText);

        scrollLogView();
    }


    private void scrollLogView() {
        logScrollView.post(() -> logScrollView.fullScroll(ScrollView.FOCUS_DOWN));
    }


    private void checkPermissions() {
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
            Log.d(LOG, "checking Permission: " + p);
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG, "missing Permission: " + p);
                allPermissionsOk = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                    Log.d(LOG, "User already denied permission once, he probably needs an explanation: " + p);
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
    }


    private void handleSdkErrors(SdkStatus status) {
        // this method logs only when status is not "OK"
        if (!status.isSuccess()) {

            // LegicMobileSdkErrorReason gives more insight about the cause
            SdkErrorReason reason = status.getReason();

            //log("An action failed with the following error: " + status.getError().name());
            switch (reason.getReasonType()) {
                case SDK_ERROR:
                    /*log("SDK internal error:\n" +
                            "You probably tried actions that are not allowed (unsupported interfaces, " +
                            "activation of non-deployed files, invalid data).");*/

                    //log("SDK error code: " + reason.getSdkErrorCode());
                    break;
                case BACKEND_ERROR:
                    /*log("Backend error:\n" +
                            "This is usually caused by invalid configuration data (invalid mobileAppId), " +
                            "incorrect requests (wrong state, not registered) or by problems on the backend system.");

                    log("Back-end error code (LEGIC Connect): " + reason.getErrorCode());*/
                    break;
                case HTTP_ERROR:
                    /*log("HTTP error:\n" +
                            "This could be caused by connection or authentication problems, please check " +
                            "your configuration and/or your network settings.");*/

                   // log("HTTP Error code: " + reason.getErrorCode());
                    break;
                default:
                    //log("Unknown error reason: " + reason);
            }
            //log("Full error description:\n" + reason);
        }
    }

    @Override
    public void readerAddedLcMessageEvent(int i, RfInterface rfInterface) {
        Log.d("Added Lc Message " , String.valueOf(i));
    }

    @SuppressLint("LongLogTag")
    @Override
    public void backendNeonFileChangedEvent(NeonFile neonFile) {
        Log.d("Neon File changed -> file " , neonFile.toString());
    }

    @SuppressLint("LongLogTag")
    @Override
    public void backendRequestAddNeonFileDoneEvent(SdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            Log.d("Successfully Added Neon File","");
        } else {
            handleSdkErrors(sdkStatus);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void backendRequestRemoveNeonFileDoneEvent(SdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            Log.d("Successfully Removed Neon File","");
        } else {
            handleSdkErrors(sdkStatus);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void readerReadNeonFileEvent(NeonFile neonFile, RfInterface rfInterface) {
        Log.d("Reader read event on file  ", neonFile + " on interface " + rfInterface);

        byte[] messageData = new byte[]{0, 1, 1};
        try {
            if (rfInterface != null) {
                //Send information to the lock's reader
                mManager.sendLcMessageToReader(messageData, LcMessageMode.ENCRYPTED_MACED_FILE_KEYS, rfInterface);
            }
        } catch (SdkException e) {
            Log.d("Reader read event on file  " , neonFile + " failed with error : " + e);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void readerWriteNeonFileEvent(NeonFile neonFile, RfInterface rfInterface) {
        Log.d("Reader write event on file  " , neonFile + " on interface " + rfInterface);
    }

    @Override
    public void readerReadNeonSubFileEvent(NeonSubFile neonSubFile, NeonFile neonFile, RfInterface rfInterface) {
        Log.d("Read Subfile Event","");
    }

    @Override
    public void readerWriteNeonSubFileEvent(NeonSubFile neonSubFile, NeonFile neonFile, RfInterface rfInterface) {
        Log.d("Write Subfile Event","");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void readerConnectEvent(long identifier, AddressingMode addressingMode, int type, UUID uuid, RfInterface rfInterface) {
        Log.d("readerUuid",uuid.toString());
        Log.d("Reader connect event, id : " , identifier + "/" + addressingMode.name()
                + " Reader Type: " + type + " interface:" + rfInterface);
    }

    @Override
    public void readerConnectFailedEvent(SdkStatus sdkStatus, UUID uuid, RfInterface rfInterface) {
        Log.d("Reader connect failed, " , sdkStatus.getError() + " interface:" + rfInterface);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void readerDisconnectEvent(UUID uuid, RfInterface rfInterface) {
        Log.d("Reader disconnected from ", uuid + " interface: " + rfInterface);
    }

    @Override
    public void readerReceivedReaderFoundReportEvent(ReaderFoundReport readerFoundReport) {
        Log.d("Reader found","");
        mManager.connectToReader(readerFoundReport, Settings.connectionTimeout);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void rfInterfaceActivatedEvent(long identifier, AddressingMode addressingMode, RfInterface rfInterface) {
        Log.d("Interface activated, Identifier: " , identifier + " mode: " + addressingMode.name()
                + " interface:" + rfInterface);
        updateInterfaceStatus();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void rfInterfaceDeactivatedEvent(long identifier, AddressingMode addressingMode, RfInterface rfInterface) {
        Log.d("Interface deactivated, Identifier: " , identifier + " mode: " + addressingMode.name()
                + " interface:" + rfInterface);
        updateInterfaceStatus();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void rfInterfaceChangeEvent(RfInterface rfInterface, RfInterfaceState rfInterfaceState) {
        Log.d("Interface changed interface:" , rfInterface + " new state: " + rfInterfaceState);
        updateInterfaceStatus();
    }

    @Override
    public void readerLcMessageEvent(byte[] data, LcMessageMode lcMessageMode, RfInterface rfInterface) {
        Log.d("LC message event data: " , Utils.dataToByteString(data) + " mode: " + lcMessageMode
                + " on interface " + rfInterface);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void readerLcMessagePollingEvent(LcMessageMode lcMessageMode, RfInterface rfInterface) {
        Log.d("LC message polling event, mode: " , lcMessageMode + " on interface " + rfInterface);
    }

    @Override
    public void backendRegistrationInitializedEvent(SdkStatus status) {
        /*if (status.isSuccess()) {
            log("Registration Step 1 done with status " + status);
        } else {
            handleSdkErrors(status);
        }*/
        /*Log.d("sdkStatus",status.toString());
        if (status.isSuccess()) {

            //initSdk();
            Log.d("registerWithBackend","Yes");
            Log.d("mLegicToken", GlobalClass.mLegicToken);
            mManager.registerWithBackendRegistrationCode(GlobalClass.mLegicToken);

        }*/
    }

    @SuppressLint("LongLogTag")
    @Override
    public void backendRegistrationFinishedEvent(SdkStatus status) {
        if (status.isSuccess()) {
            Log.d("Registration Step 2 done with status " , status.toString());
        } else {
            handleSdkErrors(status);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void backendUnregisteredEvent(SdkStatus status) {
        if (status.isSuccess()) {
            Log.d("Unregister done with status " , status.toString());
        } else {
            handleSdkErrors(status);
        }
    }

    @Override
    public void backendSynchronizeStartEvent() {
        Log.d("Synchronize started","");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void backendSynchronizeDoneEvent(SdkStatus status) {
        if (status.isSuccess()) {
            Log.d("Synchronize done with status " , status.toString());
        } else {
            handleSdkErrors(status);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void readerPasswordRequestEvent(byte[] data, RfInterface rfInterface) {
        Log.d("Password request with bytes " , Utils.dataToByteString(data) + " interface:" + rfInterface);
    }
}


// -----------------------------------------------------------------------------
// Copyright© 2019 LEGIC® Identsystems AG, CH-8623 Wetzikon
// Confidential. All rights reserved!
// -----------------------------------------------------------------------------

package com.taj.doorunlock.unlock.doormakaba;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.legic.mobile.sdk.api.LegicMobileSdkManager;
import com.legic.mobile.sdk.api.exception.SdkException;
import com.legic.mobile.sdk.api.types.RfInterface;
import com.taj.doorunlock.R;
import com.taj.doorunlock.unlock.Taj;


/**
 * Base Activity that implements all common activity functions.
 */
public abstract class BaseActivity extends AppCompatActivity implements PermissionManagerCallback {

    //private LogManager mLogManager;
    private PermissionManager mPermissionManager;

    protected Taj mSdkQuickstartApp;
    protected LegicMobileSdkManager mManager;
    protected ScrollView logScrollView;
    protected TextView logText;
    protected TextView bleStatus;
    protected TextView bleCentralStatus;
    protected TextView hceStatus;


    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSdkQuickstartApp =  Taj.getInstance(this);
       // mLogManager = mSdkQuickstartApp.getLogManager();
        mPermissionManager = new PermissionManager( this, this);

        // Must be registered before the activity is in state CREATED
        mPermissionManager.registerForAccessFineLocation(registerForActivityResult(
                mPermissionManager.getAccessFineLocationRequest(),
                mPermissionManager.getAccessFineLocationCallback()
        ));

        mPermissionManager.registerForBluetoothScanPermission(registerForActivityResult(
                mPermissionManager.getPermissionBluetoothScanRequest(),
                mPermissionManager.getbluetoothScanCallback()
        ));

        mPermissionManager.registerForBluetoothAdvertisePermission(registerForActivityResult(
                mPermissionManager.getPermissionBluetoothAdvertiseRequest(),
                mPermissionManager.getBluetoothAdvertiseCallback()
        ));

        mPermissionManager.registerForBluetoothConnectPermission(registerForActivityResult(
                mPermissionManager.getPermissionBluetoothConnectRequest(),
                mPermissionManager.getbluetoothConnectCallback()
        ));
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void onResume() {
        super.onResume();
        mManager = mSdkQuickstartApp.getSdkManager();
        updateInterfaceStatus();
        /*if (mLogManager != null) {
            mLogManager.setUiDestination(logText);
        }*/

        if (logText != null) {
            logText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // not implemented
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // not implemented
                }

                @Override
                public void afterTextChanged(Editable s) {
                    scrollLogView();
                }
            });
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void onPause() {
        super.onPause();
    }

    //-----------------------------------------------------------------------------------------------------------------|
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    //-----------------------------------------------------------------------------------------------------------------|
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            onAbout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    //-----------------------------------------------------------------------------------------------------------------|
    public void redirectToStep1() {
        log("Not registered, redirecting to activity RegistrationStep1");
        //Intent intent = new Intent(this.getApplicationContext(), RegistrationStep1.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        //startActivity(intent);

        this.finish();
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void clearLog(View view) {
        /*if (mLogManager != null) {
            mLogManager.clearInfoViewBuffer();
            logText.setText("");
        }*/
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void handleStartOfMobileSdkManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPermissionManager.getBluetoothConnectPermissionsGranted();
        } else {
            startMobileSdkManager();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void startMobileSdkManager() {
        try {
            log("Start mobile SDK manager");
            mManager.start(Settings.mobileAppId, Settings.mobileAppTechUser,
                    Settings.mobileAppTechPassword, Settings.serverUrl);
        } catch(SdkException e) {
            logException("Could not start mobile SDK", e);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void activateInterfaces() {
        try {
            activateBleWithPermissionCheck();
            activateNfcInterface();
        } catch(SdkException e) {
            logException("Could not activate interface ", e);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void activateBleWithPermissionCheck() throws SdkException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // In BLE_PERIPHERAL role, activate interface in callback
            mPermissionManager.getBluetoothAdvertisePermissionsGranted();
        } else {
            // No runtime permissions required
            activateBleInterface();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void activateBleCentralWithPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // In BLE_CENTRAL role, activate interface in callback
            mPermissionManager.getBluetoothScanPermissionsGranted();
        } else {
            // In BLE_CENTRAL role, activate interface in callback
            mPermissionManager.getFineLocationPermissionsGranted();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void activateBleInterface() throws SdkException {
        if (mManager.isRfInterfaceHardwareSupported(RfInterface.BLE_PERIPHERAL)) {
            if (!mManager.isRfInterfaceActive(RfInterface.BLE_PERIPHERAL)) {
                mManager.setRfInterfaceActive(RfInterface.BLE_PERIPHERAL, true);
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void activateNfcInterface() throws SdkException {
        if (mManager.isRfInterfaceHardwareSupported(RfInterface.NFC_HCE)) {
            if (!mManager.isRfInterfaceActive(RfInterface.NFC_HCE)) {
                mManager.setRfInterfaceActive(RfInterface.NFC_HCE, true);
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void activateBleCentralInterface() throws SdkException {
        if (mManager.isRfInterfaceHardwareSupported(RfInterface.BLE_CENTRAL)) {
            if (!mManager.isRfInterfaceActive(RfInterface.BLE_CENTRAL)) {
                mManager.setRfInterfaceActive(RfInterface.BLE_CENTRAL, true);
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if ((currentFocus != null) && (inputMethodManager != null)) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    protected void updateInterfaceStatus() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mManager != null && mManager.isStarted()) {
                        if (bleStatus != null) setBleStatus(mManager.isRfInterfaceActive(RfInterface.BLE_PERIPHERAL));
                        if (bleCentralStatus != null) setBleCentralStatus(mManager.isRfInterfaceActive(RfInterface.BLE_CENTRAL));
                        if (hceStatus != null) setHceStatus(mManager.isRfInterfaceActive(RfInterface.NFC_HCE));
                    }
                } catch (SdkException e) {
                    logException("Update interface status failed ", e);
                }
            }
        });
    }

    //-------------------------------------------------------------------------|
    protected void log(String msg) {
        //mLogManager.log(LogManager.LogLevel.LogLevelVerbose, getClass().getSimpleName(), msg);
    }

    //-------------------------------------------------------------------------|
    protected void logException(String msg, Exception e) {
       // mLogManager.log(LogManager.LogLevel.LogLevelError, getClass().getSimpleName(), msg);
        //mLogManager.logException(e);
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void scrollLogView() {
        runOnUiThread(new Runnable() {
            public void run() {
                logScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        logScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void onAbout() {
        StringBuilder msg = new StringBuilder();

        try {
            msg.append("\n----------------------------------");
            msg.append("\nSDK version: ");
            msg.append(mManager.getVersion());
            msg.append("\n----------------------------------");
            msg.append("\nRegistered: ");
            msg.append(mManager.isRegisteredToBackend());
            msg.append("\nBLE Role: ");
            //msg.append(mManager.getCurrentBleRole());
            msg.append("\nBLE HW Enabled: ");
            msg.append(mManager.isRfInterfaceHardwareEnabled(RfInterface.BLE_PERIPHERAL));
            msg.append("\nBLE HW Supported: ");
            msg.append(mManager.isRfInterfaceHardwareSupported(RfInterface.BLE_PERIPHERAL));
            msg.append("\nBLE Active: ");
            msg.append(mManager.isRfInterfaceActive(RfInterface.BLE_PERIPHERAL));
            msg.append("\nBLE_CENTRAL HW Enabled: ");
            msg.append(mManager.isRfInterfaceHardwareEnabled(RfInterface.BLE_CENTRAL));
            msg.append("\nBLE_CENTRAL HW Supported: ");
            msg.append(mManager.isRfInterfaceHardwareSupported(RfInterface.BLE_CENTRAL));
            msg.append("\nBLE_CENTRAL Active: ");
            msg.append(mManager.isRfInterfaceActive(RfInterface.BLE_CENTRAL));
            msg.append("\nNFC-HCE HW Enabled: ");
            msg.append(mManager.isRfInterfaceHardwareEnabled(RfInterface.NFC_HCE));
            msg.append("\nNFC-HCE HW Supported: ");
            msg.append(mManager.isRfInterfaceHardwareSupported(RfInterface.NFC_HCE));
            msg.append("\nNFC-HCE Active: ");
            msg.append(mManager.isRfInterfaceActive(RfInterface.NFC_HCE));
            msg.append("\nCurrent Security Category: ");
            msg.append(mManager.getCurrentSecurityCategory());
            msg.append("\n----------------------------------\n");
            updateInterfaceStatus();
        } catch(SdkException e) {
            msg.append("Exception during SDK call: ");
            msg.append(e.getLocalizedMessage());
        }

        log(msg.toString());
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void setBleStatus(boolean enabled) {
        bleStatus.setText(getString(R.string.ble_status_msg, (enabled?"ON":"OFF")));
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void setBleCentralStatus(boolean enabled) {
        bleCentralStatus.setText(getString(R.string.ble_central_status_msg, (enabled?"ON":"OFF")));
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void setHceStatus(boolean enabled) {
        hceStatus.setText(getString(R.string.hce_status_msg, (enabled?"ON":"OFF")));
    }

    //---------------------------------------------------------------------------------------------|
    @Override
    public void onUserPermissionResultFineLocation(boolean result) {
        log("Access fine location request granted by user: " + result);

        if (result) {
            try {
                activateBleCentralInterface();
            } catch (SdkException e) {
                logException("Exception", e);
            }
        }
    }

    //---------------------------
    //------------------------------------------------------------------|
    @Override
    public void onUserPermissionResultBluetoothScan(boolean result) {
        log("Bluetooth scan request granted by user: " + result);
        if (result) {
            try {
                activateBleCentralInterface();
            } catch (SdkException e) {
                logException("Exception", e);
            }
        }
    }

    //---------------------------------------------------------------------------------------------|
    @Override
    public void onUserPermissionResultBluetoothAdvertise(boolean result) {
        log("Bluetooth advertise request granted by user: " + result);
        if (result) {
            try {
                activateBleInterface();
            } catch (SdkException e) {
                logException("Exception", e);
            }
        }
    }

    //---------------------------------------------------------------------------------------------|
    @Override
    public void onUserPermissionResultBluetoothConnect(boolean result) {
        log("Bluetooth connect request granted by user: " + result);
        if (result) {
            startMobileSdkManager();
        }
    }
}

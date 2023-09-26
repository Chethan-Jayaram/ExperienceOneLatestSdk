// -----------------------------------------------------------------------------
// Copyright© 2019 LEGIC® Identsystems AG, CH-8623 Wetzikon
// Confidential. All rights reserved!
// -----------------------------------------------------------------------------

package com.taj.doorunlock.unlock.doormakaba;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.taj.doorunlock.R;


public class PermissionManager {

    private final PermissionManagerCallback mCallback;
    private final Activity mActivity;

    //private final LogManager mLogManager;

    private final ActivityResultContracts.RequestPermission mAccessFineLocationRequest;
    private final ActivityResultContracts.RequestPermission mBluetoothScanRequest;
    private final ActivityResultContracts.RequestPermission mBluetoothAdvertiseRequest;
    private final ActivityResultContracts.RequestPermission mBluetoothConnectRequest;

    private ActivityResultLauncher<String> mAccessFineLocationRequestLauncher;
    private ActivityResultLauncher<String> mBluetoothScanRequestLauncher;
    private ActivityResultLauncher<String> mBluetoothAdvertiseRequestLauncher;
    private ActivityResultLauncher<String> mBluetoothConnectRequestLauncher;

    //---------------------------------------------------------------------------------------------|

    /**
     *  PermissionManager handles permission checks and asking the user for permissions at runtime.
     *
     *  If a user does not grant a specific permission, an explanation is shown, illustrating which
     *  function of the app requires the specified permission.
     *
     *  Permission requests are compliant with:
     *  https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions
     */
    public PermissionManager(PermissionManagerCallback callback, Activity activity) {
        mCallback = callback;
        mActivity = activity;
        //mLogManager = SdkQuickstartApplication.getInstance().getLogManager();
        mAccessFineLocationRequest = new ActivityResultContracts.RequestPermission();
        mBluetoothScanRequest = new ActivityResultContracts.RequestPermission();
        mBluetoothAdvertiseRequest = new ActivityResultContracts.RequestPermission();
        mBluetoothConnectRequest = new ActivityResultContracts.RequestPermission();
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Get callback function for access fine location permission request.
     */
    public ActivityResultCallback<Boolean> getAccessFineLocationCallback() {
        return accessFineLocationCallback;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Get callback function for bluetooth scan permission request.
     */
    public ActivityResultCallback<Boolean> getbluetoothScanCallback() {
        return bluetoothScanCallback;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Get callback function for bluetooth advertise permission request.
     */
    public ActivityResultCallback<Boolean> getBluetoothAdvertiseCallback() {
        return bluetoothAdvertiseCallback;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Get callback function for bluetooth connect permission request.
     */
    public ActivityResultCallback<Boolean> getbluetoothConnectCallback() {
        return bluetoothConnectCallback;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Gets access fine location permission request.
     *
     *  @return access fine location permission request
     */
    public ActivityResultContracts.RequestPermission getAccessFineLocationRequest() {
        return mAccessFineLocationRequest;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Gets bluetooth scan permission request.
     *
     *  @return bluetooth scan permission request
     */
    public ActivityResultContracts.RequestPermission getPermissionBluetoothScanRequest() {
        return mBluetoothScanRequest;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Gets bluetooth advertise permission request.
     *
     *  @return bluetooth advertise permission request
     */
    public ActivityResultContracts.RequestPermission getPermissionBluetoothAdvertiseRequest() {
        return mBluetoothAdvertiseRequest;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Gets bluetooth connect permission request.
     *
     *  @return bluetooth connect permission request
     */
    public ActivityResultContracts.RequestPermission getPermissionBluetoothConnectRequest() {
        return mBluetoothConnectRequest;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Registers launcher for access fine location permission request.
     *
     *  @param launcher launcher for permission request
     */
    public void registerForAccessFineLocation(ActivityResultLauncher<String> launcher) {
        mAccessFineLocationRequestLauncher = launcher;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Registers launcher for bluetooth scan permission request.
     *
     *  @param launcher launcher for permission request
     */
    public void registerForBluetoothScanPermission(ActivityResultLauncher<String> launcher) {
        mBluetoothScanRequestLauncher = launcher;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Registers launcher for bluetooth advertise permission request.
     *
     *  @param launcher launcher for permission request
     */
    public void registerForBluetoothAdvertisePermission(ActivityResultLauncher<String> launcher) {
        mBluetoothAdvertiseRequestLauncher = launcher;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Registers launcher for bluetooth connect permission request.
     *
     *  @param launcher launcher for permission request
     */
    public void registerForBluetoothConnectPermission(ActivityResultLauncher<String> launcher) {
        mBluetoothConnectRequestLauncher = launcher;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Handles process to obtain access fine location permission.
     */
    public void getFineLocationPermissionsGranted() {
        askForFineLocationRuntimePermission();
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Handles process to obtain bluetooth scan permission.
     */
    public void getBluetoothScanPermissionsGranted() {
        askForBluetoothScanRuntimePermission();
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Handles process to obtain bluetooth advertise permission.
     */
    public void getBluetoothAdvertisePermissionsGranted() {
        askForBluetoothAdvertiseRuntimePermission();
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Handles process to obtain bluetooth connect permission.
     */
    public void getBluetoothConnectPermissionsGranted() {
        askForBluetoothConnectRuntimePermission();
    }

    // Private
    //---------------------------------------------------------------------------------------------|

    //---------------------------------------------------------------------------------------------|

    /**
     *  Callback function for access fine location permission request.
     */
    private final ActivityResultCallback<Boolean> accessFineLocationCallback =
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    mCallback.onUserPermissionResultFineLocation(result);
                }
            };

    //---------------------------------------------------------------------------------------------|

    /**
     *  Callback function for bluetooth scan permission request.
     */
    private final ActivityResultCallback<Boolean> bluetoothScanCallback =
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    mCallback.onUserPermissionResultBluetoothScan(result);
                }
            };

    //---------------------------------------------------------------------------------------------|

    /**
     *  Callback function for bluetooth advertise permission request.
     */
    private final ActivityResultCallback<Boolean> bluetoothAdvertiseCallback =
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    mCallback.onUserPermissionResultBluetoothAdvertise(result);
                }
            };

    //---------------------------------------------------------------------------------------------|

    /**
     *  Callback function for bluetooth connect permission request.
     */
    private final ActivityResultCallback<Boolean> bluetoothConnectCallback =
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    mCallback.onUserPermissionResultBluetoothConnect(result);
                }
            };

    //---------------------------------------------------------------------------------------------|

    /**
     *  Asks for user access fine location permission at runtime.
     */
    private void askForFineLocationRuntimePermission() {

        String permission = Manifest.permission.ACCESS_FINE_LOCATION;

        if (isPermissionNotGranted(permission)) {
            // Check if user declined request before
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                //log("User declined access fine location request. Show an explanatory UI");

                // Show explanatory UI dialog
                showPermissionRationaleDialog(
                        "Permission ACCESS_FINE_LOCATION required",
                        "Fine location is needed for BLE_CENTRAL interface. " +
                                "Please allow access for proper operation.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAccessFineLocationRequestLauncher.launch(permission);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCallback.onUserPermissionResultFineLocation(false);
                            }
                        }
                );
            } else {
                mAccessFineLocationRequestLauncher.launch(permission);
            }
        } else {
            mCallback.onUserPermissionResultFineLocation(true);
        }
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Asks for user bluetooth scan permission at runtime.s
     */
    private void askForBluetoothScanRuntimePermission() {

        String permission = Manifest.permission.BLUETOOTH_SCAN;

        if (isPermissionNotGranted(permission)) {
            // Check if user declined request before
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                //log("User bluetooth scan request request. Show an explanatory UI");

                // Show other UI dialog
                showPermissionRationaleDialog(
                        "Permission BLUETOOTH_SCAN required",
                        "Bluetooth scan permission is required for BLE_CENTRAL interface",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mBluetoothScanRequestLauncher.launch(permission);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCallback.onUserPermissionResultBluetoothScan(false);
                            }
                        }
                );
            } else {
                mBluetoothScanRequestLauncher.launch(permission);
            }
        } else {
            mCallback.onUserPermissionResultBluetoothScan(true);
        }
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Asks for user bluetooth advertise permission at runtime.
     */
    private void askForBluetoothAdvertiseRuntimePermission() {

        String permission = Manifest.permission.BLUETOOTH_ADVERTISE;

        if (isPermissionNotGranted(permission)) {
            // Check if user declined request before
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                //log("User declined bluetooth advertise request. Show an explanatory UI");

                // Show other UI dialog
                showPermissionRationaleDialog(
                        "Permission BLUETOOTH_ADVERTISE required",
                        "Bluetooth advertise permission is needed for BLE interface",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mBluetoothAdvertiseRequestLauncher.launch(permission);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCallback.onUserPermissionResultBluetoothAdvertise(false);
                            }
                        }
                );
            } else {
                mBluetoothAdvertiseRequestLauncher.launch(permission);
            }
        } else {
            mCallback.onUserPermissionResultBluetoothAdvertise(true);
        }
    }

    //---------------------------------------------------------------------------------------------|

    /**
     *  Asks for user bluetooth connect permission at runtime.
     */
    private void askForBluetoothConnectRuntimePermission() {

        String permission = Manifest.permission.BLUETOOTH_CONNECT;

        if (isPermissionNotGranted(permission)) {
            // Check if declined request before
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                //log("User declined bluetooth connect request. Show an explanatory UI");

                // Show other UI dialog
                showPermissionRationaleDialog(
                        "Permission BLUETOOTH_CONNECT required",
                        "Bluetooth connect permission is needed for LEGIC SDK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mBluetoothConnectRequestLauncher.launch(permission);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mCallback.onUserPermissionResultBluetoothConnect(false);
                            }
                        }
                );
            } else {
                mBluetoothConnectRequestLauncher.launch(permission);
            }
        } else {
            mCallback.onUserPermissionResultBluetoothConnect(true);
        }
    }

    //---------------------------------------------------------------------------------------------|

    /**
     * Logs a message.
     *
     * @param msg message to be logged
     */
    /*protected void log(String msg) {
        mLogManager.log(LogManager.LogLevel.LogLevelVerbose,
                PermissionManager.class.getSimpleName(), msg);
    }*/

    //---------------------------------------------------------------------------------------------|

    /**
     * Checks if a permission is not granted.
     *
     * @param requiredPermission permission to be checked
     * @return  true if permission is not granted
     *          false if permission is granted
     */
    private boolean isPermissionNotGranted(String requiredPermission) {
        return ContextCompat.checkSelfPermission(
                mActivity.getApplicationContext(), requiredPermission)
                != PackageManager.PERMISSION_GRANTED;
    }

    //---------------------------------------------------------------------------------------------|

    /**
     * Creates a rationale dialog providing additional information on why a permission is needed.
     *
     * @param title title of permission rationale
     * @param rationale explanation why permission is required
     * @param okay onClick action to be performed if user accepts
     * @param cancel onClick action to be performed if user declines
     */
    private void showPermissionRationaleDialog(String title,
                                               String rationale,
                                               DialogInterface.OnClickListener okay,
                                               DialogInterface.OnClickListener cancel) {

        // Create dialog on mActivity
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setTitle(title);

        // Include don't ask again warning
        rationale = rationale + mActivity.getString(R.string.do_not_ask_again_warning);
        alertDialogBuilder.setMessage(rationale);

        // Add response buttons
        alertDialogBuilder.setPositiveButton("Ok, ask me again", okay);
        alertDialogBuilder.setNegativeButton("Cancel", cancel);

        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

}

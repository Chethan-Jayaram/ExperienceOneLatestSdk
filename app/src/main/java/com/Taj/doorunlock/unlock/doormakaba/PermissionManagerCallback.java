// -----------------------------------------------------------------------------
// Copyright© 2016 LEGIC® Identsystems AG, CH-8623 Wetzikon
// Confidential. All rights reserved!
// -----------------------------------------------------------------------------

package com.taj.doorunlock.unlock.doormakaba;

public interface PermissionManagerCallback {

    /**
     *  Callback function for access fine location permission request.
     *
     *  @param  result   true permission granted
     *                   false permission not granted
     */
    void onUserPermissionResultFineLocation(boolean result);

    /**
     *  Callback function for bluetooth scan permission request.
     *
     *  @param  result   true permission granted
     *                   false permission not granted
     */
    void onUserPermissionResultBluetoothScan(boolean result);

    /**
     *  Callback function for bluetooth advertise permission request.
     *
     *  @param  result   true permission granted
     *                   false permission not granted
     */
    void onUserPermissionResultBluetoothAdvertise(boolean result);

    /**
     *  Callback function for bluetooth connect permission request.
     *
     *  @param  result   true permission granted
     *                   false permission not granted
     */
    void onUserPermissionResultBluetoothConnect(boolean result);
}

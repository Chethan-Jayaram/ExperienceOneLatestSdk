// -----------------------------------------------------------------------------
// Copyright© 2017 LEGIC® Identsystems AG, CH-8623 Wetzikon
// Confidential. All rights reserved!
// -----------------------------------------------------------------------------

package com.taj.doorunlock.unlock.doormakaba;

import android.content.Context;

import com.legic.mobile.sdk.api.LegicMobileSdkManager;
import com.legic.mobile.sdk.api.LegicMobileSdkManagerFactory;
import com.legic.mobile.sdk.api.exception.LegicMobileSdkException;


public class Utils {

    private static String pushToken;

    //-----------------------------------------------------------------------------------------------------------------|
    /**
     * Encodes the given byte array into a hex string.
     *
     * @param data      data to encode
     * @return          hex string
     */
    public static String dataToByteString(byte[] data) {
        if(data == null)  { return ""; }
        StringBuilder outputString = new StringBuilder();
        for(byte b : data)
        {
            outputString.append(String.format("%02X", b));
        }
        return outputString.toString();
    }

    //-----------------------------------------------------------------------------------------------------------------|
    /**
     * Returns a fully configured SDK manager object to access the LEGIC SDK.
     *
     * @param context                       application context
     * @return                              configured SDK manager
     * @throws LegicMobileSdkException      if any error occurs
     */
    public static LegicMobileSdkManager getSdkManager(Context context) throws LegicMobileSdkException {
        LegicMobileSdkManager manager = LegicMobileSdkManagerFactory.getInstance(context);
        return manager;
    }

    //-----------------------------------------------------------------------------------------------------------------|
    /**
     * Interim storage for the received push token for later use (in registration call).
     *
     * @param pushToken     newly received push token
     */
    public static void setPushToken(String pushToken) {
        pushToken = pushToken;
    }

    //-----------------------------------------------------------------------------------------------------------------|
    /**
     * Returns the stored push token.
     *
     * @return  stored push token (or null if not initialized)
     */
    public static String getPushToken() {
        return pushToken;
    }

}

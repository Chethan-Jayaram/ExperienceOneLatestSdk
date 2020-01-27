package com.example.experienceone.unlock;

import com.assaabloy.mobilekeys.api.MobileKeys;
import com.assaabloy.mobilekeys.api.ReaderConnectionController;
import com.assaabloy.mobilekeys.api.ble.ScanConfiguration;

/**
 * Mobile keys api factory
 */
public interface MobileKeysApiFactory
{
    /**
     * Get the a mobile keys api instance
     * @return
     */
    MobileKeys getMobileKeys();

    /**
     * Get the a reader connection controller instance
     * @return
     */
    ReaderConnectionController getReaderConnectionController();

    /**
     * Get the scan configuration instance
     * @return
     */
    ScanConfiguration getScanConfiguration();

}

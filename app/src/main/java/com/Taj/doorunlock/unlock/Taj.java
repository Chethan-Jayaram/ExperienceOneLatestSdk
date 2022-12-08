package com.taj.doorunlock.unlock;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.assaabloy.mobilekeys.api.ApiConfiguration;
import com.assaabloy.mobilekeys.api.MobileKeys;
import com.assaabloy.mobilekeys.api.MobileKeysApi;
import com.assaabloy.mobilekeys.api.ReaderConnectionController;
import com.assaabloy.mobilekeys.api.ble.BluetoothMode;
import com.assaabloy.mobilekeys.api.ble.OpeningTrigger;
import com.assaabloy.mobilekeys.api.ble.RssiSensitivity;
import com.assaabloy.mobilekeys.api.ble.ScanConfiguration;
import com.assaabloy.mobilekeys.api.ble.ScanMode;
import com.assaabloy.mobilekeys.api.ble.TapOpeningTrigger;
import com.assaabloy.mobilekeys.api.hce.NfcConfiguration;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.taj.doorunlock.BuildConfig;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.llew.huawei.verifier.LoadedApkHuaWei;
import com.taj.doorunlock.helper.NotificationHandler;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;


import javax.net.ssl.SSLContext;


/**
 * Application class handling the initialization of the Mobile Keys API
 */
public class Taj extends Application implements MobileKeysApiFactory, OSSubscriptionObserver {

/*
    private static final int LOCK_SERVICE_CODE = BuildConfig.AAMK_LOCK_SERVICE_CODE;
    private MobileKeysApi mobileKeysFactory;


    @Override
    public void onCreate() {
        super.onCreate();
        initializeMobileKeysApi();
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }



    *//**
     * Configure and initialize the mobile keys library
     *//*
    private void initializeMobileKeysApi() {


        OpeningTrigger[] openingTriggers = {new TapOpeningTrigger(this)};
        ScanConfiguration scanConfiguration = new ScanConfiguration.Builder(openingTriggers, LOCK_SERVICE_CODE)
                .setBluetoothModeIfSupported(BluetoothMode.DUAL)
                .setScanMode(ScanMode.OPTIMIZE_PERFORMANCE)
                .setRssiSensitivity(RssiSensitivity.HIGH)
                .build();

        ApiConfiguration apiConfiguration = new ApiConfiguration.Builder()
                .setApplicationId(BuildConfig.AAMK_APP_ID)
                .setApplicationDescription(BuildConfig.AAMK_APP_ID_DESCRIPTION)
                .setNfcParameters(new NfcConfiguration.Builder()
                        .unsafe_setAttemptNfcWithScreenOff(false)
                        .build())
                .build();

        mobileKeysFactory = MobileKeysApi.getInstance();
        mobileKeysFactory.initialize(this, apiConfiguration, scanConfiguration);
        if (!mobileKeysFactory.isInitialized()) {
            throw new IllegalStateException();
        }

        MobileKeysApi.getInstance().getReaderConnectionController().enableHce();
    }

    @Override
    public MobileKeys getMobileKeys() {
        return mobileKeysFactory.getMobileKeys();
    }

    @Override
    public ReaderConnectionController getReaderConnectionController() {
        return mobileKeysFactory.getReaderConnectionController();
    }

    @Override
    public ScanConfiguration getScanConfiguration() {
        return getReaderConnectionController().getScanConfiguration();
    }*/
    private static final int LOCK_SERVICE_CODE = BuildConfig.AAMK_LOCK_SERVICE_CODE;

    private MobileKeysApi mobileKeysFactory;
    private static Taj mInstance;
    private static Context context;

    public Taj(){
        mInstance=this;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        LoadedApkHuaWei.hookHuaWeiVerifier(getBaseContext());
        mInstance=this;
        context=getApplicationContext();
        initializeMobileKeysApi();
        OneSignal.initWithContext(this);
        OneSignal.setAppId("33e7eb54-51d6-42ec-8451-f9825f8b7cad");
        OneSignal.addSubscriptionObserver(this);


        /*OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotificationHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();*/


      /*  try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Configure and initialize the mobile keys library
     */
    private void initializeMobileKeysApi()
    {

        OpeningTrigger[] openingTriggers = {new TapOpeningTrigger(this)};
        ScanConfiguration scanConfiguration = new ScanConfiguration.Builder(openingTriggers, LOCK_SERVICE_CODE)
                .setBluetoothModeIfSupported(BluetoothMode.DUAL)
                .setScanMode(ScanMode.OPTIMIZE_PERFORMANCE)
                .setRssiSensitivity(RssiSensitivity.HIGH)
                .build();

        ApiConfiguration apiConfiguration = new ApiConfiguration.Builder()
                .setApplicationId(BuildConfig.AAMK_APP_ID)
                .setApplicationDescription(BuildConfig.AAMK_APP_ID_DESCRIPTION)
                .setNfcParameters(new NfcConfiguration.Builder()
                        .unsafe_setAttemptNfcWithScreenOff(false)
                        .build())
                .build();


        mobileKeysFactory = MobileKeysApi.getInstance();
        mobileKeysFactory.initialize(this, apiConfiguration, scanConfiguration);
        if(!mobileKeysFactory.isInitialized()) {
            throw new IllegalStateException();
        }

        MobileKeysApi.getInstance().getReaderConnectionController().enableHce();
    }

    @Override
    public MobileKeys getMobileKeys()
    {
        return mobileKeysFactory.getMobileKeys();
    }

    @Override
    public ReaderConnectionController getReaderConnectionController()
    {
        return mobileKeysFactory.getReaderConnectionController();
    }

    @Override
    public ScanConfiguration getScanConfiguration()
    {
        return getReaderConnectionController().getScanConfiguration();
    }


    public static synchronized Taj getInstance(){
        return mInstance;
    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {

    }
}

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
import com.legic.mobile.sdk.api.types.ReaderOperationMode;
import com.legic.mobile.sdk.api.types.RfInterface;
import com.legic.mobile.sdk.api.types.RfInterfaceState;
import com.legic.mobile.sdk.api.types.SdkErrorReason;
import com.legic.mobile.sdk.api.types.SdkStatus;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.taj.doorunlock.BuildConfig;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.llew.huawei.verifier.LoadedApkHuaWei;
import com.taj.doorunlock.helper.NotificationHandler;
import com.taj.doorunlock.unlock.doormakaba.BaseActivity;
import com.taj.doorunlock.unlock.doormakaba.Settings;
import com.taj.doorunlock.unlock.doormakaba.Utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;


import javax.net.ssl.SSLContext;


/**
 * Application class handling the initialization of the Mobile Keys API
 */
public class Taj extends Application implements MobileKeysApiFactory, OSSubscriptionObserver, BackendEventListener,
        ReaderEventListener, SdkEventListener, LcMessageEventListener, NeonFileEventListener {

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


    /*public static synchronized Taj getInstance(){
        return mInstance;
    }*/

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {

    }
    private static final String mComponentName = Taj.class.getSimpleName();
    private static Taj self;

   // private final LogManager mLogManager = new LogManager();
    private BaseActivity activity;
    private boolean mIsInitialized;

    private LegicMobileSdkManager sdkManager;
    private SdkEventListener mSdkEventListener;
    private BackendEventListener mBackendEventListener;


    //-----------------------------------------------------------------------------------------------------------------|
    public static Taj getInstance() {
        if (self == null) {
            self = new Taj();
        }
        return self;
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public static Taj getInstance(BaseActivity activity) {
        if (self == null) {
            self = new Taj();
        }
        self.mSdkEventListener = null;
        self.mBackendEventListener = null;
        self.setActivity(activity);
        return self;
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void setActivity(BaseActivity activity) {
        this.activity = activity;
        if (!mIsInitialized) {
            initializeSdkManager();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public LegicMobileSdkManager getSdkManager() {
        if (sdkManager == null) {
            initializeSdkManager();
        }
        return sdkManager;
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void initializeSdkManager() {
        try {
            sdkManager = Utils.getSdkManager(activity.getApplicationContext());
            registerListeners();
            mIsInitialized = true;
            log("SDK initialized");
        } catch (SdkException e) {
            logException("SDK not initialized", e);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    /*public LogManager getLogManager() {
        return mLogManager;
    }*/

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void onTerminate() {
        unregisterListeners();
        super.onTerminate();
    }

    //-----------------------------------------------------------------------------------------------------------------|
    // Assign the listener implementing events interface that will receive the events
    public void setSdkEventListener(SdkEventListener listener) {
        this.mSdkEventListener = listener;
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void setBackendEventListener(BackendEventListener listener) {
        this.mBackendEventListener = listener;
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void registerListeners() {
        if (sdkManager == null) {return;}
        try {
            sdkManager.registerForSdkEvents(this);
            sdkManager.registerForReaderEvents(this);
            sdkManager.registerForBackendEvents(this);
            sdkManager.registerForLcMessageEvents(this);
            sdkManager.registerForNeonFileEvents(this);
            log("Listeners registered ");
        } catch(SdkException e) {
            logException("Could not register listener ", e);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void unregisterListeners() {
        if (sdkManager == null) {
            return;
        }
        sdkManager.unregisterAnyEvents(this);
        log("Listeners unregistered ");
    }

    //-----------------------------------------------------------------------------------------------------------------|
    // SdkEventListener implementation
    //-----------------------------------------------------------------------------------------------------------------|

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void rfInterfaceActivatedEvent(long identifier, AddressingMode mode, RfInterface rfInterface) {
        log("Interface activated, Identifier: " + identifier + " mode: " + mode
                + " interface:" + rfInterface);
        if (mSdkEventListener != null) {
            mSdkEventListener.rfInterfaceActivatedEvent(identifier, mode, rfInterface);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void rfInterfaceDeactivatedEvent(long identifier, AddressingMode mode, RfInterface rfInterface) {
        log("Interface deactivated, Identifier: " + identifier + " mode: " + mode
                + " interface:" + rfInterface);
        if (mSdkEventListener != null) {
            mSdkEventListener.rfInterfaceDeactivatedEvent(identifier, mode, rfInterface);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void rfInterfaceChangeEvent(RfInterface rfInterface, RfInterfaceState rfInterfaceState) {
        log("State change of interface: " + rfInterface +  " \nNew state: " + rfInterfaceState);
        if (mSdkEventListener != null) {
            mSdkEventListener.rfInterfaceChangeEvent(rfInterface, rfInterfaceState);
        }
    }


    //-----------------------------------------------------------------------------------------------------------------|
    // BackendEventListener implementation
    //-----------------------------------------------------------------------------------------------------------------|

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendRegistrationInitializedEvent(SdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            Log.d("Registration Step 1 done with status " , sdkStatus.toString());
            if (mBackendEventListener != null) {
                mBackendEventListener.backendRegistrationInitializedEvent(sdkStatus);
            }
        } else {
            handleSdkErrors(sdkStatus);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendRegistrationFinishedEvent(SdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            Log.d("Registration Step 2 done with status " , sdkStatus.toString());

            if (mBackendEventListener != null) {
                mBackendEventListener.backendRegistrationFinishedEvent(sdkStatus);
            }
        } else {
            handleSdkErrors(sdkStatus);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendUnregisteredEvent(SdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            if (mBackendEventListener != null) {
                mBackendEventListener.backendUnregisteredEvent(sdkStatus);
            }
        } else {
            handleSdkErrors(sdkStatus);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendSynchronizeStartEvent() {
        Log.d("Synchronize started","yes");
        if (mBackendEventListener != null) {
            mBackendEventListener.backendSynchronizeStartEvent();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendSynchronizeDoneEvent(SdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            Log.d("Synchronize done with status " , sdkStatus.toString());
        } else {
            handleSdkErrors(sdkStatus);
        }

        if (mBackendEventListener != null) {
            mBackendEventListener.backendSynchronizeDoneEvent(sdkStatus);
        }
    }


    //-----------------------------------------------------------------------------------------------------------------|
    // ReaderEventListener implementation
    //-----------------------------------------------------------------------------------------------------------------|

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerConnectEvent(long identifier,
                                   AddressingMode mode,
                                   int readerType,
                                   UUID readerUuid,
                                   RfInterface rfInterface) {
        log("Reader connect event, Mobile App id : " + identifier + "/" + mode +
                " Reader UUID: " + readerUuid.toString() +
                " Reader Type: " + readerType +
                " interface:" + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerConnectFailedEvent(SdkStatus sdkStatus, UUID readerUuid, RfInterface rfInterface) {
        log("Connection to reader with UUID"+ readerUuid.toString() + "failed");
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerDisconnectEvent(UUID readerUuid, RfInterface rfInterface) {
        log("Reader with UUID"+ readerUuid.toString() + "disconnected");
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerReceivedReaderFoundReportEvent(ReaderFoundReport readerFoundReport) {
        StringBuilder msg = new StringBuilder();

        // If reader designator is specified in settings, log only matching reader found reports
        byte[] readerDesignator = readerFoundReport.getReaderDesignator();
        byte[] settingsReaderDesignator = Settings.readerDesignator;

        boolean displayReaderFoundReport;

        if (settingsReaderDesignator.length > 0 && !(Arrays.equals(settingsReaderDesignator, readerDesignator))) {
            displayReaderFoundReport = false;
        } else {
            displayReaderFoundReport = true;
        }

        if (displayReaderFoundReport) {
            msg.append("\n----------------------------------");
            msg.append("\nReader Found Report: ");
            msg.append("\n----------------------------------");

            // Reader operation mode
            msg.append("\nOperation mode:  ");
            ReaderOperationMode operationMode = readerFoundReport.getReaderOperationMode();
            msg.append(operationMode);

            // Reader designator
            if (readerDesignator.length == 0){
                msg.append("\nDesignator:  ");
            } else {
                msg.append("\nDesignator:  0x");
                msg.append(Utils.dataToByteString(readerDesignator));
            }

            // Reader RSSI
            msg.append("\nRSSI:  ");
            int readerRSSI = readerFoundReport.getRssi();
            msg.append(readerRSSI);

            // Reader has TX power
            msg.append("\nHas TX power:  ");
            boolean hasTxPower = readerFoundReport.hasTxPower();
            msg.append((hasTxPower ? "YES" : "NO"));

            // Reader TX power
            msg.append("\nTX power:  ");
            int txPower = readerFoundReport.getTxPower();
            msg.append(txPower);

            // Timestamp
            msg.append("\nTimestamp:  ");
            long timestamp = readerFoundReport.getTimestamp();
            msg.append(timestamp);

            // Reader UUID
            msg.append("\nUUID:  ");
            UUID readerUuid = readerFoundReport.getReaderUuid();
            msg.append(readerUuid.toString());

            msg.append("\n----------------------------------\n");

            // Connect to reader when it is close (RSSI value)
            if (readerRSSI > Settings.rssiConnectionThreshold) {
                sdkManager.connectToReader(readerFoundReport, Settings.connectionTimeout);
            }
            log(msg.toString());
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    // LcMessageEventListener implementation
    //-----------------------------------------------------------------------------------------------------------------|

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
    public void readerAddedLcMessageEvent(int i, RfInterface rfInterface) {
        log("Reader added LC message event, message counter: " + i + " on interface " + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerPasswordRequestEvent(byte[] data, RfInterface rfInterface) {
        log("Password request with bytes " + Utils.dataToByteString(data) + " interface:" + rfInterface);
    }


    //-----------------------------------------------------------------------------------------------------------------|
    // LegicNeonFileEventListener implementation
    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendNeonFileChangedEvent(NeonFile neonFile) {
        log("Neon File changed -> file " + neonFile);
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendRequestAddNeonFileDoneEvent(SdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            log("Backend Request add file done with status " + sdkStatus);
        } else {
            handleSdkErrors(sdkStatus);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendRequestRemoveNeonFileDoneEvent(SdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            log("Backend Request remove file done with status " + sdkStatus);
        } else {
            handleSdkErrors(sdkStatus);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerReadNeonFileEvent(NeonFile neonFile, RfInterface rfInterface) {
        log("Reader read Event on file  " + neonFile + " on interface " + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerWriteNeonFileEvent(NeonFile neonFile, RfInterface rfInterface) {
        log("Reader write Event on file  " + neonFile + " on interface " + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerReadNeonSubFileEvent(NeonSubFile subfile, NeonFile mainFile, RfInterface rfInterface) {
        log("Reader read Event on subfile " +  subfile + " attached to mainfile " + mainFile +
                " on interface " + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void readerWriteNeonSubFileEvent(NeonSubFile subfile, NeonFile mainFile, RfInterface rfInterface) {
        log("Reader write Event on subfile " +  subfile + " attached to mainfile " + mainFile +
                " on interface " + rfInterface);
    }

    //-----------------------------------------------------------------------------------------------------------------|
    private void handleSdkErrors(SdkStatus status) {
        // this method logs only when status is not "OK"
        if (!status.isSuccess()) {

            // LegicMobileSdkErrorReason gives more insight about the cause
            SdkErrorReason reason = status.getReason();

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

    //-------------------------------------------------------------------------|
    private void log(String msg) {
        //mLogManager.log(LogManager.LogLevel.LogLevelVerbose, mComponentName, msg);
    }

    //-------------------------------------------------------------------------|
    private void logException(String msg, Exception e) {
        //mLogManager.log(LogManager.LogLevel.LogLevelError, mComponentName, msg);
       // mLogManager.logException(e);
    }



}


package com.taj.doorunlock.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.assaabloy.mobilekeys.api.EndpointSetupConfiguration;
import com.assaabloy.mobilekeys.api.MobileKeys;
import com.assaabloy.mobilekeys.api.MobileKeysCallback;
import com.assaabloy.mobilekeys.api.MobileKeysException;
import com.assaabloy.mobilekeys.api.ReaderConnectionController;
import com.assaabloy.mobilekeys.api.ReaderConnectionInfoType;
import com.assaabloy.mobilekeys.api.ble.OpeningResult;
import com.assaabloy.mobilekeys.api.ble.OpeningStatus;
import com.assaabloy.mobilekeys.api.ble.OpeningType;
import com.assaabloy.mobilekeys.api.ble.Reader;
import com.assaabloy.mobilekeys.api.ble.ReaderConnectionCallback;
import com.assaabloy.mobilekeys.api.ble.ReaderConnectionListener;
import com.assaabloy.mobilekeys.api.ble.ScanConfiguration;
import com.assaabloy.mobilekeys.api.hce.HceConnectionCallback;
import com.assaabloy.mobilekeys.api.hce.HceConnectionListener;
import com.bumptech.glide.Glide;
import com.legic.mobile.sdk.api.exception.LegicMobileSdkException;
import com.legic.mobile.sdk.api.listener.LegicMobileSdkRegistrationEventListener;
import com.legic.mobile.sdk.api.types.LegicMobileSdkPushType;
import com.legic.mobile.sdk.api.types.LegicMobileSdkStatus;
import com.legic.mobile.sdk.api.types.RfInterface;
import com.taj.doorunlock.R;
import com.taj.doorunlock.activity.unlockactivity.DoorUlockFailScreem;
import com.taj.doorunlock.activity.unlockactivity.DoorUnlockActivity;
import com.taj.doorunlock.activity.unlockactivity.DoorUnlockSucessScreen;
import com.taj.doorunlock.activity.unlockactivity.DoormakabaUnlockActivity;
import com.taj.doorunlock.adapter.ActiveBookingAdapter;
import com.taj.doorunlock.helper.APIResponse;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.pojo.Data;
import com.taj.doorunlock.pojo.GeneralPojo;
import com.taj.doorunlock.pojo.doorunlock.DoorUnlock;
import com.taj.doorunlock.pojo.doorunlock.LegicToken;
import com.taj.doorunlock.pojo.doorunlock.Mobilekeys;
import com.taj.doorunlock.retrofit.ClientServiceGenerator;
import com.taj.doorunlock.services.APIMethods;
import com.taj.doorunlock.services.ApiListener;
import com.taj.doorunlock.unlock.ByteArrayHelper;
import com.taj.doorunlock.unlock.MobileKeysApiFacade;
import com.taj.doorunlock.unlock.MobileKeysApiFactory;
import com.taj.doorunlock.unlock.doormakaba.BaseActivity;
import com.taj.doorunlock.unlock.doormakaba.Settings;
import com.taj.doorunlock.unlock.doormakaba.Utils;
import com.taj.doorunlock.utils.NetworkChangeReceiver;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.taj.doorunlock.helper.GlobalClass.ShowAlet;
import static com.taj.doorunlock.helper.GlobalClass.edit;
import static com.taj.doorunlock.helper.GlobalClass.sharedPreferences;
import static com.taj.doorunlock.helper.GlobalClass.showPermissionDialoug;

import org.jetbrains.annotations.Nullable;


public class BookingDetailsListActivity extends BaseActivity implements ApiListener, MobileKeysApiFacade,
        ReaderConnectionListener,
        MobileKeysCallback,
        HceConnectionListener, SwipeRefreshLayout.OnRefreshListener, LegicMobileSdkRegistrationEventListener {


    private Context mContext;
    private ProgressDialog mDialog;
    private ProgressBar mLoading;
    private MobileKeysApiFacade mMobileKeysApiFacade;
    private MobileKeys mMobileKeys;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private ReaderConnectionCallback mReaderConnectionCallback;
    private HceConnectionCallback mHceConnectionCallback;
    public ReaderConnectionController mReaderConnectionController;
    private MobileKeysApiFactory mMobileKeysApiFactory;
    private ScanConfiguration mScanConfiguration;
    private RecyclerView mRecycler_active_booking;
    private FrameLayout mFragment_container;
    private LinearLayout mLyt_home;
    private TextView tv_InactiveBooking_message;
    private NestedScrollView nestedScroll;
    private TextView tv_user_name, tv_Hotel_location, tv_welcome;
    private RelativeLayout profile_lyt;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ActiveBookingAdapter adapter;
    private ImageView mBannerImage;
    private ProgressDialog dialog;
    private Handler mainHandler;
    private Data data;
    private Vibrator vibrator;
    private Handler handler;
    private MobileKeysApiFacade mobileKeysApiFacade;
   //private AppUpdateManager mAppUpdateManager;

    /*   @Override
       protected void onStart() {
           super.onStart();

           mAppUpdateManager = AppUpdateManagerFactory.create(this);

           mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

               if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                   try {
                       AlertDialog.Builder builder = new AlertDialog.Builder(this);
                       builder.setTitle("Update Available");
                       builder.setMessage("Please update the app to latest version")
                               .setCancelable(false)
                               .setPositiveButton("UPDATE", (dialog, id) -> {
                                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.mobisprint.aurika")));
                               });
                       AlertDialog alert = builder.create();
                       alert.show();
                       Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                       Button negativeButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
                       positiveButton.setTextColor(getResources().getColor(R.color.black));
                       negativeButton.setTextColor(getResources().getColor(R.color.black));
                   } catch (Exception e) {
                       e.printStackTrace();
                       e.getMessage();
                   }
               }
           });
       }*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_details_list_activity);
        try {
            mContext = this;
            mRecycler_active_booking = findViewById(R.id.recycler_active_booking);
            mLyt_home = findViewById(R.id.lyt_home);
            tv_Hotel_location = findViewById(R.id.tv_Hotel_location);
            mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_lyt);
            mFragment_container = findViewById(R.id.fragment_container);
            tv_InactiveBooking_message = findViewById(R.id.tv_InactiveBooking_message);
            mLoading = findViewById(R.id.loading);
            mLoading.setVisibility(View.GONE);
            nestedScroll = findViewById(R.id.nestedScroll);
            tv_user_name = findViewById(R.id.tv_user_name);
            mFragment_container.setVisibility(View.GONE);
            mLyt_home.setVisibility(View.VISIBLE);
            findViewById(R.id.nav_menu).setVisibility(View.GONE);
            findViewById(R.id.btn_back).setVisibility(View.GONE);
            profile_lyt = findViewById(R.id.profile_lyt);
            tv_welcome = findViewById(R.id.tv_welcome);
            mBannerImage = findViewById(R.id.img_banner);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mMobileKeysApiFacade = this;

            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            profile_lyt.setOnClickListener(v -> {

                performLogut(GlobalClass.mUser_token);

            });



            if(!sharedPreferences.getBoolean("requestedRuntimePermission", false)){
                getBooking(GlobalClass.mUser_token);
            }


            if (!(this instanceof MobileKeysApiFacade)) {
                throw new IllegalArgumentException("Error: attaching to context that doesn't implement MobileKeysApiFacade");
            }


            RegistorNetworkChnage();

            //initialize sdk callback methods
            onCreated();
            readCallBack();




            Intent intent = getIntent();
            if (intent.getStringExtra("fName") != null) {
                GlobalClass.mFirstName = intent.getStringExtra("fName");
            }
            GlobalClass.mManager = Utils.getSdkManager(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performLogut(String mUser_token) {
        Map<String, String> map = new HashMap<>();
        map.put("token", mUser_token);
        map.put("device_id", GlobalClass.mPrefix + GlobalClass.android_id + GlobalClass.mSufix);
        Call<GeneralPojo> logout = GlobalClass.mApi.logout(map);
        APIResponse.getCallRetrofit(logout, "logout", mContext, this);
    }

    private void RegistorNetworkChnage() {
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(mNetworkChangeReceiver, intentFilter);
    }


    private void getBooking(String user_token) {
        mLoading.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("token", user_token);
        map.put("device_id", GlobalClass.mPrefix + GlobalClass.android_id + GlobalClass.mSufix);
        Call<GeneralPojo> hotelDirectory = GlobalClass.mApi.getBookingDetails(map);
        APIResponse.getCallRetrofit(hotelDirectory, "bookings", mContext, this);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            mSwipeRefreshLayout.setRefreshing(false);
            mLoading.setVisibility(View.GONE);
            mRecycler_active_booking.setVisibility(View.VISIBLE);
            if (apiCallName.equalsIgnoreCase("bookings")) {
                GeneralPojo generalPojo = (GeneralPojo) response.body();
                if (generalPojo.getStatus()) {
                    tv_Hotel_location.setText(generalPojo.getData().getLocation());
                    if (generalPojo.getData() != null) {
                        GlobalClass.edit.apply();
                        tv_welcome.setVisibility(View.VISIBLE);
                        tv_InactiveBooking_message.setVisibility(View.GONE);
                        nestedScroll.setVisibility(View.VISIBLE);
                        data=generalPojo.getData();
                        setadapter(GlobalClass.mUser_token, generalPojo.getData(), 1);
                        tv_user_name.setText("Hello " + GlobalClass.mFirstName + "!");
                        Glide.with(this)
                                .load(generalPojo.getData().getLocationBanner().getSrc())
                                .error(R.drawable.banner_login)
                                .into(mBannerImage);

                        if (GlobalClass.hasLocationPermissions(mContext)) {
                            Log.d("Location_access","provided");
                            if (data.getLockType().equals("assaabloy") && !sharedPreferences.getBoolean("hasIvitationCode",false)) {
                                mAssabloyDoorUnlockApi(generalPojo);
                            } else if (data.getLockType().equals("dormakaba") && !sharedPreferences.getBoolean("isRegestrationComplete",false)) {
                                mDormakabaDoorUnlockApi(generalPojo);
                            }
                        }else{
                            if (!sharedPreferences.getBoolean("requestedRuntimePermission", false)) {
                                GlobalClass.edit.putBoolean("requestedRuntimePermission", true);
                                GlobalClass.edit.apply();
                                showPermissionDialoug(this);
                            }
                        }
                    } else {
                        tv_InactiveBooking_message.setVisibility(View.VISIBLE);
                        nestedScroll.setVisibility(View.GONE);
                        tv_welcome.setVisibility(View.GONE);
                        mRecycler_active_booking.setVisibility(View.GONE);
                        tv_Hotel_location.setText("");
                        tv_user_name.setText("");
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    Data data = new Data();
                    tv_Hotel_location.setText("");
                    tv_user_name.setText("");
                    tv_welcome.setVisibility(View.GONE);
                    setadapter(GlobalClass.mUser_token, data, 0);
                    nestedScroll.setVisibility(View.GONE);
                    mRecycler_active_booking.setVisibility(View.GONE);
                    tv_InactiveBooking_message.setVisibility(View.VISIBLE);
                    ShowAlet(mContext, "Alert", generalPojo.getMessage());
                }
            } else if (apiCallName.equalsIgnoreCase("logout")) {
                GeneralPojo generalPojo = (GeneralPojo) response.body();
                if (generalPojo.getStatus()) {

                    /*GlobalClass.edit.putBoolean("registrationComplete",false);
                    GlobalClass.edit.putBoolean("hasIvitationCode", false);
                    GlobalClass.edit.apply();*/
                   // MobileKeysApi.getInstance().getMobileKeys().unregisterEndpoint(this);
                    Intent intent = new Intent(this, UseAuthenticationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("logout", false);
                    startActivity(intent);
                    finish();
                }
            }  else if (apiCallName.equalsIgnoreCase("keys")) {
                dialog.dismiss();
                GeneralPojo generation = (GeneralPojo) response.body();
                if (generation.getStatus()) {
                    dialog.dismiss();
                    //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new DoorUnlockFragment()).addToBackStack(null).commit();

                    GlobalClass.edit.putBoolean("isRegestrationComplete", true).apply();
                    edit.commit();
                    GlobalClass.user_registered = true;
                    GlobalClass.edit.putBoolean("isSyncComplete", false).apply();
                    try {
                        if (!GlobalClass.mManager.isStarted()) {
                            GlobalClass.mManager.start(Settings.mobileAppId, Settings.mobileAppTechUser, Settings.mobileAppTechPassword,
                                    Settings.serverUrl);
                        }
                    } catch (LegicMobileSdkException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(mContext, DoormakabaUnlockActivity.class);
                    intent.putExtra("room_no","1006");
                    startActivity(intent);


                } else {
                    dismissDialog();
                    GlobalClass.ShowAlet(mContext, "Alert!!",generation.getMessage());

                }
            } else if (apiCallName.equalsIgnoreCase("getToken")) {

                LegicToken legicToken = (LegicToken) response.body();
                if (response.isSuccessful() && legicToken.getData().getPrepareCustomRegistrationResponse().getStatus()
                        .getDescription().equalsIgnoreCase("OK")) {

                    GlobalClass.mLegicToken = legicToken.getData().getPrepareCustomRegistrationResponse().getToken();
                    // get push token (if available)
                    String token = Utils.getPushToken();
                    List<RfInterface> interfaces = new ArrayList<>();
                    try {
                        boolean ble = GlobalClass.mManager.isRfInterfaceSupported(RfInterface.BLE);
                        if (ble) {
                            interfaces.add(RfInterface.BLE);
                        }
                    } catch (LegicMobileSdkException e) {
                        dialog.dismiss();
                        e.getLocalizedMessage();
                    }
                    GlobalClass.mManager.initiateRegistration(
                            "73512478-" + GlobalClass.mPrefix + GlobalClass.android_id + GlobalClass.mSufix,
                            interfaces,
                            Settings.lcConfirmationMethod,
                            token,
                            LegicMobileSdkPushType.GCM);
                } else {
                    dismissDialog();
                    GlobalClass.ShowAlet(mContext, "Alert!!", legicToken.getData().getPrepareCustomRegistrationResponse().getStatus().getDescription());
                }
            }
        } catch (Exception e) {
            dismissDialog();
            e.printStackTrace();
        }
    }

    private void RequestPermissonfromSettings() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Allow location permission to unlock the door")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) ->
                        {
                            dialog.dismiss();

                        }

                )
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.dismiss();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void mDormakabaDoorUnlockApi(GeneralPojo generalPojo) {
        mDormakabaDoorUnlock(GlobalClass.mUser_token ,generalPojo.getData());
    }

    private void mAssabloyDoorUnlockApi(GeneralPojo generalPojo) {
        if (!(GlobalClass.user_registered && sharedPreferences.getBoolean("isRegestrationComplete",false))) {
            Log.d("new_user","yes");
            if (!sharedPreferences.getBoolean("hasIvitationCode", false)) {
                GlobalClass.edit.putString("reservation_key", generalPojo.getData().getReservation_key());
                getInvitationCode(GlobalClass.mUser_token, generalPojo.getData());
            } else if (!sharedPreferences.getString("reservation_key", "").equalsIgnoreCase(generalPojo.getData().getReservation_key())) {
                GlobalClass.edit.putString("reservation_key", generalPojo.getData().getReservation_key());
                mobilekeyapi(GlobalClass.mUser_token, generalPojo.getData());
            }
        }else{
            mobilekeyapi(GlobalClass.mUser_token,generalPojo.getData());
        }
    }


    @Override
    public void onErrorListner() {
        try {
            mLoading.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setadapter(String user_token, Data data, Integer size) {


        adapter = new ActiveBookingAdapter(size, data, v -> {
            try {


                if (data.getLockType().equals("assaabloy")) {

                    mAssabloyDoorUnlock(user_token, data);

                } else if (data.getLockType().equals("dormakaba")) {
                    mDormakabaDoorUnlock(user_token, data);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecycler_active_booking.setLayoutManager(mLayoutManager);
        mRecycler_active_booking.setNestedScrollingEnabled(false);
        mRecycler_active_booking.setItemAnimator(new DefaultItemAnimator());
        mRecycler_active_booking.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void mAssabloyDoorUnlock(String user_token, Data data) {
        if (!sharedPreferences.getBoolean("key_generated",false)) {
            GlobalClass.edit.putString("reservation_key", data.getReservation_key());
            getInvitationCode(user_token, data);
        } else if (!sharedPreferences.getString("reservation_key", "").equalsIgnoreCase(data.getReservation_key())) {
            GlobalClass.edit.putString("reservation_key", data.getReservation_key());
            mobilekeyapi(user_token, data);
         }else {


                Fragment fragment = new DoorUnlockActivity();
                Bundle bundle = new Bundle();
                bundle.putString("room_no", data.getRooms());


                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, fragment, "LOGIN_TAG")
                        .addToBackStack(null)
                        .commit();
                 mFragment_container.setVisibility(View.VISIBLE);
                mLyt_home.setVisibility(View.GONE);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mFragment_container.setVisibility(View.GONE);
        mLyt_home.setVisibility(View.VISIBLE);
    }

    //on create initialize all sdk keys
    private void onCreated() {
        try {
            MobileKeysApiFacade mobileKeysApiFacade = this;
            mMobileKeysApiFactory = (MobileKeysApiFactory) getApplication();
            mMobileKeys = mMobileKeysApiFactory.getMobileKeys();
            mReaderConnectionController = mMobileKeysApiFactory.getReaderConnectionController();
            mScanConfiguration = mMobileKeysApiFactory.getScanConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //read connection call backs from sdk
    private void readCallBack() {
        try {
            mReaderConnectionCallback = new ReaderConnectionCallback(getApplicationContext());
            mReaderConnectionCallback.registerReceiver(this);
            mHceConnectionCallback = new HceConnectionCallback(getApplicationContext());
            mHceConnectionCallback.registerReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public MobileKeys getMobileKeys() {
        return mMobileKeysApiFactory.getMobileKeys();
    }

    @Override
    public void onStartUpComplete() {

        showEndpointSetupFragmentIfNotSetup();
    }

    @Override
    public void onEndpointSetUpComplete() {
        Log.d("setupComplete", "completed");
    }

    @Override
    public void endpointNotPersonalized() {

    }


    @Override
    public void onReaderConnectionOpened(Reader reader, OpeningType openingType) {
    }


    //on successful door unlock navigate to main screen with vibration alert
    @Override
    public void onReaderConnectionClosed(Reader reader, OpeningResult openingResult) {
        try {
            Fragment fragment;
            byte[]payload = openingResult.getStatusPayload();
            if (ByteArrayHelper.containsData(payload)) {
                if (ByteArrayHelper.didUnlock(payload)) {
                    fragment = new DoorUnlockSucessScreen();
                  /*  Bundle bundle = new Bundle();
                    if(openingResult.getStatusPayload().length>0) {
                        bundle.putString("room_no", String.valueOf(openingResult.getStatusPayload()[6]));
                    }
                    fragment.setArguments(bundle);*/
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_container, fragment, "LOGIN_TAG")
                            .commit();

                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(200);
                    }
                } else {
                    fragment = new DoorUlockFailScreem();
                    Bundle bundle = new Bundle();
       /*             if(openingResult.getStatusPayload().length>0) {
                        bundle.putString("room_no", String.valueOf(openingResult.getStatusPayload()[6]));
                    }

                    fragment.setArguments(bundle);*/

                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_container, fragment, "LOGIN_TAG")
                            .commit();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onReaderConnectionFailed(Reader reader, OpeningType openingType, OpeningStatus openingStatus) {
    }

    @Override
    public void onHceSessionOpened() {
    }

    @Override
    public void onHceSessionClosed(int i) {

    }

    @Override
    public void onHceSessionInfo(ReaderConnectionInfoType hceConnectionInfoType) {
    }


    //is end point setup complete return's the staus of invitation key registration
    @Override
    public boolean isEndpointSetUpComplete() {
        boolean isEndpointSetup = false;
        try {
            isEndpointSetup = mMobileKeys.isEndpointSetupComplete();

        } catch (MobileKeysException e) {
            e.printStackTrace();
        }
        return isEndpointSetup;
    }

    @Override
    public ReaderConnectionController getReaderConnectionController() {
        return mReaderConnectionController;
    }

    @Override
    public ScanConfiguration getScanConfiguration() {
        return mScanConfiguration;
    }


    private void showEndpointSetupFragmentIfNotSetup() {
        try {
            if (mMobileKeys.isEndpointSetupComplete()) {
                onEndpointSetUpComplete();
            }
        } catch (MobileKeysException exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void handleMobileKeysTransactionCompleted() {

    }

    @Override
    public void handleMobileKeysTransactionFailed(MobileKeysException e) {

    }

    public static boolean shouldRetry(MobileKeysException exception) {
        boolean shouldRetry = false;
        switch (exception.getErrorCode()) {
            case INTERNAL_ERROR:
            case SERVER_UNREACHABLE:
            case SDK_BUSY:
                shouldRetry = true;
                break;
            case INVALID_INVITATION_CODE:
            case DEVICE_SETUP_FAILED:
            case SDK_INCOMPATIBLE:
            case DEVICE_NOT_ELIGIBLE:
            case ENDPOINT_NOT_SETUP:
            default:
                break;
        }
        return shouldRetry;
    }

    //unregister callbacks and receivers
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);
        mReaderConnectionCallback.unregisterReceiver();
        mHceConnectionCallback.unregisterReceiver();
    }

    //door unlock invitation code api call
    private void getInvitationCode(String user_token, Data data) {
        try {
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("please wait..");
            mDialog.setCancelable(false);
            mDialog.show();
            Map<String, String> map = new HashMap<>();
            map.put("token", user_token);
            map.put("location_key", data.getLocation_key());
            map.put("device_id", GlobalClass.mPrefix + GlobalClass.android_id + GlobalClass.mSufix);
            APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
            Call<DoorUnlock> call = api.getInvitationCode(map);
            call.enqueue(new Callback<DoorUnlock>() {
                @Override
                public void onResponse(Call<DoorUnlock> call, Response<DoorUnlock> response) {
                    try {
                        if (response.isSuccessful()) {
                            DoorUnlock doorUnlock = (DoorUnlock) response.body();
                            if (doorUnlock.getStatus()) {
                                dismissDialog();
                                if (doorUnlock.getData().getInvitationCode() != null && !(doorUnlock.getData().getInvitationCode().isEmpty())) {
                                    GlobalClass.edit.putBoolean("hasIvitationCode", true);
                                    GlobalClass.edit.apply();
                                    submitInvitationCode(doorUnlock.getData().getInvitationCode(), user_token, data);
                                } else {
                                    errorHandler("Something Went wrong please contact front desk");
                                }

                            } else {

                                dismissDialog();
                                errorHandler(doorUnlock.getMessage());
                            }
                        } else {
                            errorHandler(getString(R.string.ERROR));
                        }
                    } catch (Exception e) {

                        dismissDialog();
                        e.printStackTrace();
                        e.getMessage();
                    }

                }

                @Override
                public void onFailure(Call<DoorUnlock> call, Throwable t) {
                    try {
                        if (t instanceof SocketTimeoutException) {
                            errorHandler(getString(R.string.SOCKET_ISSUE));
                        } else {
                            errorHandler(getString(R.string.NETWORK_ISSUE));
                        }
                    } catch (Exception e) {

                        dismissDialog();
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            dismissDialog();
        }
    }

    private void errorHandler(String error) {
        dismissDialog();
        GlobalClass.ShowAlet(mContext, "Alert", error);

    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    //submit invitation code to SDK
    private void submitInvitationCode(String invitationCode, String user_token, Data data) {
        try {
            mMobileKeysApiFacade.getMobileKeys().endpointSetup(this,
                    invitationCode, new EndpointSetupConfiguration.Builder().build());
            mDialog.setMessage("please wait, while we are registering your phone for mobile key");
            mDialog.setCancelable(false);
            mDialog.show();
            checkInvitionComplet(user_token, data);
        } catch (Exception e) {
            e.printStackTrace();
            dismissDialog();
        }
    }


    //Check if invitation code registration successful
    @SuppressLint("LongLogTag")
    private void checkInvitionComplet(String user_token, Data data) {
        try {
            new Handler().postDelayed(() -> {
                if (mMobileKeysApiFacade.isEndpointSetUpComplete()) {
                    edit.putBoolean("registrationComplete",true);
                    mMobileKeysApiFacade.getMobileKeys().endpointUpdate(this);
                    edit.putBoolean("hasInvitationCode", true);
                    edit.apply();
                    mobilekeyapi(user_token, data);
                } else {
                    checkInvitionComplet(user_token, data);
                }
            }, 5000);
        } catch (Exception e) {
            e.printStackTrace();
            dismissDialog();
        }
    }


    //api call to get mobilekeys
    private void mobilekeyapi(String user_token, Data data) {
        try {
            dismissDialog();
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("please wait, while we are creating your mobile key");
            mDialog.setCancelable(false);
            mDialog.show();
            Map map = new HashMap();
            map.put("token", user_token);
            map.put("location_key", data.getLocation_key());
            map.put("device_id", GlobalClass.mPrefix + GlobalClass.android_id + GlobalClass.mSufix);

            Call<DoorUnlock> call = GlobalClass.mApi.mobilekeyapi(map);
            call.enqueue(new Callback<DoorUnlock>() {
                @Override
                public void onResponse(Call<DoorUnlock> call, Response<DoorUnlock> response) {
                    try {
                        if (response.isSuccessful()) {
                            DoorUnlock doorUnlock = response.body();
                            if (doorUnlock.getStatus()) {

                                dismissDialog();

                                GlobalClass.key_generated = true;
                                edit.putBoolean("key_generated",true);
                                GlobalClass.user_registered = true;
                                GlobalClass.edit.putBoolean("isRegestrationComplete",true);
                                GlobalClass.edit.apply();
                                GlobalClass.edit.commit();


                                if (mMobileKeysApiFacade.isEndpointSetUpComplete()) {
                                    mMobileKeysApiFacade.onEndpointSetUpComplete();
                                }

                               /* Fragment fragment = new DoorUnlockActivity();
                                Bundle bundle = new Bundle();
                                bundle.putString("room_no", data.getRooms());

                                fragment.setArguments(bundle);

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "LOGIN_TAG")
                                        .addToBackStack(null)
                                        .commit();
                                mFragment_container.setVisibility(View.VISIBLE);
                                mLyt_home.setVisibility(View.GONE);*/
                            } else {
                                dismissDialog();
                                GlobalClass.edit.putBoolean("hasIvitationCode", false);
                                GlobalClass.edit.apply();
                                GlobalClass.ShowAlet(mContext, "Alert", doorUnlock.getMessage());
                            }
                        } else {
                            dismissDialog();
                            GlobalClass.edit.putBoolean("hasIvitationCode", false);
                            GlobalClass.edit.apply();
                            errorHandler(getString(R.string.ERROR));
                        }
                    } catch (Exception e) {
                        GlobalClass.edit.putBoolean("hasIvitationCode", false);
                        GlobalClass.edit.apply();
                        dismissDialog();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<DoorUnlock> call, Throwable t) {
                    try {
                        if (t instanceof SocketTimeoutException) {
                            Log.d("mobile key exception", t.getMessage());
                            errorHandler(getString(R.string.SOCKET_ISSUE));

                        } else {
                            Log.d("mobile key exception", t.getMessage());
                            errorHandler(getString(R.string.NETWORK_ISSUE));
                        }
                    } catch (Exception e) {
                        dismissDialog();
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            dismissDialog();
            e.getMessage();
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        Log.d("onrefresh", "booking called");
        getBooking(GlobalClass.mUser_token);
        mSwipeRefreshLayout.setRefreshing(true);

    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!GlobalClass.mManager.isStarted()) {

                GlobalClass.mManager.start(Settings.mobileAppId, Settings.mobileAppTechUser, Settings.mobileAppTechPassword,
                        Settings.serverUrl);
            }
        } catch (LegicMobileSdkException e) {

            Log.d("e.message",e.getMessage());

            e.printStackTrace();
        }

        registerListeners();

        if(sharedPreferences.getBoolean("requestedRuntimePermission", false)){
            getBooking(GlobalClass.mUser_token);
        }
    }

    private void mDormakabaDoorUnlock(String user_token, Data data) {

        if (!sharedPreferences.getBoolean("isRegestrationComplete", false) && !GlobalClass.user_registered ) {
            try {

                if (!GlobalClass.mManager.isRegisteredToBackend()) {
                    GlobalClass.edit.putString("reservation_key", data.getReservation_key());
                    gettoken(data);
                } else {
                    getkeyfiles(data);
                }
            } catch (LegicMobileSdkException e) {
                e.printStackTrace();
            }
        } else if
        (!sharedPreferences.getString("reservation_key", "").equalsIgnoreCase(data.getReservation_key())) {
            // multiple room api call here
            // mobilekeyapi(MY_ROOMS.get(v).getRoom().getRoomNo());
            GlobalClass.edit.putString("reservation_key", data.getReservation_key());
            edit.apply();
            getkeyfiles(data);
        } else {
            try {

                if (!GlobalClass.mManager.isStarted()) {
                    GlobalClass.mManager.start(Settings.mobileAppId, Settings.mobileAppTechUser, Settings.mobileAppTechPassword,
                            Settings.serverUrl);
                }
            } catch (LegicMobileSdkException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(mContext, DoormakabaUnlockActivity.class);
            intent.putExtra("room_no", data.getRooms());
            startActivity(intent);
        }
    }




    //door unlock  api call
    private void gettoken(Data data) {
        try {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("please wait while we register your device...");
            dialog.setCancelable(false);
            dialog.show();
            HashMap map = new HashMap();
            map.put("location_key", data.getLocation_key());
            map.put("device_id", GlobalClass.mPrefix + GlobalClass.android_id + GlobalClass.mSufix);
            Call<LegicToken> keyGenerationCall = GlobalClass.mApi.getToken(map);
            APIResponse.postCallRetrofit(keyGenerationCall, "getToken", mContext, this);
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }

    private void getkeyfiles(Data data) {
        try {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("please wait while we generate mobile keys...");
            dialog.setCancelable(false);
            dialog.show();
            HashMap map = new HashMap();
            map.put("token", GlobalClass.mUser_token);
            map.put("location_key", data.getLocation_key());
            map.put("device_id", GlobalClass.mPrefix + GlobalClass.android_id + GlobalClass.mSufix);
            Call<GeneralPojo> keyGenerationCall = GlobalClass.mApi.getkeyfiles(map);
            APIResponse.postCallRetrofit(keyGenerationCall, "keys", mContext, this);
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }


    //-----------------------------------------------------------------------------------------------------------------|
    public void registerListeners() {

       // Log.d("mManager", String.valueOf(GlobalClass.mManager));

        if (GlobalClass.mManager == null) {
            return;
        }
        try {
            GlobalClass.mManager.registerForRegistrationEvents(this);

        } catch (LegicMobileSdkException e) {
            e.getLocalizedMessage();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------|
    public void unregisterListeners() {
        if (GlobalClass.mManager == null) {
            return;
        }
        GlobalClass.mManager.unregisterAnyEvents(this);
    }


    //-----------------------------------------------------------------------------------------------------------------|
    public void initSdk() throws LegicMobileSdkException {
        GlobalClass.mManager = Utils.getSdkManager(mContext.getApplicationContext());

        registerListeners();

        if (!GlobalClass.mManager.isStarted()) {
            GlobalClass.mManager.start(Settings.mobileAppId, Settings.mobileAppTechUser, Settings.mobileAppTechPassword,
                    Settings.serverUrl);
        }

        GlobalClass.mManager.setLcProjectAddressingMode(true);

        if (GlobalClass.mManager.isRegisteredToBackend()) {
            if (GlobalClass.mManager.isRfInterfaceSupported(RfInterface.BLE) && !GlobalClass.mManager.isRfInterfaceActive(RfInterface.BLE)) {
                GlobalClass.mManager.activateRfInterface(RfInterface.BLE);
            }
        }
    }

    @Override
    public void backendRegistrationStartDoneEvent(LegicMobileSdkStatus sdkStatus) {
        if (sdkStatus.isSuccess()) {
            try {
                dialog.setMessage("please wait while we initilize your device...");

                initSdk();

                GlobalClass.mManager.register(GlobalClass.mLegicToken);
            } catch (LegicMobileSdkException e) {
                dismissDialog();
                e.printStackTrace();
            }

        }

    }

    //-----------------------------------------------------------------------------------------------------------------|
    @Override
    public void backendRegistrationFinishedDoneEvent(LegicMobileSdkStatus legicSdkStatus) {
        // error handling / logging is done in base class
        if (legicSdkStatus.isSuccess()) {
            mainHandler = new Handler(mContext.getMainLooper());
            // This is your code
            Runnable myRunnable = () -> {
                dialog.dismiss();
                getkeyfiles(data);
            };
            mainHandler.post(myRunnable);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterListeners();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (mainHandler != null) {
            mainHandler.removeCallbacks(null);
        }
    }

    @Override
    public void backendUnregisterDoneEvent(LegicMobileSdkStatus sdkStatus) {

    }


}

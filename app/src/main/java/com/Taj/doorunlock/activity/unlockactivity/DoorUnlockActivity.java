package com.taj.doorunlock.activity.unlockactivity;

import android.Manifest;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.assaabloy.mobilekeys.api.MobileKey;
import com.assaabloy.mobilekeys.api.MobileKeysApi;
import com.assaabloy.mobilekeys.api.MobileKeysCallback;
import com.assaabloy.mobilekeys.api.MobileKeysException;
import com.assaabloy.mobilekeys.api.ReaderConnectionController;
import com.assaabloy.mobilekeys.api.ble.OpeningResult;
import com.assaabloy.mobilekeys.api.ble.OpeningStatus;
import com.assaabloy.mobilekeys.api.ble.OpeningType;
import com.assaabloy.mobilekeys.api.ble.Reader;
import com.assaabloy.mobilekeys.api.ble.ReaderConnectionCallback;
import com.assaabloy.mobilekeys.api.ble.ReaderConnectionListener;
import com.taj.doorunlock.R;
import com.taj.doorunlock.activity.BookingDetailsListActivity;
import com.taj.doorunlock.helper.GlobalClass;
import com.taj.doorunlock.helper.ProgressBarAnimation;
import com.taj.doorunlock.unlock.ByteArrayHelper;
import com.taj.doorunlock.unlock.ClosestLockTrigger;
import com.taj.doorunlock.unlock.MobileKeysApiFacade;
import com.taj.doorunlock.unlock.UnlockNotification;
import com.taj.doorunlock.utils.BluetoothChangeReceiver;
import com.taj.doorunlock.utils.GpsLocationReceiver;

import java.util.Collections;
import java.util.List;

import static com.taj.doorunlock.helper.GlobalClass.sharedPreferences;
import static com.taj.doorunlock.helper.GlobalClass.showPermissionDialoug;


public class DoorUnlockActivity extends Fragment
        implements View.OnClickListener, MobileKeysCallback, ClosestLockTrigger.LockInRangeListener,
        SwipeRefreshLayout.OnRefreshListener, ReaderConnectionListener {


    private static final int REQUEST_LOCATION_PERMISSION = 10;
    private MobileKeysApiFacade mobileKeysApiFacade;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ClosestLockTrigger closestLockTrigger = new ClosestLockTrigger(this);
    private BluetoothAdapter mBluetoothAdapter;
    private Handler handler;
    private Context mContext;
    private BluetoothChangeReceiver mBluetoothReceiver;
    private GpsLocationReceiver mLocationReceiver;

    private ReaderConnectionCallback readerConnectionCallback;
    private String room_no = "";
    private Vibrator vibrator;

    private List<MobileKey> data = null;
    private ProgressBar mTimer_Progressbar;

    private String  mRoom_No;

    boolean permissionGranted = true;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_door_unlock, container, false);
        try {
            mContext = getContext();
              Bundle data = getArguments();


            mRoom_No=data.getString("room_no","");


            vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mTimer_Progressbar = view.findViewById(R.id.timer_progressbar);



            updateApiAction();

            // onRefresh();

            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof MobileKeysApiFacade)) {
            throw new IllegalArgumentException("Error: attaching to context that doesn't implement MobileKeysApiFacade");
        }
        mobileKeysApiFacade = (MobileKeysApiFacade) context;
    }

    private void updateApiAction() {
        readerConnectionCallback = new ReaderConnectionCallback(mContext.getApplicationContext());
        readerConnectionCallback.registerReceiver(this);
        mobileKeysApiFacade.getMobileKeys().endpointUpdate(this);
        mobileKeysApiFacade.isEndpointSetUpComplete();
    }


    /**
     * Load mobile keys api
     */
    private void loadKeys() {


        try {


                data = mobileKeysApiFacade.getMobileKeys().listMobileKeys();


                Log.d("keysize", String.valueOf(data.size()));
/*
            for(int i=0;i<mobileKeysApiFacade.getMobileKeys().listMobileKeys().size();i++){
                *//*  if(mobileKeysApiFacade.getMobileKeys().listMobileKeys().get(i).getLabel().contains(room_no)){*//*

                // data.add(mobileKeysApiFacade.getMobileKeys().listMobileKeys().get(i));
                Toast.makeText(mContext, data.get(i).getLabel(),Toast.LENGTH_SHORT).show();
                 Log.d("card_no",data.get(i).getLabel());

                *//*     }
             *//*

            }*/
        } catch (MobileKeysException e) {
            e.printStackTrace();
            e.getMessage();
        }
        if (data == null) {
            data = Collections.emptyList();
        }



    }


    /**
     * Start BLE scanning or request permission
     */
    private void startScanning() {


            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                startReading();
            } else {
                startReading();
            }
    }

    private void startReading() {
        ReaderConnectionController controller = MobileKeysApi.getInstance().getReaderConnectionController();
        controller.enableHce();
        Notification notification = UnlockNotification.create(requireContext());
        controller.startForegroundScanning(notification);
    }


    /**
     * Stop BLE scanning or
     */
    private void stopScanning() {
        try {

            ReaderConnectionController controller = MobileKeysApi.getInstance().getReaderConnectionController();
            controller.stopScanning();
//        controller.disableHce();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if app has location permission
     *
     * @return
     */


    /**
     * Request location permission, location permission is required for BLE scanning when running Marshmallow or above
     */




    private Boolean statusCheck() {
        final LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            return true;
        }
        return false;
    }

    /*private boolean hasRequiredPermissions()
    {

        boolean permissionGranted = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionGranted &= ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
            return permissionGranted;
        }

        permissionGranted &= ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionGranted &= ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        return permissionGranted;
    }*/

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Please turn on location to unlock Door")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        (dialog, id) -> {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                permissionGranted &= ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
                            }
                            permissionGranted &= ContextCompat.checkSelfPermission(requireContext(),
                                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }

                        })
                .setNegativeButton("No", (dialog, id) -> {
                    Intent intent = new Intent(mContext, BookingDetailsListActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /*private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Please turn on location to unlock Door")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> {
                    Intent intent = new Intent(mContext, BookingDetailsListActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                });
        final AlertDialog alert = builder.create();
        alert.show();
    }*/


    /**
     * Callback from closest lock trigger.
     * Toggle button
     *
     * @param lockInRange
     */
    @Override
    public void onLockInRange(final boolean lockInRange) {

        toggleOpenButton(lockInRange);
    }


    private void toggleOpenButton(boolean enabled) {


       /* if (!enabled) {
            setUiComponents();
        }*/

    }

    /**
     * Mobile keys transaction success/completed callback
     */
    @Override
    public void handleMobileKeysTransactionCompleted() {
        //    swipeRefreshLayout.setRefreshing(false);

       // loadKeys();
    }


    /**
     * Mobile keys failed callback
     *
     * @param mobileKeysException
     */
    @Override
    public void handleMobileKeysTransactionFailed(MobileKeysException mobileKeysException) {
        //  swipeRefreshLayout.setRefreshing(false);
        switch (mobileKeysException.getErrorCode()) {
            case ENDPOINT_NOT_SETUP:
                mobileKeysApiFacade.endpointNotPersonalized();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //Listen to lock changes
        try {
            loadKeys();
            mobileKeysApiFacade.getScanConfiguration().getRootOpeningTrigger().add(closestLockTrigger);
            toggleOpenButton(false);
            if (!GlobalClass.sharedPreferences.getBoolean("hasInvitationCode", false)) {
                try {
                    mobileKeysApiFacade.getMobileKeys().unregisterEndpoint(this);
                    mobileKeysApiFacade.getMobileKeys().listMobileKeys().clear();
                } catch (MobileKeysException e) {
                    e.printStackTrace();
                }
            }

            mLocationReceiver = new GpsLocationReceiver();
            mBluetoothReceiver = new BluetoothChangeReceiver();
            IntentFilter bluetoothFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
            getActivity().registerReceiver(mBluetoothReceiver, bluetoothFilter);
            IntentFilter locationFilter = new IntentFilter("android.location.PROVIDERS_CHANGED");
            getActivity().registerReceiver(mLocationReceiver, locationFilter);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissionGranted &= ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
            }

           /* mTimer=   new CountDownTimer(10000, 1000) {

                public void onTick(long millisUntilFinished) {

                   // mTimer_Progressbar.setProgress((int)millisUntilFinished/100);
                   // mTimer_Progressbar.setProgress((int) (millisUntilFinished / 100));
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    mTimer_Progressbar.setProgress(0);
                }

            }.start();

*/
startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        if (GlobalClass.hasLocationPermissions(mContext) || Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            if(statusCheck()){
                handler = new Handler();
                handler.postDelayed(() -> {
                    try {
                        Intent intent = new Intent(mContext, BookingDetailsListActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 20000);


                ProgressBarAnimation anim = new ProgressBarAnimation(mTimer_Progressbar, 100, 0);
                anim.setDuration(20100);
                mTimer_Progressbar.startAnimation(anim);
                //Update scanning based if we have keys
                if (data.isEmpty()) {
                    stopScanning();
                } else {
                    startScanning();
                }
                if(GlobalClass.mLocationPermission!=null && GlobalClass.mLocationPermission.isShowing()) {
                    GlobalClass.mLocationPermission.dismiss();
                }
            }

        }else {
            RequestPermissonfromSettings();

        }
    }

    private void RequestPermissonfromSettings() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Allow location permission to unlock the door")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) ->
                        {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                permissionGranted &= ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
                            }

                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",getActivity().getPackageName(), null);
                                intent.setData(uri);
                                getActivity().startActivity(intent);
                            }

                        }

                )
                .setNegativeButton("No", (dialog, id) -> {
                    Intent intent = new Intent(mContext, BookingDetailsListActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onPause() {
        super.onPause();

        //Stop listening to lock changes
        toggleOpenButton(false);

        mobileKeysApiFacade.getScanConfiguration().getRootOpeningTrigger().remove(closestLockTrigger);
        //Stop scanning
        stopScanning();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        stopScanning();
        getActivity().unregisterReceiver(mBluetoothReceiver);
        getActivity().unregisterReceiver(mLocationReceiver);

    }


    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.tv_unlock_status:
                // Send broadcast to custom opening trigger to open closest reader
                closestLockTrigger.openClosestReader();

                break;
           *//* case android.support.design.R.id.snackbar_action:
                updateApiAction();
                break;
*//*
        }*/
    }

    @Override
    public void onRefresh() {
        updateApiAction();
        //  loadKeys();
    }


    /**
     * Permission request callback
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            if (requestCode == REQUEST_LOCATION_PERMISSION) {
                //Update scanning based if we have keys
                if (data.isEmpty()) {
                    stopScanning();
                } else {
                    startScanning();
                }
                if (GlobalClass.mLocationPermission.isShowing()) {
                    GlobalClass.mLocationPermission.dismiss();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
       try{
           if(GlobalClass.mLocationPermission!=null && GlobalClass.mLocationPermission.isShowing()) {
               GlobalClass.mLocationPermission.dismiss();
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }


    @Override
    public void onReaderConnectionOpened(Reader reader, OpeningType openingType) {

    }

    @Override
    public void onReaderConnectionClosed(Reader reader, OpeningResult openingResult) {

         /*   if (mRoom_No!=null && mRoom_No.contains(String.valueOf(openingResult.getStatusPayload()[6]))) {
                Fragment fragment = new DoorUnlockSucessScreen();
                Bundle bundle = new Bundle();
                if(openingResult.getStatusPayload().length>0) {
                    bundle.putString("room_no", String.valueOf(openingResult.getStatusPayload()[6]));
                }

                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, fragment, "LOGIN_TAG")
                        .commit();

                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(200);
                }
            } else {
                Fragment fragment = new DoorUlockFailScreem();
                Bundle bundle = new Bundle();
                if(openingResult.getStatusPayload().length>0) {
                    bundle.putString("room_no", String.valueOf(openingResult.getStatusPayload()[6]));
                }

                fragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, fragment, "LOGIN_TAG")
                        .commit();
            }
            handler = new Handler();
            handler.postDelayed(() -> {
                try {
                    Intent intent = new Intent(mContext, BookingDetailsListActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 2000);*/

    }

    @Override
    public void onReaderConnectionFailed(Reader reader, OpeningType openingType, OpeningStatus openingStatus) {

    }

}

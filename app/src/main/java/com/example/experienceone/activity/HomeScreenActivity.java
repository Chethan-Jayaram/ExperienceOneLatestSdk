package com.example.experienceone.activity;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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
import com.example.experienceone.R;
import com.example.experienceone.fragment.general.EditProfile;
import com.example.experienceone.fragment.general.HomeGridFragment;
import com.example.experienceone.fragment.general.SOS;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.unlock.MobileKeysApiFacade;
import com.example.experienceone.unlock.MobileKeysApiFactory;
import com.example.experienceone.utils.NetworkChangeReceiver;
import com.google.android.material.navigation.NavigationView;

import java.util.List;


public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MobileKeysApiFacade,
        ReaderConnectionListener,
        MobileKeysCallback,
        HceConnectionListener {

    private static final String TAG = HomeScreenActivity.class.getName();
    private MobileKeys mobileKeys;
    private DrawerLayout drawer;
    private View headerLayout;
    private ReaderConnectionCallback readerConnectionCallback;
    private HceConnectionCallback hceConnectionCallback;
    private ReaderConnectionController readerConnectionController;
    private MobileKeysApiFactory mobileKeysApiFactory;
    private ScanConfiguration scanConfiguration;
    private Vibrator vibrator;
    private ImageView nav_menu;
    private ImageView iv_sos;
    private ImageView img_et_btn;
    private NetworkChangeReceiver mNetworkChangeReceiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            nav_menu = findViewById(R.id.nav_menu);
            ImageView btn_back = findViewById(R.id.btn_back);
            NavigationView navigationView = findViewById(R.id.nav_view);
            iv_sos = findViewById(R.id.iv_sos);
            drawer = findViewById(R.id.drawer_layout);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            RegistorNetworkChnage();


            Intent intent = getIntent();

            String str=intent.getStringExtra("changes");
            if(str.equalsIgnoreCase("ble")){
                GlobalClass.showErrorMsg(this,"Bluetooth Turned off");
            }else if(str.equalsIgnoreCase("loc")){
                GlobalClass.showErrorMsg(this,"Location turned off");
            }




            //initialize sdk callback methods
            onCreated();
            readCallBack();

            navigationView.setNavigationItemSelectedListener(this);


            this.getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new HomeGridFragment()).commit();

            nav_menu.setOnClickListener(v -> {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    drawer.openDrawer(GravityCompat.START);
            });

            btn_back.setOnClickListener(v -> {
                onBackPressed();
            });

            headerLayout = navigationView.getHeaderView(0);

            img_et_btn = headerLayout.findViewById(R.id.img_et_btn);

            img_et_btn.setOnClickListener(v -> {
                this.getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new EditProfile()).addToBackStack(null).commit();
                onBackPressed();
            });

            iv_sos.setOnClickListener(v -> {
                this.getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new SOS()).addToBackStack(null).commit();

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RegistorNetworkChnage() {
        mNetworkChangeReceiver=new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(mNetworkChangeReceiver, intentFilter);
    }


    @Override
    public void onBackPressed() {
        try {
            onResume();
            if (GlobalClass.flow) {
                GlobalClass.flow = false;
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                this
                        .getSupportFragmentManager()
                        .beginTransaction().replace(R.id.home_fragment_container,
                        new HomeGridFragment()).commit();
            } else if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onResume() {
        try {

            if (GlobalClass.hasActiveBooking) {
                iv_sos.setVisibility(View.VISIBLE);
            } else {
                iv_sos.setVisibility(View.GONE);
            }
            img_et_btn.setVisibility(View.VISIBLE);
            nav_menu.setVisibility(View.VISIBLE);
            TextView user_name = headerLayout.findViewById(R.id.user_name);
            TextView email = headerLayout.findViewById(R.id.user_email);
            ImageView img_profile_photo = headerLayout.findViewById(R.id.img_profile_photo);
            user_name.setText(GlobalClass.sharedPreferences.getString("fName", "") + " " + GlobalClass.sharedPreferences.getString("lName", ""));
            Glide.with(this).load(GlobalClass.sharedPreferences.getString("img", "")).error(R.drawable.profile_image).into(img_profile_photo);
            email.setText(GlobalClass.sharedPreferences.getString("eMail", ""));
            GlobalClass.mPreviousRouteName="";
            for (int i = 0; i < GlobalClass.headerList.size(); i++) {
                GlobalClass.headerList.get(i).setSelected(false);
                if (!GlobalClass.headerList.get(i).getRoutesSubcategory().isEmpty()) {
                    for (int j = 0; j < GlobalClass.headerList.get(i).getRoutesSubcategory().size(); j++) {
                        GlobalClass.headerList.get(i).getRoutesSubcategory().get(j).setSelected(false);
                    }
                }
            }
            super.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    //on create initialize all sdk keys
    private void onCreated() {
        try {
            MobileKeysApiFacade mobileKeysApiFacade = this;
            mobileKeysApiFactory = (MobileKeysApiFactory) getApplication();
            mobileKeys = mobileKeysApiFactory.getMobileKeys();
            readerConnectionController = mobileKeysApiFactory.getReaderConnectionController();
            scanConfiguration = mobileKeysApiFactory.getScanConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //read connection call backs from sdk
    private void readCallBack() {
        try {
            readerConnectionCallback = new ReaderConnectionCallback(getApplicationContext());
            readerConnectionCallback.registerReceiver(this);
            hceConnectionCallback = new HceConnectionCallback(getApplicationContext());
            hceConnectionCallback.registerReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //unregister callbacks and receivers
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);
        readerConnectionCallback.unregisterReceiver();
        hceConnectionCallback.unregisterReceiver();
    }


    @Override
    public MobileKeys getMobileKeys() {
        return mobileKeysApiFactory.getMobileKeys();
    }

    @Override
    public void onStartUpComplete() {
        Log.d(TAG, "Application onStartUpComplete()");
        showEndpointSetupFragmentIfNotSetup();
    }

    @Override
    public void onEndpointSetUpComplete() {
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

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            this
                    .getSupportFragmentManager()
                    .beginTransaction().replace(R.id.home_fragment_container,
                    new HomeGridFragment()).commit();
            GlobalClass.mPreviousRouteName="";
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
            isEndpointSetup = mobileKeys.isEndpointSetupComplete();

        } catch (MobileKeysException e) {
            e.printStackTrace();
        }
        return isEndpointSetup;
    }

    @Override
    public ReaderConnectionController getReaderConnectionController() {
        return readerConnectionController;
    }

    @Override
    public ScanConfiguration getScanConfiguration() {
        return scanConfiguration;
    }


    private void showEndpointSetupFragmentIfNotSetup() {
        try {
            if (mobileKeys.isEndpointSetupComplete()) {
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



    //onAcitvity result pass data from gallery to fragment(edit profile)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f instanceof EditProfile) {
                    f.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
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


}

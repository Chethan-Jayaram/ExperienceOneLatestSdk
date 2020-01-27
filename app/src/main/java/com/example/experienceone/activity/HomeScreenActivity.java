package com.example.experienceone.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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
import com.example.experienceone.adapter.generaladapters.ExpandableNavListAdapter;
import com.example.experienceone.fragment.general.EditProfile;
import com.example.experienceone.fragment.general.SOS;
import com.example.experienceone.fragment.general.HomeGridFragment;
import com.example.experienceone.helper.APIResponse;

import com.example.experienceone.helper.GlobalClass;

import com.example.experienceone.pojo.navmenuitems.NavMenuItems;
import com.example.experienceone.pojo.navmenuitems.Result;
import com.example.experienceone.pojo.navmenuitems.RoutesSubcategory;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.example.experienceone.unlock.MobileKeysApiFacade;
import com.example.experienceone.unlock.MobileKeysApiFactory;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MobileKeysApiFacade,
        ReaderConnectionListener,
        MobileKeysCallback,
        HceConnectionListener {

    private static final String TAG = HomeScreenActivity.class.getName();
    private MobileKeys mobileKeys;
    private ExpandableListView expandableListView;

    private DrawerLayout drawer;
    private View headerLayout;
    private ReaderConnectionCallback readerConnectionCallback;
    private HceConnectionCallback hceConnectionCallback;
    private ReaderConnectionController readerConnectionController;
    private MobileKeysApiFactory mobileKeysApiFactory;
    private ScanConfiguration scanConfiguration;
    private MobileKeysApiFacade mobileKeysApiFacade;
    private Vibrator vibrator;
    private ImageView nav_menu, btn_back, iv_sos, img_et_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            nav_menu = findViewById(R.id.nav_menu);
            btn_back = findViewById(R.id.btn_back);
            iv_sos = findViewById(R.id.iv_sos);
            NavigationView navigationView = findViewById(R.id.nav_view);
            expandableListView = findViewById(R.id.expandableListView);
            drawer = findViewById(R.id.drawer_layout);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            onCreated();
            readCallBack();

            navigationView.setNavigationItemSelectedListener(this);

           // getNavMenuItems();

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

   /* private void getNavMenuItems() {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<NavMenuItems> navApiCall = api.getNavItems(headerMap);
        APIResponse.callBackgroundRetrofit(navApiCall, "navitems", this, this);
    }*/

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


   /* @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            if (apiCallName.equalsIgnoreCase("navitems")) {
                NavMenuItems navMenuItems = (NavMenuItems) response.body();
                if (navMenuItems.getStatus().equalsIgnoreCase("Success")) {
                    List<Result> item = navMenuItems.getResult();
                    HashMap<Result, List<RoutesSubcategory>> childList = new HashMap<>();
                    for (int i = 0; i < item.size(); i++) {
                        headerList.add(item.get(i));
                        if (!item.get(i).getRoutesSubcategory().isEmpty()) {
                            for (int j = 0; j < item.get(i).getRoutesSubcategory().size(); j++) {
                                List<RoutesSubcategory> routesSubcategory = item.get(i).getRoutesSubcategory();
                                childList.put(headerList.get(i), routesSubcategory);
                            }
                        }
                    }
                    populateExpandableList(this, headerList, childList);
                } else {
                    GlobalClass.showErrorMsg(this, navMenuItems.getError());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

 /*   @Override
    public void onErrorListner() {

    }*/

   /* private void populateExpandableList(HomeScreenActivity homeScreenActivity, List<Result> headerList, HashMap<Result, List<RoutesSubcategory>> childList) {

        ExpandableListAdapter expandableListAdapter = new ExpandableNavListAdapter(homeScreenActivity, headerList, childList);

        expandableListView.setAdapter(expandableListAdapter);


        expandableListView.setOnGroupClickListener((parent, v, groupPosition, ID) -> {
            if (!headerList.get(groupPosition).getSelected()) {
                headerList.get(groupPosition).setSelected(true);
                for(int i=0;i<headerList.size();i++){
                    if(groupPosition!=i) {
                        headerList.get(i).setSelected(false);
                    }
                }
                ChangeFragment(headerList.get(groupPosition).getMobileRoute().getRouteName());
                if(groupPosition!=2){
                    handelNavDrawer();
                }
            }else if(groupPosition!=2) {
                handelNavDrawer();
            }

            return false;
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            if (GlobalClass.hasActiveBooking) {
                if (!childList.get(headerList.get(groupPosition)).get(childPosition).getSelected()) {
                    childList.get(headerList.get(groupPosition)).get(childPosition).setSelected(true);
                    for(int i=0;i<childList.get(headerList.get(groupPosition)).size();i++){
                        if(childPosition!=i) {
                            childList.get(headerList.get(groupPosition)).get(i).setSelected(false);
                        }
                    }
                    if(  GlobalClass.ChangeChildFragment(childList.get(headerList.get(groupPosition)).get(childPosition).getMobileRoute().getRouteName(),this)){
                        handelNavDrawer();
                        HomeGridFragment homeGridFragment=new HomeGridFragment();
                        homeGridFragment.getInvitationCode();
                    }


                }else{
                    handelNavDrawer();
                }



            }
            return false;
        });
    }

    private void ChangeFragment(String className) {
        try {
            try {
                if (GlobalClass.hasActiveBooking) {
                    GlobalClass.edit.putBoolean("hasInvitationCode", false);
                    GlobalClass.edit.apply();
                 *//*   if (!mobileKeysApiFacade.getMobileKeys().listMobileKeys().isEmpty()) {
                        mobileKeysApiFacade.getMobileKeys().listMobileKeys().clear();
                        mobileKeysApiFacade.getMobileKeys().unregisterEndpoint(this);
                    }*//*
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            className = GlobalClass.getClassName(className);
            if (className.contains("Logout")) {
                Intent intent = new Intent(this, UseAuthenticationActivity.class);
                intent.putExtra("logout", true);
                startActivity(intent);
                finish();
            } else if (!className.isEmpty()) {
                String fullPathOfTheClass = "com.example.experienceone.fragment." + className;
                Class<?> cls = Class.forName(fullPathOfTheClass);
                Fragment fragment = (Fragment) cls.newInstance();
                this.getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    //on create initialize all sdk keys
    private void onCreated() {
        try {
            mobileKeysApiFacade = this;
            mobileKeysApiFactory = (MobileKeysApiFactory) getApplication();
            mobileKeys = mobileKeysApiFactory.getMobileKeys();
            readerConnectionController = mobileKeysApiFactory.getReaderConnectionController();
            scanConfiguration = mobileKeysApiFactory.getScanConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //read connetion call backs from sdk
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

    //unregister calbacks and receivers
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void   handelNavDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}

package com.example.experienceone.fragment.general;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.assaabloy.mobilekeys.api.EndpointSetupConfiguration;
import com.assaabloy.mobilekeys.api.MobileKeysCallback;
import com.assaabloy.mobilekeys.api.MobileKeysException;
import com.example.experienceone.R;
import com.example.experienceone.activity.HomeScreenActivity;
import com.example.experienceone.activity.UseAuthenticationActivity;
import com.example.experienceone.adapter.generaladapters.ExpandableNavListAdapter;
import com.example.experienceone.adapter.generaladapters.HomeGridViewAdapter;
import com.example.experienceone.fragment.modules.DoorUnlockingFragment;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.CustomMessageHelper;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.dashboardelements.DashboardItem;
import com.example.experienceone.pojo.dashboardelements.DashbordElements;
import com.example.experienceone.pojo.doorunlock.InvitationCode;
import com.example.experienceone.pojo.doorunlock.Mobilekeys;
import com.example.experienceone.pojo.navmenuitems.NavMenuItems;
import com.example.experienceone.pojo.navmenuitems.Result;
import com.example.experienceone.pojo.navmenuitems.RoutesSubcategory;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;
import com.example.experienceone.unlock.MobileKeysApiFacade;
import com.example.experienceone.unlock.SnackbarFactory;

import org.xml.sax.ErrorHandler;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeGridFragment extends Fragment implements ApiListener, MobileKeysCallback, View.OnClickListener {

    private GridView gridView;
    private List<DashboardItem> item;
    private Context context=getContext();
    private ProgressBar loading;
    private ProgressDialog dialog;
    private MobileKeysApiFacade mobileKeysApiFacade;
    private SnackbarFactory snackbarFactory;
    private ExpandableListView expandableListView;
    private DrawerLayout drawer;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof MobileKeysApiFacade)) {
            throw new IllegalArgumentException("Error: attaching to context that doesn't implement MobileKeysApiFacade");
        }
        mobileKeysApiFacade = (MobileKeysApiFacade) context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        try {
            context = view.getContext();
            gridView = view.findViewById(R.id.gridview);
            getActivity().findViewById(R.id.btn_back).setVisibility(View.GONE);
            getActivity().findViewById(R.id.nav_menu).setVisibility(View.VISIBLE);
            expandableListView = getActivity().findViewById(R.id.expandableListView);
            drawer = getActivity().findViewById(R.id.drawer_layout);
            expandableListView.setDividerHeight(0);
            loading=view.findViewById(R.id.loading);
            TextView toolBarText = getActivity().findViewById(R.id.toolbar_title);



            getNavMenuItems();
            getDashBoardElements();



            snackbarFactory = new SnackbarFactory(container);
            toolBarText.setText(GlobalClass.loacation);


        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }


    //hide keyboard on Activity created
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void getNavMenuItems() {
        GlobalClass.headerList.clear();
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<NavMenuItems> navApiCall = api.getNavItems(headerMap);
        APIResponse.callBackgroundRetrofit(navApiCall, "navitems", context, this);
    }

    private void getDashBoardElements() {
        loading.setVisibility(View.VISIBLE);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Call<DashbordElements> dashboardelement = api.getDashBoardElemnts(headerMap);
        APIResponse.getCallRetrofit(dashboardelement, "dashBoardElements", context, this);
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {
        try {
            loading.setVisibility(View.GONE);
            if (apiCallName.equalsIgnoreCase("dashBoardElements")) {
                DashbordElements dashbordElements = (DashbordElements) response.body();
                if (dashbordElements.getStatus().equalsIgnoreCase("Success")) {
                    item = dashbordElements.getResult().getDashboardItems();
                    GlobalClass.hasActiveBooking=dashbordElements.getResult().getIsActive();
                    HashMap<String,String> routeMap=new HashMap<>();
                    for(DashboardItem result:item){
                        routeMap.put(result.getMobileRoute().getRouteName(),"");
                    }
                    HomeGridViewAdapter adapter = new HomeGridViewAdapter(context, item, position -> {
                        if (GlobalClass.hasActiveBooking) {
                            item.get(position).getId();
                            if (GlobalClass.ChangeChildFragment(item.get(position).getMobileRoute().getRouteName(), (FragmentActivity) context)) {
                                getInvitationCode();
                            }
                        }
                    });
                    gridView.setAdapter(adapter);
                }
            }else  if (apiCallName.equalsIgnoreCase("navitems")) {
                NavMenuItems navMenuItems = (NavMenuItems) response.body();
                if (navMenuItems.getStatus().equalsIgnoreCase("Success")) {
                    List<Result> item = navMenuItems.getResult();
                    HashMap<Result, List<RoutesSubcategory>> childList = new HashMap<>();
                    for (int i = 0; i < item.size(); i++) {
                        GlobalClass.headerList.add(item.get(i));
                        if (!item.get(i).getRoutesSubcategory().isEmpty()) {
                            for (int j = 0; j < item.get(i).getRoutesSubcategory().size(); j++) {
                                List<RoutesSubcategory> routesSubcategory = item.get(i).getRoutesSubcategory();
                                childList.put(GlobalClass.headerList.get(i), routesSubcategory);
                            }
                        }
                    }
                    populateExpandableList(context, GlobalClass.headerList, childList);
                } else {
                    GlobalClass.showErrorMsg(context, navMenuItems.getError());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {
        try{
            loading.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onStart() {
        super.onStart();try {
            if (mobileKeysApiFacade.isEndpointSetUpComplete()) {
                mobileKeysApiFacade.onEndpointSetUpComplete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Mobile keys transaction success/completed callback
     */
    @Override
    public void handleMobileKeysTransactionCompleted() {
        if (isVisible()) {
            mobileKeysApiFacade.onEndpointSetUpComplete();
        }
    }


    //door unlock invitation code api call
    private void getInvitationCode() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("please wait..");
        dialog.setCancelable(false);
        dialog.show();
        Map map = new HashMap();
        map.put("token", GlobalClass.token);
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Call<InvitationCode> call = api.getInvitationCode(map);
        call.enqueue(new Callback<InvitationCode>() {
            @Override
            public void onResponse(Call<InvitationCode> call, Response<InvitationCode> response) {
                try {
                    if (response.isSuccessful()) {
                        InvitationCode invitationCode=response.body();
                        if (response.body().getStatus().equalsIgnoreCase("Success")) {
                            dismissDialog();
                            GlobalClass.token=response.body().getResult().getToken();
                            submitInvitationCode(response.body().getResult().getInvitationCode());
                        } else {
                            GlobalClass.mPreviousRouteName="";
                            dismissDialog();
                            CustomMessageHelper showDialog = new CustomMessageHelper((Activity) context);
                            showDialog.showCustomMessage((Activity) context, "Alert!!", invitationCode.getError(), false, false);
                        }
                    } else {
                        errorHandler( getString(R.string.ERROR));
                    }
                } catch (Exception e) {
                    GlobalClass.mPreviousRouteName="";
                    dismissDialog();
                    e.printStackTrace();
                    e.getMessage();
                }

            }
            @Override
            public void onFailure(Call<InvitationCode> call, Throwable t) {
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
    }

    private void errorHandler(String error) {
        GlobalClass.mPreviousRouteName="";
        dismissDialog();
        CustomMessageHelper showDialog = new CustomMessageHelper((Activity) context);
        showDialog.showCustomMessage((Activity) context, "Alert!!",error, false, false);
    }

    private void dismissDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    //submit invitation code to SDK
    private void submitInvitationCode(String invitation) {
        try {
            mobileKeysApiFacade.getMobileKeys().endpointSetup(this, invitation, new EndpointSetupConfiguration.Builder().build());
            dialog.setMessage("please wait, while we are registering your phone for mobile key");
            dialog.setCancelable(false);
            dialog.show();
            checkInvitionComplet();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Check if invitation code registration successful
    private void checkInvitionComplet() {
        try {
            new Handler().postDelayed(() -> {
                if (mobileKeysApiFacade.isEndpointSetUpComplete()) {
                    mobilekeyapi();
                } else {
                    checkInvitionComplet();
                }
            }, 5000);
        }catch (Exception e){
            GlobalClass.mPreviousRouteName="";
            e.printStackTrace();
        }
    }


    //api call to get mobilekeys
    private void mobilekeyapi() {
        try {
            dialog.dismiss();
            dialog.setMessage("please wait, while we are creating your mobile key");
            dialog.setCancelable(false);
            dialog.show();
            Map map = new HashMap();
            map.put("token", GlobalClass.token);
            APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
            Call<Mobilekeys> call = api.mobilekeyapi(map);
            call.enqueue(new Callback<Mobilekeys>() {
                @Override
                public void onResponse(Call<Mobilekeys> call, Response<Mobilekeys> response) {
                    try {
                        if (response.isSuccessful()){
                            Mobilekeys mobilekeys=response.body();
                            if (response.body().getResult().getAutoKey().getStatus().equalsIgnoreCase("Success")) {
                                dismissDialog();
                                GlobalClass.sharedPreferences = context.getSharedPreferences(GlobalClass.shredPrefName, 0);
                                GlobalClass.edit = GlobalClass.sharedPreferences.edit();
                                GlobalClass.edit.putBoolean("hasInvitationCode", true);
                                GlobalClass.edit.apply();
                                if (mobileKeysApiFacade.isEndpointSetUpComplete()) {
                                    mobileKeysApiFacade.onEndpointSetUpComplete();
                                }
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new DoorUnlockingFragment()).addToBackStack(null).commit();
                            } else {
                                GlobalClass.mPreviousRouteName="";
                                dismissDialog();
                                CustomMessageHelper showDialog = new CustomMessageHelper((Activity) context);
                                showDialog.showCustomMessage((Activity) context, "Alert!!", mobilekeys.getResult().getAutoKey().getResult().getDeveloperMessage(), false, false);
                            }
                        } else {
                            errorHandler( getString(R.string.ERROR));
                        }
                    } catch (Exception e) {
                        GlobalClass.mPreviousRouteName="";
                        dismissDialog();
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<Mobilekeys> call, Throwable t) {
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
            dismissDialog();
            e.getMessage();
            e.printStackTrace();
        }
    }

    /**
     * Mobile keys transaction failed callback
     *
     * @param mobileKeysException failed description
     */
    @Override
    public void handleMobileKeysTransactionFailed(MobileKeysException mobileKeysException) {
        if (isVisible()) {
            //  handler.postDelayed(unlockUiRunnable, UNLOCK_UI_DELAY);
            snackbarFactory.createAndShow(mobileKeysException,
                    HomeScreenActivity.shouldRetry(mobileKeysException) ? this : null);
        }
    }


    @Override
    public void onClick(View v) {

    }


    //nav Drawer using  expandable list view

    private void populateExpandableList(Context context, List<Result> headerList, HashMap<Result, List<RoutesSubcategory>> childList) {

        ExpandableListAdapter expandableListAdapter = new ExpandableNavListAdapter(context, headerList, childList);

        expandableListView.setAdapter(expandableListAdapter);


        expandableListView.setOnGroupClickListener((parent, v, groupPosition, ID) -> {
            if(headerList.get(groupPosition).getRoutesSubcategory().size()<=0) {
                if(!headerList.get(groupPosition).getSelected()){
                    headerList.get(groupPosition).setSelected(true);
                    for (int i = 0; i < headerList.size(); i++) {
                        if (groupPosition != i) {
                            headerList.get(i).setSelected(false);
                        }
                    }
                    ChangeFragment(headerList.get(groupPosition).getMobileRoute().getRouteName());
                    if (headerList.get(groupPosition).getRoutesSubcategory().size() <= 0) {
                        handelNavDrawer();
                    }
                } else if (headerList.get(groupPosition).getRoutesSubcategory().size() <= 0) {
                    handelNavDrawer();
                }
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
                    if (GlobalClass.ChangeChildFragment(childList.get(headerList.get(groupPosition)).get(childPosition).getMobileRoute().getRouteName(), (FragmentActivity) context)){
                        getInvitationCode();
                    }else{
                        handelNavDrawer();
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

            className = GlobalClass.getClassName(className);
            if (className.equalsIgnoreCase("general.Logout")) {
                Intent intent = new Intent(context, UseAuthenticationActivity.class);
                intent.putExtra("logout", true);
                startActivity(intent);
                getActivity().finish();
            } else if (className.contains("HomeGridFragment")) {
                String fullPathOfTheClass = "com.example.experienceone.fragment." + className;
                Class<?> cls = Class.forName(fullPathOfTheClass);
                Fragment fragment = (Fragment) cls.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).commit();
            }else if(!className.isEmpty()){
                String fullPathOfTheClass = "com.example.experienceone.fragment." + className;
                Class<?> cls = Class.forName(fullPathOfTheClass);
                Fragment fragment = (Fragment) cls.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //hide nav drawer
    private void handelNavDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}

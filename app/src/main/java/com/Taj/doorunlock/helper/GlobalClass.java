package com.taj.doorunlock.helper;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*import androidx.security.crypto.EncryptedSharedPreferences;

import androidx.security.crypto.MasterKeys;*/

import com.google.gson.Gson;
import com.legic.mobile.sdk.api.LegicMobileSdkManager;
import com.taj.doorunlock.R;
import com.taj.doorunlock.activity.BookingDetailsListActivity;
import com.taj.doorunlock.retrofit.ClientServiceGenerator;
import com.taj.doorunlock.services.APIMethods;
import com.taj.doorunlock.unlock.Taj;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@SuppressLint("SimpleDateFormat")
public class GlobalClass {

    public static AlertDialog mLocationPermission;
    public static final String android_id = Settings.Secure.getString(Taj.getAppContext().getContentResolver(),
            Settings.Secure.ANDROID_ID);
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static final String mSufix = "20434c";
    public static final String mPrefix = "494820";

    public static String mUser_token = "";
    public static String mLegicToken = "";

    public static String mFirstName = "";

    public static final APIMethods mApi = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
    public static AlertDialog alert;

    public static final Integer mDelay = 50;

    private static final String shredPrefName = "TAJ";
    public static SharedPreferences sharedPreferences = Taj.getAppContext().getSharedPreferences(shredPrefName, Context.MODE_PRIVATE);
    ;

    //doormakba
    //check in check out dates
    public static Date mUserCheckInDate;
    public static Date mUserCheckOutDate;
    public static LegicMobileSdkManager mManager;


    /*   private static String masterKeyAlias;

       static {
           try {
               masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

               sharedPreferences = EncryptedSharedPreferences.create(
                       shredPrefName,
                       masterKeyAlias,
                       Taj.getAppContext(),
                       EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                       EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
               );
           } catch (GeneralSecurityException | IOException e) {
               e.printStackTrace();
           }
       }
   */
    public static SharedPreferences.Editor edit = sharedPreferences.edit();


    public static DateFormat inputdateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat outputdateformat = new SimpleDateFormat("MMM d, yyyy h:mm a");
    public static Gson gson = new Gson();

    public static DateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss");

    public static DateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a");

    public static DateFormat outputhourFormat = new SimpleDateFormat("HH");

    public static DateFormat outputMinFormat = new SimpleDateFormat("mm");

    public static DecimalFormat decimalFormat = new DecimalFormat("0.00");


    public static DateFormat weatherinputdateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static DateFormat weatheroutputdateformat = new SimpleDateFormat("EEEE h:mm a");

    public static DateFormat tomorrowDayformat = new SimpleDateFormat("EEEE");
    public static DateFormat weainputdateformat = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat weaoutputdateformat = new SimpleDateFormat("EEEE");


    public static DateFormat mRoutineDateFormat = new SimpleDateFormat("MMM d, yyyy");


    public interface AdapterClickListner { // create an interface
        void onItemClickListener(Integer position); // create callback function
    }


    // Function to remove duplicates from an ArrayList
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }


    public interface OnBiometricAuthSucess { // create an interface
        void onSucessfullBiometricAuth(); // create callback function
    }


    public static void ShowAlet(Context context, String title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        dialog.dismiss();
                    });
            alert = builder.create();
            alert.show();
            Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setTextColor(context.getResources().getColor(R.color.black));
            negativeButton.setTextColor(context.getResources().getColor(R.color.black));
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }


    // Used to convert 24hr format to 12hr format with AM/PM values
    public static String updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }


    public static String encodeTobase64(Bitmap image) {
        String imageEncoded = "";
        try {
            Bitmap immagex = image;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageEncoded;
    }

    public static boolean hasLocationPermissions(Context context) {
        return (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

    }


    public static void showPermissionDialoug( Activity activity) {
        try {
            Button btn_enable, btn_deny;
            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            ViewGroup viewGroup = activity.findViewById(android.R.id.content);


            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.location_dailoug, viewGroup, false);

            //Now we need an AlertDialog.Builder object
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.Dialog);

            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView);

            //finally creating the alert dialog and displaying it
            mLocationPermission = builder.create();
            btn_enable = dialogView.findViewById(R.id.btn_enable);
            // btn_deny= dialogView.findViewById(R.id.btn_deny);
          /*  btn_deny.setOnClickListener(v ->{

                try {
                    mLocationPermission.dismiss();

                    Intent intent = new Intent(view.getContext(), BookingDetailsListActivity.class);
                    view.getContext().startActivity(intent);
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } );*/

            btn_enable.setOnClickListener(v -> {
                try {
                    ActivityCompat.requestPermissions(activity,
                            getPermissions(),
                            MY_PERMISSIONS_REQUEST_LOCATION);
                    mLocationPermission.dismiss();
                } catch (Exception e) {
                    Log.d("permission exception", "exception");
                }

            });
            mLocationPermission.show();

            mLocationPermission.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    public static void buildAlertMessageNoGps(Context mContext, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Please turn on location to unlock Door")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    mContext.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                })
                .setNegativeButton("No", (dialog, id) -> {
                    Intent intent = new Intent(mContext, BookingDetailsListActivity.class);
                    mContext.startActivity(intent);
                    activity.finish();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public static String[] getPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        } else {

            return new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        }
    }


}
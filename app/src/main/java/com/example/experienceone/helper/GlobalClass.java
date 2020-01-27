package com.example.experienceone.helper;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.example.experienceone.R;
import com.example.experienceone.pojo.color.ColorPojo;
import com.example.experienceone.pojo.dinning.CategoryItem;
import com.example.experienceone.pojo.navmenuitems.Result;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressLint("SimpleDateFormat")
public class GlobalClass {
//doorunlock
    public static String mPreviousRouteName="";
    public static List<Result> headerList = new ArrayList<>();

    public static boolean flow=false;
 //complte setup
    public static String loacation="";
    public static Boolean isMpinSetupComplete = false;
    public static Boolean hasActiveBooking = false;
    public static final String shredPrefName = "experienceOne";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor edit;
    public static String token = "";
    public static Integer Booking_id;
    public static Integer Guest_id;

    public static DateFormat inputdateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static DateFormat outputdateformat = new SimpleDateFormat("MMM d, yyyy h:mm a");
    public static Gson gson = new Gson();

    public static DateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss");

    public static DateFormat outputTimeFormat = new SimpleDateFormat("hh:mm a");


    public static DateFormat outputhourFormat = new SimpleDateFormat("HH");

    public static DateFormat outputMinFormat = new SimpleDateFormat("mm");

    public static DecimalFormat decimalFormat = new DecimalFormat("0.00");


    public interface AdapterClickListner { // create an interface
        void onItemClickListener(Integer position); // create callback function
    }

    public static void showErrorMsg(Context context, String error) {
        CustomMessageHelper showDialog = new CustomMessageHelper(context);
        showDialog.showCustomMessage((Activity) context, "Alert!!", error, false, false);
    }

    public static String getColor(String str){
        ColorPojo map = gson.fromJson(str, ColorPojo.class);
        return  map.getActionStyles().getBackground();
    }

    public static String getClassName(String className) {
        String name = null;
        if (className.contains("internet")) {
            name = "InternetWifi";
        } else if (className.contains("directory")) {
            name = "HotelDirectory";
        } else if (className.contains("house")) {
            name = "HouseKeeping";
        } else if (className.contains("laundry")) {
            name = "Laundry";
        } else if (className.contains("dining")) {
            name = "dining.InRoomDining";
        } else if (className.contains("foreign")) {
            name = "ForeignExchange";
        } else if (className.contains("emergency")) {
            name = "EmergencyServices";
        } else if (className.contains("preference")) {
            name = "preferences.Preferences";
        } else if (className.contains("tavel")) {
            name = "travel.Travel";
        } else if(className.contains("tour")){
            name = "LocalTourGuide";
        } else if(className.contains("main-screen")){
            name = "general.HomeGridFragment";
        }else if(className.contains("information")){
            name = "general.HotelInformation";
        }else if(className.contains("services")){
            name = "";
        }else if(className.contains("my-stay")){
            name = "mystay.MyStayFragment";
        }else if(className.contains("tickets")){
            name = "general.TicketsList";
        }else if(className.contains("notification")){
            name = "general.Notification";
        } else if(className.contains("about")){
            name = "general.About";
        }else if(className.contains("report-a-bug")){
            name = "general.ReportABug";
        }else if(className.contains("logout")){
            name = "general.Logout";
        }else if(className.contains("door-unlock")){
            name = "DoorUnlockingFragment";
        }
        return name;
    }

    public static int numberStepperAdd(int count) {
        if (count < 100) {
            count = count + 1;
        }
        return count;
    }

    public static int numberStepperSub(int count) {
        if (count > 0) {
            count = count - 1;
        }
        return count;
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


    public static void ShowAlet(Context context, String title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        dialog.dismiss();
                    });
            AlertDialog alert = builder.create();
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
        String imageEncoded="";
        try {
            Bitmap immagex =image;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageEncoded;
    }





    public static void putsharedpreference(Context context, String firstName, String lastName, String contactNumber, String email, String martialStatus, String gender,String img) {
        GlobalClass.sharedPreferences = context.getSharedPreferences(GlobalClass.shredPrefName, 0);
        GlobalClass.edit = GlobalClass.sharedPreferences.edit();
        GlobalClass.edit.putString("fName", firstName);
        GlobalClass.edit.putString("lName", lastName);
        GlobalClass.edit.putString("cNumber", contactNumber);
        GlobalClass.edit.putString("eMail", email);
        GlobalClass.edit.putString("mStatus", martialStatus);
        GlobalClass.edit.putString("gender", gender);
        GlobalClass.edit.putString("img", img);
        GlobalClass.edit.apply();
    }

    public static List<CategoryItem> removeDuplicateItems(ArrayList<CategoryItem> menuItems) {
        List<CategoryItem>  menu_item=GlobalClass.removeDuplicates(menuItems);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            menu_item=  menu_item.stream().filter(p->p.getQuantity()!=0).collect(Collectors.toList());
        }else{
            for(int i=0;i<menu_item.size();i++){
                if(menu_item.get(i).getQuantity()==0){
                    menu_item.remove(menu_item.get(i));
                }
            }
        }
        return menu_item;
    }

    public static Boolean ChangeChildFragment(String className, FragmentActivity context) {
        try {
            if(!mPreviousRouteName.equalsIgnoreCase(className)) {
                mPreviousRouteName=className;
                className = GlobalClass.getClassName(className);
                String fullPathOfTheClass = "com.example.experienceone.fragment.modules." + className;
                Class<?> cls = Class.forName(fullPathOfTheClass);
                Fragment fragment = (Fragment) cls.newInstance();

                if (!GlobalClass.sharedPreferences.getBoolean("hasInvitationCode",false)&&className.contains("DoorUnlockingFragment")) {
                    return  true; //getInvitationCode();
                } else {
                    context.getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
                    return false;
                }
/*
                context.getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment).addToBackStack(null).commit();
*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }




}

package com.example.experienceone.fragment.modules.preferences;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.experienceone.R;
import com.example.experienceone.helper.APIResponse;
import com.example.experienceone.helper.GlobalClass;
import com.example.experienceone.pojo.GeneralPojo;
import com.example.experienceone.pojo.preference.CreateUpdatePrefrencesPojo;
import com.example.experienceone.pojo.preference.Result;
import com.example.experienceone.retrofit.ClientServiceGenerator;
import com.example.experienceone.services.APIMethods;
import com.example.experienceone.services.ApiListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Response;

public class CreateUpdatePreferences extends Fragment implements ApiListener {

    private TextView mon, tue, wed, thu, fri, sat, sun, pref_name;
    private TimePicker timePicker;
    private Context context;
    private LinkedHashMap<String, Integer> weekdays;
    private String operation;
    private Boolean bool;
    private Integer id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_update_prefrence, container, false);
        try {
            context = view.getContext();
            pref_name = view.findViewById(R.id.pref_name);
            timePicker = view.findViewById(R.id.timePicker);
            mon = view.findViewById(R.id.mon);
            tue = view.findViewById(R.id.tue);
            wed = view.findViewById(R.id.wed);
            thu = view.findViewById(R.id.thu);
            fri = view.findViewById(R.id.fri);
            sat = view.findViewById(R.id.sat);
            sun = view.findViewById(R.id.sun);

            weekdays = new LinkedHashMap<>();
            weekdays.put("Monday", 0);
            weekdays.put("Tuesday", 0);
            weekdays.put("Wednesday", 0);
            weekdays.put("Thursday", 0);
            weekdays.put("Friday", 0);
            weekdays.put("Saturday", 0);
            weekdays.put("Sunday", 0);
            Bundle data = getArguments();
            operation = data.getString("Operation");
            TextView toolbar_title = getActivity().findViewById(R.id.toolbar_title);
            toolbar_title.setText("Add Prefrences");
            if (operation.equalsIgnoreCase("Edit")) {
                toolbar_title.setText("Edit Prefrences");
                Result mResult = data.getParcelable("details");
                bool = data.getBoolean("bool");
                id = data.getInt("id");
                pref_name.setText(mResult.getName());

                String[] arr = mResult.getRepeatDays().replaceAll(" ", "").split(",");
                for (String s : arr) {
                    setSelectedBackground(s);
                }

                    int hour=Integer.parseInt(GlobalClass.outputhourFormat.format(GlobalClass.inputTimeFormat.parse(mResult.getTime())));
                   int min=Integer.parseInt(GlobalClass.outputMinFormat.format(GlobalClass.inputTimeFormat.parse(mResult.getTime())));
                    setTime(hour,min);

            }


            Button save_btn = view.findViewById(R.id.save_btn);
            save_btn.setOnClickListener(v -> {
                StringBuilder weekdayBuilder = prepareWeekdays();
                if (operation.equalsIgnoreCase("Create")) {
                    if (!pref_name.getText().toString().isEmpty()) {
                        if (pref_name.getText().toString().length() < 100) {
                            if (!weekdayBuilder.toString().isEmpty()) {
                                String repeatdays=weekdayBuilder.toString();
                                repeatdays=repeatdays.substring(0, repeatdays.length() - 1);
                                postPreffrences(repeatdays);
                            } else {
                                GlobalClass.ShowAlet(context, "Alert", "Please select ");
                            }
                        } else {
                            GlobalClass.ShowAlet(context, "Alert", "Prefrences tittle cannot contain more than 100 character");
                        }
                    } else {
                        GlobalClass.ShowAlet(context, "Alert", "Please enter the preference title");
                    }
                } else if (operation.equalsIgnoreCase("Edit")) {
                    if (!pref_name.getText().toString().isEmpty()) {
                        if (pref_name.getText().toString().length() < 100) {
                            if (!weekdayBuilder.toString().isEmpty()) {
                                String repeatdays=weekdayBuilder.toString();
                                repeatdays=repeatdays.substring(0, repeatdays.length() - 1);
                                updateDetails(bool, repeatdays, id);
                            } else {
                                GlobalClass.ShowAlet(context, "Alert", "Please select ");
                            }
                        } else {
                            GlobalClass.ShowAlet(context, "Alert", "Prefrences tittle cannot contain more than 100 character");
                        }
                    } else {
                        GlobalClass.ShowAlet(context, "Alert", "Please enter the preference title");
                    }
                }
            });
            weekDayclickLisners();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    private StringBuilder prepareWeekdays() {
        StringBuilder weekdayBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : weekdays.entrySet()) {
            if (entry.getValue() == 1) {
                weekdayBuilder.append(" " + entry.getKey() + ",");
            }
        }
        return weekdayBuilder;
    }
    private void setTime(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
    }

    private void postPreffrences(String repeatdays) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Map<String, String> body = PostDetails(repeatdays, true);
        Call<CreateUpdatePrefrencesPojo> generalPojoCall = api.postCreatePrefrence(headerMap, body);
        APIResponse.postCallRetrofit(generalPojoCall, "create_Pref", context, this);
    }

    private void updateDetails(boolean status, String  repeatdays, int id) {
        APIMethods api = ClientServiceGenerator.getUrlClient().create(APIMethods.class);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "bearer " + GlobalClass.token);
        Map<String, String> body = PostDetails(repeatdays, status);
        Call<CreateUpdatePrefrencesPojo> putStatusPrefrence = api.putStatusPrefrence(String.valueOf(id), headerMap, body);
        APIResponse.postCallRetrofit(putStatusPrefrence, "updatePrefrence", context, this);
    }


    private Map<String, String> PostDetails(String weekdayBuilder, boolean b) {
        Map<String, String> body = new HashMap<>();
        body.put("name", pref_name.getText().toString());
        body.put("_time", timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
        body.put("repeat_days", weekdayBuilder);
        body.put("guest", String.valueOf(GlobalClass.Guest_id));
        body.put("is_active", String.valueOf(true));
        return body;
    }

    @Override
    public <ResponseType> void success(Response<ResponseType> response, String apiCallName) {

        try {
            if (apiCallName.equalsIgnoreCase("create_Pref")) {
                CreateUpdatePrefrencesPojo prefrencesPojo = (CreateUpdatePrefrencesPojo) response.body();
                if (prefrencesPojo.getStatus().equalsIgnoreCase("Success")) {
                    getActivity().getSupportFragmentManager().popBackStack("create", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    GlobalClass.showErrorMsg(context, prefrencesPojo.getError());
                }
            } else if (apiCallName.equalsIgnoreCase("updatePrefrence")) {
                GeneralPojo generalPojo = (GeneralPojo) response.body();
                if (generalPojo.getStatus().equalsIgnoreCase("Success")) {
                    getActivity().getSupportFragmentManager().popBackStack("edit", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorListner() {

    }

    private void weekDayclickLisners() {
        mon.setOnClickListener(new View.OnClickListener() {
            private boolean stateChanged;

            public void onClick(View view) {
                if (stateChanged) {
                    mon.setBackgroundColor(getResources().getColor(R.color.white));
                    mon.setTextColor(getResources().getColor(R.color.black));
                    weekdays.put("Monday", 0);
                } else {
                    setSelectedBackground("Monday");
                    weekdays.put("Monday", 1);
                }
                stateChanged = !stateChanged;
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            private boolean stateChanged;

            public void onClick(View view) {
                if (stateChanged) {
                    tue.setBackgroundColor(getResources().getColor(R.color.white));
                    tue.setTextColor(getResources().getColor(R.color.black));
                    weekdays.put("Tuesday", 0);
                } else {
                    setSelectedBackground("Tuesday");
                    weekdays.put("Tuesday", 1);
                }
                stateChanged = !stateChanged;
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            private boolean stateChanged;

            public void onClick(View view) {
                if (stateChanged) {
                    wed.setBackgroundColor(getResources().getColor(R.color.white));
                    wed.setTextColor(getResources().getColor(R.color.black));
                    weekdays.put("Wednesday", 0);
                } else {
                    setSelectedBackground("Wednesday");
                }
                stateChanged = !stateChanged;
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            private boolean stateChanged;

            public void onClick(View view) {
                if (stateChanged) {
                    thu.setBackgroundColor(getResources().getColor(R.color.white));
                    thu.setTextColor(getResources().getColor(R.color.black));
                    weekdays.put("Thursday", 0);
                } else {
                    setSelectedBackground("Thursday");
                }
                stateChanged = !stateChanged;
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            private boolean stateChanged;

            public void onClick(View view) {
                if (stateChanged) {
                    fri.setBackgroundColor(getResources().getColor(R.color.white));
                    fri.setTextColor(getResources().getColor(R.color.black));
                    weekdays.put("Friday", 0);
                } else {
                    setSelectedBackground("Friday");
                }
                stateChanged = !stateChanged;
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            private boolean stateChanged;

            public void onClick(View view) {
                if (stateChanged) {
                    sat.setBackgroundColor(getResources().getColor(R.color.white));
                    sat.setTextColor(getResources().getColor(R.color.black));
                    weekdays.put("Saturday", 0);
                } else {
                    setSelectedBackground("Saturday");
                }
                stateChanged = !stateChanged;
            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            private boolean stateChanged;

            public void onClick(View view) {
                if (stateChanged) {
                    sun.setBackgroundColor(getResources().getColor(R.color.white));
                    sun.setTextColor(getResources().getColor(R.color.black));
                    weekdays.put("Sunday", 0);
                } else {
                    setSelectedBackground("Sunday");
                }
                stateChanged = !stateChanged;
            }
        });
    }

    private void setSelectedBackground(String str) {
        switch (str) {
            case "Monday":
                mon.setBackground(getResources().getDrawable(R.drawable.bg_weekdays));
                mon.setTextColor(getResources().getColor(R.color.white));
                weekdays.put("Monday", 1);
                break;
            case "Tuesday":
                tue.setBackground(getResources().getDrawable(R.drawable.bg_weekdays));
                tue.setTextColor(getResources().getColor(R.color.white));
                weekdays.put("Tuesday", 1);
                break;
            case "Wednesday":
                wed.setBackground(getResources().getDrawable(R.drawable.bg_weekdays));
                wed.setTextColor(getResources().getColor(R.color.white));
                weekdays.put("Wednesday", 1);
                break;
            case "Thursday":
                thu.setBackground(getResources().getDrawable(R.drawable.bg_weekdays));
                thu.setTextColor(getResources().getColor(R.color.white));
                weekdays.put("Thursday", 1);
                break;
            case "Friday":
                fri.setBackground(getResources().getDrawable(R.drawable.bg_weekdays));
                fri.setTextColor(getResources().getColor(R.color.white));
                weekdays.put("Friday", 1);
                break;
            case "Saturday":
                sat.setBackground(getResources().getDrawable(R.drawable.bg_weekdays));
                sat.setTextColor(getResources().getColor(R.color.white));
                weekdays.put("Saturday", 1);
                break;
            case "Sunday":
                sun.setBackground(getResources().getDrawable(R.drawable.bg_weekdays));
                sun.setTextColor(getResources().getColor(R.color.white));
                weekdays.put("Sunday", 1);
                break;
        }

    }
}

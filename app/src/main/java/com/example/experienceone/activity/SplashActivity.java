package com.example.experienceone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.experienceone.R;
import com.example.experienceone.helper.GlobalClass;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2500; //splash screen will be shown for 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            new Handler().postDelayed(() -> {
                getSharedPreferences();
                Intent mainIntent = new Intent(SplashActivity.this, UseAuthenticationActivity.class);
                startActivity(mainIntent);
                finish();
            }, SPLASH_DISPLAY_LENGTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSharedPreferences() {
        GlobalClass.sharedPreferences = this.getSharedPreferences(GlobalClass.shredPrefName, 0);
        GlobalClass.isMpinSetupComplete = GlobalClass.sharedPreferences.getBoolean("isMpinSetupComplete",false );
    }

}

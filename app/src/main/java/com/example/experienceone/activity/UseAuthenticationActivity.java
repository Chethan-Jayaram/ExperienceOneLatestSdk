package com.example.experienceone.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.experienceone.R;
import com.example.experienceone.fragment.login.LoginMpinFragment;
import com.example.experienceone.fragment.login.UserAuthenticationFragment;
import com.example.experienceone.helper.GlobalClass;


public class UseAuthenticationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            if(getIntent().getBooleanExtra("logout",false)){
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginMpinFragment()).commit();
            }
            ImageView btn_back = findViewById(R.id.btn_back);
            findViewById(R.id.nav_menu).setVisibility(View.GONE);
            findViewById(R.id.iv_sos).setVisibility(View.GONE);
            if (GlobalClass.sharedPreferences.getBoolean("isMpinSetupComplete", false)) {
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginMpinFragment()).commit();
            }else{
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserAuthenticationFragment()).commit();
            }
            btn_back.setOnClickListener(v -> {
                onBackPressed();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  /*  @Override
    public void onBackPressed() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }if(GlobalClass.isMpinSetupComplete){
            finish();
        }else {
            super.onBackPressed();
        }
    }*/
}


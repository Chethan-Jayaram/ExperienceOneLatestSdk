package com.taj.doorunlock.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.taj.doorunlock.R;
import com.taj.doorunlock.activity.loginfragments.LoginMpinFragment;
import com.taj.doorunlock.activity.loginfragments.UserAuthenticationFragment;
import com.taj.doorunlock.helper.GlobalClass;


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
            btn_back.setOnClickListener(v -> onBackPressed());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


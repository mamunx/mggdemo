package com.mamun.mggdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Making the Splash Screen go Full Screen.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        final SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);

        int SPLASH_TIME_OUT = 2200;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(settings.getString("email", null) == null)
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

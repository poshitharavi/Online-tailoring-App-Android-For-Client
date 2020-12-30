package com.example.tailorar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences loginPreferences = getSharedPreferences("tailorUserDetails", MODE_PRIVATE);
                if (loginPreferences.contains("user_id")) { //How can I ask here?
                    Intent intent = new Intent(getApplicationContext(), StyleActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LogingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000);

    }
}

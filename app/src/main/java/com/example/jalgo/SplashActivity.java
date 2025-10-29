package com.example.jalgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
            long userId = sp.getLong("user_id", -1);

            // Si ya hay sesión guardada → va al MainActivity
            // Si no → va al LoginActivity
            Intent intent = (userId > 0)
                    ? new Intent(SplashActivity.this, MainActivity.class)
                    : new Intent(SplashActivity.this, LoginActivity.class);

            startActivity(intent);
            finish();
        }, 3000); // 3 segundos
    }
}


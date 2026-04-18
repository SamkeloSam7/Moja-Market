package com.example.mojamarket;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashLogo = findViewById(R.id.splashLogo);
        TextView splashSubtitle = findViewById(R.id.splashSubtitle);

        ObjectAnimator blink = ObjectAnimator.ofFloat(splashSubtitle, "alpha", 0.3f, 1f);
        blink.setDuration(800);
        blink.setRepeatMode(ValueAnimator.REVERSE);
        blink.setRepeatCount(ValueAnimator.INFINITE);
        blink.start();

        ObjectAnimator flip = ObjectAnimator.ofFloat(splashLogo, "rotationY", 0f, 180f);
        flip.setDuration(800);
        flip.setRepeatMode(ValueAnimator.REVERSE);
        flip.setRepeatCount(ValueAnimator.INFINITE);
        flip.start();

        splashSubtitle.postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("MojaMarketPrefs", MODE_PRIVATE);

            String rememberedUsername = prefs.getString("remembered_username", "");
            boolean hasRememberedUser = !rememberedUsername.isEmpty();

            Intent intent;
            if (hasRememberedUser) {
                intent = new Intent(SplashActivity.this, QuickLoginActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            }

            startActivity(intent);
            finish();
        }, 1800);
    }
}
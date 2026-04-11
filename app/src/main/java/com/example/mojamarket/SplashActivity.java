package com.example.mojamarket;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
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

        // Blink animation for subtitle
        ObjectAnimator blink = ObjectAnimator.ofFloat(splashSubtitle, "alpha", 0.3f, 1f);
        blink.setDuration(800);
        blink.setRepeatMode(ValueAnimator.REVERSE);
        blink.setRepeatCount(ValueAnimator.INFINITE);
        blink.start();

        // Flip animation for logo
        ObjectAnimator flip = ObjectAnimator.ofFloat(splashLogo, "rotationY", 0f, 180f, 360f);
        flip.setDuration(1600);
        flip.setRepeatMode(ValueAnimator.RESTART);
        flip.setRepeatCount(ValueAnimator.INFINITE);
        flip.start();

        splashSubtitle.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            finish();
        }, 1800);
    }
}
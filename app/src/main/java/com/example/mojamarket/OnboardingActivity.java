package com.example.mojamarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 onboardingViewPager;
    private MaterialButton nextButton;
    private TextView skipButton;
    private LinearLayout dotsLayout;
    private List<OnboardingItem> onboardingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);

        if (getSharedPreferences("MojaMarketPrefs", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        onboardingViewPager = findViewById(R.id.onboardingViewPager);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);
        dotsLayout = findViewById(R.id.dotsLayout);

        setupOnboardingItems();

        OnboardingAdapter adapter = new OnboardingAdapter(onboardingItems);
        onboardingViewPager.setAdapter(adapter);

        setupDots();
        updateDots(0);

        skipButton.setOnClickListener(v -> goToWelcome());

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateDots(position);

                if (position == onboardingItems.size() - 1) {
                    nextButton.setText("Get Started");
                } else {
                    nextButton.setText("Next");
                }
            }
        });

        nextButton.setOnClickListener(v -> {
            int current = onboardingViewPager.getCurrentItem();

            if (current < onboardingItems.size() - 1) {
                onboardingViewPager.setCurrentItem(current + 1);
            } else {
                goToWelcome();
            }
        });
    }

    private void goToWelcome() {
        startActivity(new Intent(OnboardingActivity.this, WelcomeActivity.class));
        finish();
    }

    private void setupOnboardingItems() {
        onboardingItems = new ArrayList<>();

        onboardingItems.add(new OnboardingItem(
                R.drawable.onboarding_welcome,
                "Welcome to Moja Market",
                "Your trusted peer-to-peer marketplace for buying and selling items directly with other users."
        ));

        onboardingItems.add(new OnboardingItem(
                R.drawable.onboarding_sell,
                "List Your Items",
                "Post items you want to sell with photos, descriptions, and set your own prices."
        ));

        onboardingItems.add(new OnboardingItem(
                R.drawable.onboarding_chat,
                "Chat & Negotiate",
                "Communicate directly with buyers and sellers and negotiate deals safely."
        ));

        onboardingItems.add(new OnboardingItem(
                R.drawable.onboarding_rating,
                "Rate & Review",
                "Build trust by rating your transactions and reviewing other users."
        ));
    }

    private void setupDots() {
        AppCompatImageView[] dots = new AppCompatImageView[onboardingItems.size()];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new AppCompatImageView(this);
            dots[i].setBackgroundResource(R.drawable.dot_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dots[i].setLayoutParams(params);

            final int index = i;
            dots[i].setOnClickListener(v -> onboardingViewPager.setCurrentItem(index));
            dotsLayout.addView(dots[i]);
        }
    }

    private void updateDots(int currentPosition) {
        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            if (i == currentPosition) {
                dotsLayout.getChildAt(i).setBackgroundResource(R.drawable.dot_active);
            } else {
                dotsLayout.getChildAt(i).setBackgroundResource(R.drawable.dot_inactive);
            }
        }
    }
}
package com.example.mojamarket;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private TextView skipText;
    private LinearLayout dotsLayout;
    private List<OnboardingItem> onboardingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        onboardingViewPager = findViewById(R.id.onboardingViewPager);
        nextButton = findViewById(R.id.nextButton);
        skipText = findViewById(R.id.skipText);
        dotsLayout = findViewById(R.id.dotsLayout);

        skipText.setOnClickListener(v -> completeOnboarding());

        setupOnboardingItems();

        OnboardingAdapter adapter = new OnboardingAdapter(onboardingItems);
        onboardingViewPager.setAdapter(adapter);

        setupDots();
        updateDots(0);

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
                completeOnboarding();
            }
        });

    }

    private void setupOnboardingItems() {
        onboardingItems = new ArrayList<>();

        onboardingItems.add(new OnboardingItem(
                android.R.drawable.ic_menu_agenda,
                "Welcome to Moja Market",
                "Your trusted peer-to-peer marketplace for buying and selling items directly with other users.",
                R.drawable.bg_circle_blue
        ));

        onboardingItems.add(new OnboardingItem(
                android.R.drawable.ic_menu_upload,
                "List Your Items",
                "Post items you want to sell with photos, descriptions, and set your own prices. Manage your listings easily.",
                R.drawable.bg_circle_purple
        ));

        onboardingItems.add(new OnboardingItem(
                android.R.drawable.ic_dialog_email,
                "Chat & Negotiate",
                "Communicate directly with buyers and sellers through our built-in messaging system. Negotiate and close deals safely.",
                R.drawable.bg_circle_green
        ));

        onboardingItems.add(new OnboardingItem(
                android.R.drawable.ic_lock_lock,
                "Rate & Review",
                "Build trust in the community by rating your transactions and checking seller ratings before you buy.",
                R.drawable.bg_circle_orange
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

    private void completeOnboarding() {
        Intent intent = new Intent(OnboardingActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
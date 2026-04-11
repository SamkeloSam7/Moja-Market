package com.example.mojamarket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView registerText;
    private MaterialButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backButton = findViewById(R.id.backButton);
        registerText = findViewById(R.id.registerText);
        loginButton = findViewById(R.id.loginButton);

        backButton.setOnClickListener(v -> finish());

        loginButton.setOnClickListener(v -> {
            getSharedPreferences("MojaMarketPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isLoggedIn", true)
                    .apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        String fullText = "Don't have an account? Register";
        SpannableString spannableString = new SpannableString(fullText);

        ClickableSpan registerClickable = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#2563EB"));
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };

        int start = fullText.indexOf("Register");
        int end = start + "Register".length();

        spannableString.setSpan(registerClickable, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        registerText.setText(spannableString);
        registerText.setMovementMethod(LinkMovementMethod.getInstance());
        registerText.setHighlightColor(Color.TRANSPARENT);
    }
}
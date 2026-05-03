package com.example.mojamarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojamarket.auth.Login;
import com.example.mojamarket.models.User;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.example.mojamarket.auth.Login;
import com.example.mojamarket.models.User;

public class LoginActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView registerText;
    private MaterialButton loginButton;

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backButton = findViewById(R.id.backButton);
        registerText = findViewById(R.id.registerText);
        loginButton = findViewById(R.id.loginButton);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        backButton.setOnClickListener(v -> finish());

        loginButton.setOnClickListener(v -> {
            String loginIdentifier = getText(emailInput);
            String password        = getText(passwordInput);

            if (TextUtils.isEmpty(loginIdentifier) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email/username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // async network call to call backend
            Login.loginUserIn(loginIdentifier, password, this, new Login.LoginCallback() {
                @Override
                public void onSuccess(User user) {
                    runOnUiThread(() -> {


                        getSharedPreferences("MojaMarketPrefs", MODE_PRIVATE)
                                .edit()
                                .putBoolean("isLoggedIn", true)
                                .putString("remembered_login_identifier", loginIdentifier)
                                .putString("remembered_name",     user.getName())
                                .putString("remembered_username", user.getUsername())
                                .putString("remembered_email",    user.getEmail())
                                .apply();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    });
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Login failed: " + message, Toast.LENGTH_LONG).show();
                    });
                }
            });
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

    private String getText(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
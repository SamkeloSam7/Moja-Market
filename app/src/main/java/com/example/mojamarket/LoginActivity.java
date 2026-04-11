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

import com.example.mojamarket.auth.Login;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView registerText;
    private MaterialButton loginButton;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backButton = findViewById(R.id.backButton);
        registerText = findViewById(R.id.registerText);
        loginButton = findViewById(R.id.loginButton);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        emailInput.setError(null);
        passwordInput.setError(null);

        backButton.setOnClickListener(v -> finish());

        loginButton.setOnClickListener(v -> {
            String loginID = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
            String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";

            emailInput.setError(null);
            passwordInput.setError(null);

            boolean isFormValid = true;

            if (loginID.isEmpty()) {
                emailInput.setError("Email or Username required");
                isFormValid = false;
            }
            if (password.isEmpty()) {
                passwordInput.setError("Password required");
                isFormValid = false;
            }

            if (!isFormValid) return;

            boolean isvalidLogin = Login.LoginUserIn(loginID, password, this);
            if (isvalidLogin) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                emailInput.setError("Invalid login credentials");
                passwordInput.setError("Invalid login credentials");
            }
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
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton backButton;
    private MaterialButton createAccountButton;
    private TextView loginText;

    private TextInputEditText nameInput;
    private TextInputEditText surnameInput;
    private TextInputEditText emailInput;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backButton = findViewById(R.id.backButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginText = findViewById(R.id.loginText);

        nameInput = findViewById(R.id.nameInput);
        surnameInput = findViewById(R.id.surnameInput);
        emailInput = findViewById(R.id.emailInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        backButton.setOnClickListener(v -> finish());

        createAccountButton.setOnClickListener(v -> {
            String name = getText(nameInput);
            String surname = getText(surnameInput);
            String email = getText(emailInput);
            String username = getText(usernameInput);
            String password = getText(passwordInput);
            String confirmPassword = getText(confirmPasswordInput);

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() ||
                    username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        String fullText = "Already have an account? Login";
        SpannableString spannableString = new SpannableString(fullText);

        ClickableSpan loginClickable = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#2563EB"));
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };

        int start = fullText.indexOf("Login");
        int end = start + "Login".length();

        spannableString.setSpan(loginClickable, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        loginText.setText(spannableString);
        loginText.setMovementMethod(LinkMovementMethod.getInstance());
        loginText.setHighlightColor(Color.TRANSPARENT);
    }

    private String getText(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
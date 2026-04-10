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

import com.example.mojamarket.auth.Register;
import com.example.mojamarket.utility.Validation;
import com.example.mojamarket.utility.ValidationResult;
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
            // Get inputs from user
            String name = getText(nameInput);
            String surname = getText(surnameInput);
            String email = getText(emailInput);
            String username = getText(usernameInput);
            String password = getText(passwordInput);
            String confirmPassword = getText(confirmPasswordInput);

            //Validate the input
            ValidationResult nameRes = Validation.validateName(name);
            ValidationResult surnameRes = Validation.validateSurname(surname);
            ValidationResult emailRes = Validation.validateEmail(email);
            ValidationResult userRes = Validation.validateUsername(username);
            ValidationResult passRes = Validation.validatePassword(password);

            // Check if input is valid and set errors
            boolean isFormValid = true;

            if (!nameRes.isValid) {
                nameInput.setError(nameRes.message);
                isFormValid = false;
            }
            if (!surnameRes.isValid) {
                surnameInput.setError(surnameRes.message);
                isFormValid = false;
            }
            if (!emailRes.isValid) {
                emailInput.setError(emailRes.message);
                isFormValid = false;
            }
            if (!userRes.isValid) {
                usernameInput.setError(userRes.message);
                isFormValid = false;
            }
            if (!passRes.isValid) {
                passwordInput.setError(passRes.message);
                isFormValid = false;
            }

            // Manual check if passwords match
            if (!password.equals(confirmPassword)) {
                confirmPasswordInput.setError("Passwords do not match");
                isFormValid = false;
            }

            //If errors are found stop
            if (!isFormValid) return;

            // If input is valid Register the user
            Register.RegisterUser(name, surname, username, email, password,this);


            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
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
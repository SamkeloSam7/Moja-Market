package com.example.mojamarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojamarket.auth.Login;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class QuickLoginActivity extends AppCompatActivity {

    private TextView quickLoginGreeting;
    private TextView quickLoginSubtitle;
    private TextView quickLoginUsername;
    private TextView quickLoginInitial;
    private TextInputEditText quickPasswordInput;
    private MaterialButton signInButton;
    private MaterialButton switchAccountButton;
    private ImageButton backButton;

    private SharedPreferences prefs;

    private String rememberedName;
    private String rememberedUsername;
    private String rememberedLoginIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_login);

        prefs = getSharedPreferences("MojaMarketPrefs", MODE_PRIVATE);

        quickLoginGreeting = findViewById(R.id.quickLoginGreeting);
        quickLoginSubtitle = findViewById(R.id.quickLoginSubtitle);
        quickPasswordInput = findViewById(R.id.quickPasswordInput);
        signInButton = findViewById(R.id.signInButton);
        switchAccountButton = findViewById(R.id.switchAccountButton);
        backButton = findViewById(R.id.backButton);

        rememberedName = prefs.getString("remembered_name", "");
        rememberedUsername = prefs.getString("remembered_username", "");
        rememberedLoginIdentifier = prefs.getString("remembered_login_identifier", "");

        if (TextUtils.isEmpty(rememberedUsername) && TextUtils.isEmpty(rememberedName)) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        String displayName = !TextUtils.isEmpty(rememberedName) ? rememberedName : rememberedUsername;

        quickLoginGreeting.setText("Hello again, " + displayName);
        quickLoginSubtitle.setText("Welcome back to Moja Market");



        backButton.setOnClickListener(v -> finish());

        signInButton.setOnClickListener(v -> {
            String password = getText(quickPasswordInput);

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = Login.LoginUserIn(rememberedLoginIdentifier, password, this);

            if (success) {
                prefs.edit()
                        .putBoolean("isLoggedIn", true)
                        .apply();

                Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        });

        switchAccountButton.setOnClickListener(v -> {
            prefs.edit()
                    .remove("remembered_name")
                    .remove("remembered_username")
                    .remove("remembered_email")
                    .remove("remembered_login_identifier")
                    .putBoolean("isLoggedIn", false)
                    .apply();

            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String getText(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
package com.example.mojamarket;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MojaMarketPrefs";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_USERNAME = "user_username";
    private static final String KEY_LOGGED_IN = "isLoggedIn";

    private ImageButton backButton;
    private TextView settingsName;
    private TextView settingsEmail;
    private TextView settingsUsername;
    private TextView themeStatusText;
    private MaterialButton editAccountButton;
    private MaterialButton logoutButton;
    private MaterialSwitch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.backButton);
        settingsName = findViewById(R.id.settingsName);
        settingsEmail = findViewById(R.id.settingsEmail);
        settingsUsername = findViewById(R.id.settingsUsername);
        themeStatusText = findViewById(R.id.themeStatusText);
        editAccountButton = findViewById(R.id.editAccountButton);
        logoutButton = findViewById(R.id.logoutButton);
        themeSwitch = findViewById(R.id.themeSwitch);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        loadUserData();
        setupThemeSection();

        editAccountButton.setOnClickListener(v -> showEditAccountDialog());

        logoutButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MojaMarketPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Intent intent = new Intent(SettingsActivity.this, OnboardingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String name = prefs.getString(KEY_NAME, "Samkelo Mthembu");
        String email = prefs.getString(KEY_EMAIL, "samkelosthembiso7@gmail.com");
        String username = prefs.getString(KEY_USERNAME, "samkelo");

        settingsName.setText(name);
        settingsEmail.setText(email);
        settingsUsername.setText("@" + username);
    }

    private void setupThemeSection() {
        String currentTheme = ThemeUtils.getCurrentTheme(this);
        boolean isDark = "dark".equals(currentTheme);

        themeSwitch.setChecked(isDark);
        updateThemeStatusText(isDark);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeUtils.setTheme(SettingsActivity.this, isChecked ? "dark" : "light");
            recreate();
        });
    }

    private void updateThemeStatusText(boolean isDark) {
        if (isDark) {
            themeStatusText.setText("Currently Dark Mode");
        } else {
            themeStatusText.setText("Currently Light Mode");
        }
    }

    private void showEditAccountDialog() {
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_account, null);

        TextInputEditText editNameInput = dialogView.findViewById(R.id.editNameInput);
        TextInputEditText editEmailInput = dialogView.findViewById(R.id.editEmailInput);
        TextInputEditText editUsernameInput = dialogView.findViewById(R.id.editUsernameInput);
        MaterialButton saveAccountChangesButton = dialogView.findViewById(R.id.saveAccountChangesButton);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        editNameInput.setText(prefs.getString(KEY_NAME, "Samkelo Mthembu"));
        editEmailInput.setText(prefs.getString(KEY_EMAIL, "samkelosthembiso7@gmail.com"));
        editUsernameInput.setText(prefs.getString(KEY_USERNAME, "samkelo"));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        saveAccountChangesButton.setOnClickListener(v -> {
            String name = getText(editNameInput);
            String email = getText(editEmailInput);
            String username = getText(editUsernameInput);

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            prefs.edit()
                    .putString(KEY_NAME, name)
                    .putString(KEY_EMAIL, email)
                    .putString(KEY_USERNAME, username)
                    .apply();

            loadUserData();
            Toast.makeText(this, "Account updated successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
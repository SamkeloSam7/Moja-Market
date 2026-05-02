package com.example.mojamarket;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojamarket.models.User;
import com.example.mojamarket.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView settingsName;
    private TextView settingsEmail;
    private TextView settingsUsername;
    private TextView themeStatusText;
    private MaterialButton editAccountButton;
    private MaterialButton logoutButton;
    private MaterialSwitch themeSwitch;

    private String name, surname, username, email;

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

        User user = SessionManager.getLoggedInUser(this);
        name = user.getName();
        surname = user.getSurname();
        username = user.getUsername();
        email = user.getEmail();

        settingsName.setText(name + " " + surname);
        settingsEmail.setText(email);
        settingsUsername.setText("@" + username);

        setupThemeSection();

        editAccountButton.setOnClickListener(v -> showEditAccountDialog());

        logoutButton.setOnClickListener(v -> {
            getSharedPreferences("MojaMarketPrefs", MODE_PRIVATE).edit().clear().apply();

            Intent intent = new Intent(SettingsActivity.this, OnboardingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
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
        themeStatusText.setText(isDark ? "Currently Dark Mode" : "Currently Light Mode");
    }

    private void showEditAccountDialog() {
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_account, null);

        TextInputEditText editNameInput = dialogView.findViewById(R.id.editNameInput);
        TextInputEditText editEmailInput = dialogView.findViewById(R.id.editEmailInput);
        TextInputEditText editUsernameInput = dialogView.findViewById(R.id.editUsernameInput);
        MaterialButton saveAccountChangesButton = dialogView.findViewById(R.id.saveAccountChangesButton);

        editNameInput.setText(name);
        editEmailInput.setText(email);
        editUsernameInput.setText(username);

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

            settingsName.setText(name + " " + surname);
            settingsEmail.setText(email);
            settingsUsername.setText("@" + username);

            Toast.makeText(this, "Account updated successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
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
import com.example.mojamarket.network.UserRepository;
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

        TextInputEditText editEmailInput           = dialogView.findViewById(R.id.editEmailInput);
        TextInputEditText editUsernameInput        = dialogView.findViewById(R.id.editUsernameInput);
        TextInputEditText editPasswordInput        = dialogView.findViewById(R.id.editPasswordInput);
        TextInputEditText editConfirmPasswordInput = dialogView.findViewById(R.id.editConfirmPasswordInput);
        MaterialButton    saveButton               = dialogView.findViewById(R.id.saveProfileChangesButton);

        editEmailInput.setText(email);
        editUsernameInput.setText(username);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        saveButton.setOnClickListener(v -> {
            String newEmail    = getText(editEmailInput);
            String newUsername = getText(editUsernameInput);
            String newPassword = getText(editPasswordInput);
            String confirmPwd  = getText(editConfirmPasswordInput);

            if (!TextUtils.isEmpty(newPassword) || !TextUtils.isEmpty(confirmPwd)) {
                if (!newPassword.equals(confirmPwd)) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            User currentUser = SessionManager.getLoggedInUser(this);

            String emailToSend    = TextUtils.isEmpty(newEmail)    ? currentUser.getEmail()    : newEmail;
            String usernameToSend = TextUtils.isEmpty(newUsername) ? currentUser.getUsername() : newUsername;
            String passwordToSend = TextUtils.isEmpty(newPassword) ? currentUser.getPassword() : newPassword;

            saveButton.setEnabled(false);

            UserRepository.updateProfile(
                    currentUser.getUserID(),
                    name,
                    surname,
                    usernameToSend,
                    emailToSend,
                    passwordToSend,
                    new UserRepository.ActionCallback() {
                        @Override
                        public void onSuccess(String message) {
                            runOnUiThread(() -> {
                                saveButton.setEnabled(true);

                                currentUser.setEmail(emailToSend);
                                currentUser.setUsername(usernameToSend);
                                SessionManager.setLoggedInUser(currentUser);

                                email    = emailToSend;
                                username = usernameToSend;

                                settingsEmail.setText(emailToSend);
                                settingsUsername.setText("@" + usernameToSend);

                                Toast.makeText(SettingsActivity.this, "Account updated successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                        }

                        @Override
                        public void onFailure(String message) {
                            runOnUiThread(() -> {
                                saveButton.setEnabled(true);
                                Toast.makeText(SettingsActivity.this, "Update failed: " + message, Toast.LENGTH_LONG).show();
                            });
                        }
                    }
            );
        });

        dialog.show();
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
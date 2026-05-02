package com.example.mojamarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mojamarket.models.Want;
import com.example.mojamarket.session.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

public class WantDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView wantStatus;
    private TextView wantItemName;
    private TextView wantBudget;
    private TextView wantDescription;
    private TextView requesterName;
    private TextView requesterUsername;
    private TextView postedDate;
    private MaterialButton respondButton;
    private LinearLayout ownRequestBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_detail);

        backButton = findViewById(R.id.backButton);
        wantStatus = findViewById(R.id.wantStatus);
        wantItemName = findViewById(R.id.wantItemName);
        wantBudget = findViewById(R.id.wantBudget);
        wantDescription = findViewById(R.id.wantDescription);
        requesterName = findViewById(R.id.requesterName);
        requesterUsername = findViewById(R.id.requesterUsername);
        postedDate = findViewById(R.id.postedDate);
        respondButton = findViewById(R.id.respondButton);
        ownRequestBox = findViewById(R.id.ownRequestBox);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        Want want = SessionManager.getCurrentClickedWantRequest(this);

        if (want == null) {
            Toast.makeText(this, "Request not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        wantItemName.setText(want.getItem());
        wantDescription.setText(want.getDescription());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        wantBudget.setText("R" + numberFormat.format(want.getBudget()));

        postedDate.setText("Posted on " + want.getDatePosted().toString());

        if (want.getBuyer() != null) {
            requesterName.setText(want.getBuyer().getName() + " " + want.getBuyer().getSurname());
            requesterUsername.setText("@" + want.getBuyer().getUsername());
        } else {
            requesterName.setText("Unknown");
            requesterUsername.setText("@unknown");
        }

        if (!want.isWantStatus()) {
            wantStatus.setText("Fulfilled");
            wantStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            wantStatus.setTextColor(0xFF6B7280);
        } else {
            wantStatus.setText("Actively Looking");
            wantStatus.setBackgroundResource(R.drawable.bg_looking_badge);
            wantStatus.setTextColor(0xFFFFFFFF);
        }

        boolean ownRequest = SessionManager.getLoggedInUser(this) != null &&
                want.getBuyer() != null &&
                want.getBuyer().getUserID().equals(SessionManager.getLoggedInUser(this).getUserID());

        if (!ownRequest && want.isWantStatus()) {
            respondButton.setVisibility(View.VISIBLE);
            ownRequestBox.setVisibility(View.GONE);

            respondButton.setOnClickListener(v ->
                    startActivity(new Intent(WantDetailActivity.this, ChatActivity.class)));

        } else if (ownRequest) {
            respondButton.setVisibility(View.GONE);
            ownRequestBox.setVisibility(View.VISIBLE);
        } else {
            respondButton.setVisibility(View.GONE);
            ownRequestBox.setVisibility(View.GONE);
        }
    }
}
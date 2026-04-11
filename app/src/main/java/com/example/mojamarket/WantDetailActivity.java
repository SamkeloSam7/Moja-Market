package com.example.mojamarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        Intent intent = getIntent();

        String itemName = intent.getStringExtra("item_name");
        String description = intent.getStringExtra("description");
        double budget = intent.getDoubleExtra("budget", 0);
        String datePosted = intent.getStringExtra("date_posted");
        String username = intent.getStringExtra("username");
        boolean fulfilled = intent.getBooleanExtra("fulfilled", false);
        boolean ownRequest = intent.getBooleanExtra("own_request", false);
        int requestId = intent.getIntExtra("request_id", -1);

        if (itemName == null || description == null || datePosted == null || username == null) {
            Toast.makeText(this, "Request not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        wantItemName.setText(itemName);
        wantDescription.setText(description);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        wantBudget.setText("R" + numberFormat.format((int) budget));

        requesterName.setText("Samkelo Mthembu");
        requesterUsername.setText("@" + username);
        postedDate.setText("Posted on " + formatDate(datePosted));

        if (fulfilled) {
            wantStatus.setText("Fulfilled");
            wantStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            wantStatus.setTextColor(0xFF6B7280);
        } else {
            wantStatus.setText("Actively Looking");
            wantStatus.setBackgroundResource(R.drawable.bg_looking_badge);
            wantStatus.setTextColor(0xFFFFFFFF);
        }

        if (!ownRequest && !fulfilled) {
            respondButton.setVisibility(View.VISIBLE);
            ownRequestBox.setVisibility(View.GONE);

            respondButton.setOnClickListener(v -> {
                Intent chatIntent = new Intent(WantDetailActivity.this, ChatActivity.class);
                chatIntent.putExtra("user_id", requestId);
                chatIntent.putExtra("name", "Samkelo Mthembu");
                chatIntent.putExtra("username", username);
                startActivity(chatIntent);
            });
        } else if (ownRequest) {
            respondButton.setVisibility(View.GONE);
            ownRequestBox.setVisibility(View.VISIBLE);
        } else {
            respondButton.setVisibility(View.GONE);
            ownRequestBox.setVisibility(View.GONE);
        }
    }

    private String formatDate(String rawDate) {
        try {
            if (rawDate.contains("/")) {
                SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = input.parse(rawDate);
                SimpleDateFormat output = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
                return output.format(date);
            }
        } catch (Exception ignored) {
        }
        return rawDate;
    }
}
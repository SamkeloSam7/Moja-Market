package com.example.mojamarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mojamarket.models.Want;
import com.example.mojamarket.utility.Helper;
import com.example.mojamarket.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import java.text.NumberFormat;
import java.util.Locale;

public class WantDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView wantItemName, wantBudget, wantDescription, requesterName, requesterUsername, datePosted;
    private MaterialButton respondButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_detail);

        // Initializes UI components and links them to the layout XML
        setupViews();

        Want want = SessionManager.getCurrentClickedWantRequest(this);
        if (want != null) {
            // Fills the screen with the cached data from the feed
            displayWantData(want);
        } else {
            finish();
        }
    }

    // Connects views and assigns the back navigation listener
    private void setupViews() {
        backButton = findViewById(R.id.backButton);
        wantItemName = findViewById(R.id.wantItemName);
        wantBudget = findViewById(R.id.wantBudget);
        wantDescription = findViewById(R.id.wantDescription);
        requesterName = findViewById(R.id.requesterName);
        datePosted = findViewById(R.id.request_postedDate);
        requesterUsername = findViewById(R.id.requesterUsername);
        backButton.setOnClickListener(v -> finish());
    }

    // Populates text fields and formats currency for the budget display
    private void displayWantData(Want want) {
        wantItemName.setText(want.getItem());
        wantDescription.setText(want.getDescription());
        wantBudget.setText("R" + NumberFormat.getNumberInstance(Locale.US).format(want.getBudget()));
        datePosted.setText("Posted on " + Helper.formatDate(want.getDatePosted()));

        if (want.getBuyer() != null) {
            requesterName.setText(want.getBuyer().getName() + " " + want.getBuyer().getSurname());
            requesterUsername.setText("@" + want.getBuyer().getUsername());
        }
    }
}
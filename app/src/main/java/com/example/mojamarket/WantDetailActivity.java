package com.example.mojamarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mojamarket.models.Want;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.network.ChatRepository;
import com.google.android.material.button.MaterialButton;
import java.text.NumberFormat;
import java.util.Locale;

public class WantDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView wantItemName, wantBudget, wantDescription, requesterName, requesterUsername;
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
        requesterUsername = findViewById(R.id.requesterUsername);
        respondButton = findViewById(R.id.respondButton);
        backButton.setOnClickListener(v -> finish());
    }

    // Populates text fields and formats currency for the budget display
    private void displayWantData(Want want) {
        wantItemName.setText(want.getItem());
        wantDescription.setText(want.getDescription());
        wantBudget.setText("R" + NumberFormat.getNumberInstance(Locale.US).format(want.getBudget()));

        if (want.getBuyer() != null) {
            requesterName.setText(want.getBuyer().getName() + " " + want.getBuyer().getSurname());
            requesterUsername.setText("@" + want.getBuyer().getUsername());
        }

        initializeRespondButton(want);
    }

    // Manages button visibility and triggers the chat creation through ChatRepository
    private void initializeRespondButton(Want want) {
        String currentUserID = SessionManager.getLoggedInUser(this) != null ? SessionManager.getLoggedInUser(this).getUserID() : "";
        boolean isSelf = want.getBuyer() != null && want.getBuyer().getUserID().equals(currentUserID);

        respondButton.setVisibility(isSelf ? View.GONE : View.VISIBLE);
        respondButton.setOnClickListener(v -> {
            respondButton.setEnabled(false);
            // Calls the centralized ChatRepository, passing wantID while keeping itemID null
            ChatRepository.createChat(currentUserID, want.getBuyer().getUserID(), null, want.getId(), new ChatRepository.ActionCallback() {
                @Override
                public void onSuccess(String chatID) {
                    runOnUiThread(() -> {
                        respondButton.setEnabled(true);
                        Intent intent = new Intent(WantDetailActivity.this, ChatActivity.class);
                        intent.putExtra("chatID", chatID);
                        intent.putExtra("currentUserID", currentUserID);
                        intent.putExtra("name", want.getBuyer().getName());
                        intent.putExtra("username", want.getBuyer().getUsername());
                        startActivity(intent);
                    });
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> {
                        respondButton.setEnabled(true);
                        Toast.makeText(WantDetailActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }
}
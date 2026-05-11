package com.example.mojamarket;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojamarket.models.Message;
import com.example.mojamarket.network.ApiClient;
import com.example.mojamarket.network.ChatRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView chatHeaderAvatar;
    private TextView chatHeaderName;
    private TextView chatHeaderUsername;
    private RecyclerView messagesRecyclerView;
    private MaterialButton confirmSaleButton;
    private MaterialButton rateButton;
    private TextInputEditText messageInput;
    private MaterialButton sendButton;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    private final Handler handler = new Handler();
    private Runnable pollingRunnable;

    private static final int POLL_INTERVAL_MS = 3000;

    private String lastMessageTime = "2000-01-01 00:00:00";
    private String chatID;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton           = findViewById(R.id.backButton);
        chatHeaderAvatar     = findViewById(R.id.chatHeaderAvatar);
        chatHeaderName       = findViewById(R.id.chatHeaderName);
        chatHeaderUsername   = findViewById(R.id.chatHeaderUsername);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        confirmSaleButton    = findViewById(R.id.confirmSaleButton);
        rateButton           = findViewById(R.id.rateButton);
        messageInput         = findViewById(R.id.messageInput);
        sendButton           = findViewById(R.id.sendButton);

        chatID        = getIntent().getStringExtra("chatID");
        currentUserID = getIntent().getStringExtra("currentUserID");

        String name     = getIntent().getStringExtra("name");
        String username = getIntent().getStringExtra("username");

        if (name == null)     name     = "Unknown User";
        if (username == null) username = "unknown";

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        chatHeaderName.setText(name);
        chatHeaderUsername.setText("@" + username);

        if (!name.isEmpty()) {
            chatHeaderAvatar.setText(name.substring(0, 1).toUpperCase());
        }

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageList    = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> sendMessage());

        confirmSaleButton.setOnClickListener(v ->
                Toast.makeText(this, "Sale confirmed!", Toast.LENGTH_SHORT).show()
        );

        rateButton.setOnClickListener(v -> showRatingDialog());

        loadChatHistory();
    }

    // Loads the full history once on open, then kicks off polling
    private void loadChatHistory() {
        ChatRepository.getChatHistory(chatID, currentUserID, new ChatRepository.MessagesCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                for (Message m : messages) {
                    messageList.add(m);
                    lastMessageTime = m.getTimeSent();
                }

                messageAdapter.notifyDataSetChanged();

                if (!messageList.isEmpty()) {
                    messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                }

                // Start polling only after history is loaded so lastMessageTime is correct
                startPolling();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(ChatActivity.this, "Failed to load chat", Toast.LENGTH_SHORT).show();
                // Still start polling so the screen isn't dead
                startPolling();
            }
        });
    }

    private void sendMessage() {
        String text = messageInput.getText() == null
                ? ""
                : messageInput.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            return;
        }

        // Clear input immediately so it feels responsive
        messageInput.setText("");

        ChatRepository.sendMessage(chatID, currentUserID, text, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                // Pull new messages straight away so sender sees their own message
                fetchNewMessages();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(
                        ChatActivity.this,
                        "Failed to send message",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    // Polling loop that runs every 3 seconds
    private void startPolling() {
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                fetchNewMessages();
                handler.postDelayed(this, POLL_INTERVAL_MS);
            }
        };

        handler.post(pollingRunnable);
    }

    private void stopPolling() {
        if (pollingRunnable != null) {
            handler.removeCallbacks(pollingRunnable);
        }
    }

    // Fetches only messages newer than lastMessageTime
    private void fetchNewMessages() {
        ChatRepository.getNewMessages(chatID, lastMessageTime, currentUserID, new ChatRepository.MessagesCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (messages.isEmpty()) {
                    return;
                }

                for (Message m : messages) {
                    messageList.add(m);
                    lastMessageTime = m.getTimeSent();
                }

                messageAdapter.notifyDataSetChanged();
                messagesRecyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onFailure(String message) {
                //don't interrupt the user for a missed poll
            }
        });
    }

    private void showRatingDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rate_transaction, null);

        TextView star1 = dialogView.findViewById(R.id.star1);
        TextView star2 = dialogView.findViewById(R.id.star2);
        TextView star3 = dialogView.findViewById(R.id.star3);
        TextView star4 = dialogView.findViewById(R.id.star4);
        TextView star5 = dialogView.findViewById(R.id.star5);

        EditText ratingCommentInput = dialogView.findViewById(R.id.ratingCommentInput);

        final int[] rating = {0};

        View.OnClickListener starClick = view -> {
            if (view == star1)      rating[0] = 1;
            else if (view == star2) rating[0] = 2;
            else if (view == star3) rating[0] = 3;
            else if (view == star4) rating[0] = 4;
            else if (view == star5) rating[0] = 5;

            updateStars(rating[0], star1, star2, star3, star4, star5);
        };

        star1.setOnClickListener(starClick);
        star2.setOnClickListener(starClick);
        star3.setOnClickListener(starClick);
        star4.setOnClickListener(starClick);
        star5.setOnClickListener(starClick);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        MaterialButton submitRatingButton = dialogView.findViewById(R.id.submitRatingButton);

        submitRatingButton.setOnClickListener(v -> {
            if (rating[0] == 0) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Rating submitted successfully!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateStars(int rating, TextView... stars) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setTextColor(
                    i < rating
                            ? Color.parseColor("#FACC15")
                            : Color.parseColor("#D1D5DB")
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPolling();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Only restart polling if history is already loaded
        if (chatID != null && pollingRunnable != null) {
            startPolling();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPolling();
    }
}
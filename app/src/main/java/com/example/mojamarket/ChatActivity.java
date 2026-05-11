package com.example.mojamarket;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private TextView chatHeaderAvatar, chatHeaderName, chatHeaderUsername;
    private RecyclerView messagesRecyclerView;
    private MaterialButton confirmSaleButton, rateButton, sendButton;
    private TextInputEditText messageInput;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pollingRunnable;

    private static final int POLL_INTERVAL_MS = 3000;
    private String lastMessageTime = "2000-01-01 00:00:00";
    private String chatID, currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setupViews();

        chatID        = getIntent().getStringExtra("chatID");
        currentUserID = getIntent().getStringExtra("currentUserID");
        String name     = getIntent().getStringExtra("name");
        String username = getIntent().getStringExtra("username");

        if (chatID == null || currentUserID == null) {
            finish();
            return;
        }

        displayHeader(name, username);
        setupRecyclerView();

        sendButton.setOnClickListener(v -> sendMessage());
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        confirmSaleButton.setOnClickListener(v -> Toast.makeText(this, "Sale confirmed!", Toast.LENGTH_SHORT).show());
        rateButton.setOnClickListener(v -> showRatingDialog());

        loadChatHistory();
    }

    private void setupViews() {
        backButton           = findViewById(R.id.backButton);
        chatHeaderAvatar     = findViewById(R.id.chatHeaderAvatar);
        chatHeaderName       = findViewById(R.id.chatHeaderName);
        chatHeaderUsername   = findViewById(R.id.chatHeaderUsername);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        confirmSaleButton    = findViewById(R.id.confirmSaleButton);
        rateButton           = findViewById(R.id.rateButton);
        messageInput         = findViewById(R.id.messageInput);
        sendButton           = findViewById(R.id.sendButton);
    }

    private void displayHeader(String name, String username) {
        chatHeaderName.setText(name != null ? name : "Unknown User");
        chatHeaderUsername.setText("@" + (username != null ? username : "unknown"));
        if (name != null && !name.isEmpty()) {
            chatHeaderAvatar.setText(name.substring(0, 1).toUpperCase());
        }
    }

    private void setupRecyclerView() {
        messageList    = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messageAdapter);
    }

    // Loads full message history once when the chat screen opens
    private void loadChatHistory() {
        ChatRepository.getChatHistory(chatID, currentUserID, new ChatRepository.MessagesCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                runOnUiThread(() -> {
                    messageList.clear();
                    for (Message m : messages) {
                        messageList.add(m);
                        lastMessageTime = m.getTimeSent();
                    }
                    messageAdapter.notifyDataSetChanged();
                    scrollToBottom();
                    startPolling();
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(ChatActivity.this, "Error loading history", Toast.LENGTH_SHORT).show();
                    startPolling();
                });
            }
        });
    }

    private void sendMessage() {
        String text = messageInput.getText() != null ? messageInput.getText().toString().trim() : "";
        if (TextUtils.isEmpty(text)) return;

        messageInput.setText("");

        ChatRepository.sendMessage(chatID, currentUserID, text, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                fetchNewMessages();
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Failed to send", Toast.LENGTH_SHORT).show());
            }
        });
    }

    // Polling loop that runs every 3 seconds
    private void startPolling() {
        stopPolling();
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                fetchNewMessages();
                handler.postDelayed(this, POLL_INTERVAL_MS);
            }
        };
        handler.postDelayed(pollingRunnable, POLL_INTERVAL_MS);
    }

    private void stopPolling() {
        if (pollingRunnable != null) {
            handler.removeCallbacks(pollingRunnable);
        }
    }

    // Short polling — fetches only messages newer than lastMessageTime
    private void fetchNewMessages() {
        ChatRepository.getNewMessages(chatID, lastMessageTime, currentUserID, new ChatRepository.MessagesCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (messages.isEmpty()) return;

                runOnUiThread(() -> {
                    int startPos = messageList.size();
                    for (Message m : messages) {
                        messageList.add(m);
                        lastMessageTime = m.getTimeSent();
                    }
                    messageAdapter.notifyItemRangeInserted(startPos, messages.size());
                    scrollToBottom();
                });
            }

            @Override
            public void onFailure(String message) {
                // Silent fail to avoid interrupting the user
            }
        });
    }

    private void scrollToBottom() {
        if (messageAdapter.getItemCount() > 0) {
            messagesRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        }
    }

    private void showRatingDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rate_transaction, null);
        TextView[] stars = {
                view.findViewById(R.id.star1), view.findViewById(R.id.star2),
                view.findViewById(R.id.star3), view.findViewById(R.id.star4),
                view.findViewById(R.id.star5)
        };

        final int[] selectedRating = {0};
        for (int i = 0; i < stars.length; i++) {
            int finalI = i;
            stars[i].setOnClickListener(v -> {
                selectedRating[0] = finalI + 1;
                updateStars(selectedRating[0], stars);
            });
        }

        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        view.findViewById(R.id.submitRatingButton).setOnClickListener(v -> {
            if (selectedRating[0] == 0) return;
            dialog.dismiss();
        });
        dialog.show();
    }

    private void updateStars(int rating, TextView... stars) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setTextColor(Color.parseColor(i < rating ? "#FACC15" : "#D1D5DB"));
        }
    }

    @Override
    protected void onPause() { super.onPause(); stopPolling(); }

    @Override
    protected void onResume() { super.onResume(); if (chatID != null) startPolling(); }

    @Override
    protected void onDestroy() { super.onDestroy(); stopPolling(); }
}
package com.example.mojamarket;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backButton = findViewById(R.id.backButton);
        chatHeaderAvatar = findViewById(R.id.chatHeaderAvatar);
        chatHeaderName = findViewById(R.id.chatHeaderName);
        chatHeaderUsername = findViewById(R.id.chatHeaderUsername);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        confirmSaleButton = findViewById(R.id.confirmSaleButton);
        rateButton = findViewById(R.id.rateButton);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        String name = getIntent().getStringExtra("name");
        String username = getIntent().getStringExtra("username");

        if (name == null) name = "Unknown User";
        if (username == null) username = "unknown";

        chatHeaderName.setText(name);
        chatHeaderUsername.setText("@" + username);
        chatHeaderAvatar.setText(name.substring(0, 1).toUpperCase());

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageList = new ArrayList<>();
        messageList.add(new Message("Hey, is this item still available?", "2:30 PM", false));
        messageList.add(new Message("Yes, it is still available.", "2:31 PM", true));
        messageList.add(new Message("Can we meet tomorrow?", "2:33 PM", false));
        messageList.add(new Message("Yes, tomorrow works for me.", "2:35 PM", true));

        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setAdapter(messageAdapter);
        messagesRecyclerView.scrollToPosition(messageList.size() - 1);

        sendButton.setOnClickListener(v -> {
            String text = messageInput.getText() == null ? "" : messageInput.getText().toString().trim();

            if (TextUtils.isEmpty(text)) {
                return;
            }

            messageList.add(new Message(text, "Now", true));
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            messagesRecyclerView.scrollToPosition(messageList.size() - 1);
            messageInput.setText("");
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
        });

        confirmSaleButton.setOnClickListener(v ->
                Toast.makeText(this, "Sale confirmed! Buyer can now rate the item.", Toast.LENGTH_SHORT).show()
        );

        rateButton.setOnClickListener(v -> showRatingDialog());
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
            if (view == star1) rating[0] = 1;
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
            if (i < rating) {
                stars[i].setTextColor(Color.parseColor("#FACC15"));
            } else {
                stars[i].setTextColor(Color.parseColor("#D1D5DB"));
            }
        }
    }
}
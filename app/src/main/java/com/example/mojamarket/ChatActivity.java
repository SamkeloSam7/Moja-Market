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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private RequestQueue requestQueue;

    private Handler handler = new Handler();
    private Runnable runnable;

    private String lastMessageTime = "2000-01-01 00:00:00";

    private String chatID;
    private String currentUserID;

    private static final String BASE_URL = "https://yourdomain.com/api/chats/";

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

        requestQueue = Volley.newRequestQueue(this);

        chatID = getIntent().getStringExtra("chatID");
        currentUserID = getIntent().getStringExtra("currentUserID");

        String name = getIntent().getStringExtra("name");
        String username = getIntent().getStringExtra("username");

        if (name == null) {
            name = "Unknown User";
        }

        if (username == null) {
            username = "unknown";
        }

        backButton.setOnClickListener(
                v -> getOnBackPressedDispatcher().onBackPressed()
        );

        chatHeaderName.setText(name);
        chatHeaderUsername.setText("@" + username);

        if (!name.isEmpty()) {
            chatHeaderAvatar.setText(
                    name.substring(0, 1).toUpperCase()
            );
        }

        messagesRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        messageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(messageList);

        messagesRecyclerView.setAdapter(messageAdapter);

        loadChatHistory();

        startPolling();

        sendButton.setOnClickListener(v -> sendMessage());

        confirmSaleButton.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Sale confirmed!",
                        Toast.LENGTH_SHORT
                ).show()
        );

        rateButton.setOnClickListener(v -> showRatingDialog());
    }

// Chat history

    private void loadChatHistory() {

        String url =
                BASE_URL +
                "getChatHistory.php?chatID=" +
                chatID;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                response -> {

                    try {

                        JSONArray data =
                                response.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {

                            JSONObject obj =
                                    data.getJSONObject(i);

                            String sender =
                                    obj.getString("message_sender");

                            String content =
                                    obj.getString("message_content");

                            String time =
                                    obj.getString("time_sent");

                            boolean isMine =
                                    sender.equals(currentUserID);

                            messageList.add(
                                    new Message(
                                            content,
                                            time,
                                            isMine
                                    )
                            );

                            lastMessageTime = time;
                        }

                        messageAdapter.notifyDataSetChanged();

                        if (!messageList.isEmpty()) {

                            messagesRecyclerView.scrollToPosition(
                                    messageList.size() - 1
                            );
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();

                        Toast.makeText(
                                this,
                                "JSON parsing error",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                },

                error -> Toast.makeText(
                        this,
                        "Failed to load chat",
                        Toast.LENGTH_SHORT
                ).show()
        );

        requestQueue.add(request);
    }

    private void sendMessage() {

        String text = messageInput.getText() == null
                ? ""
                : messageInput.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            return;
        }

        String url = BASE_URL + "sendMessage.php";

        JSONObject body = new JSONObject();

        try {

            body.put(
                    "messageID",
                    UUID.randomUUID().toString()
            );

            body.put("chatID", chatID);

            body.put("senderID", currentUserID);

            body.put("message", text);

        } catch (JSONException e) {

            e.printStackTrace();

            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,

                response -> {

                    messageInput.setText("");

                    getNewMessages();
                },

                error -> Toast.makeText(
                        this,
                        "Failed to send message",
                        Toast.LENGTH_SHORT
                ).show()
        );

        requestQueue.add(request);
    }

    // START POLLING

    private void startPolling() {

        runnable = new Runnable() {

            @Override
            public void run() {

                getNewMessages();

                handler.postDelayed(this, 3000);
            }
        };

        handler.post(runnable);
    }

    private void getNewMessages() {

        String url =
                BASE_URL +
                "getMessages.php?chatID=" +
                chatID +
                "&lastTime=" +
                lastMessageTime;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                response -> {

                    try {

                        JSONArray data =
                                response.getJSONArray("data");

                        if (data.length() == 0) {
                            return;
                        }

                        for (int i = 0; i < data.length(); i++) {

                            JSONObject obj =
                                    data.getJSONObject(i);

                            String sender =
                                    obj.getString("message_sender");

                            String content =
                                    obj.getString("message_content");

                            String time =
                                    obj.getString("time_sent");

                            boolean isMine =
                                    sender.equals(currentUserID);

                            messageList.add(
                                    new Message(
                                            content,
                                            time,
                                            isMine
                                    )
                            );

                            lastMessageTime = time;
                        }

                        messageAdapter.notifyDataSetChanged();

                        messagesRecyclerView.scrollToPosition(
                                messageList.size() - 1
                        );

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                },

                error -> {

                }
        );

        requestQueue.add(request);
    }

    private void showRatingDialog() {

        View dialogView = LayoutInflater.from(this)
                .inflate(
                        R.layout.dialog_rate_transaction,
                        null
                );

        TextView star1 = dialogView.findViewById(R.id.star1);
        TextView star2 = dialogView.findViewById(R.id.star2);
        TextView star3 = dialogView.findViewById(R.id.star3);
        TextView star4 = dialogView.findViewById(R.id.star4);
        TextView star5 = dialogView.findViewById(R.id.star5);

        EditText ratingCommentInput =
                dialogView.findViewById(
                        R.id.ratingCommentInput
                );

        final int[] rating = {0};

        View.OnClickListener starClick = view -> {

            if (view == star1) {
                rating[0] = 1;
            } else if (view == star2) {
                rating[0] = 2;
            } else if (view == star3) {
                rating[0] = 3;
            } else if (view == star4) {
                rating[0] = 4;
            } else if (view == star5) {
                rating[0] = 5;
            }

            updateStars(
                    rating[0],
                    star1,
                    star2,
                    star3,
                    star4,
                    star5
            );
        };

        star1.setOnClickListener(starClick);
        star2.setOnClickListener(starClick);
        star3.setOnClickListener(starClick);
        star4.setOnClickListener(starClick);
        star5.setOnClickListener(starClick);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        MaterialButton submitRatingButton =
                dialogView.findViewById(
                        R.id.submitRatingButton
                );

        submitRatingButton.setOnClickListener(v -> {

            if (rating[0] == 0) {

                Toast.makeText(
                        this,
                        "Please select a rating",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    this,
                    "Rating submitted successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateStars(
            int rating,
            TextView... stars
    ) {

        for (int i = 0; i < stars.length; i++) {

            if (i < rating) {

                stars[i].setTextColor(
                        Color.parseColor("#FACC15")
                );

            } else {

                stars[i].setTextColor(
                        Color.parseColor("#D1D5DB")
                );
            }
        }
    }
    @Override
    protected void onPause() {

        super.onPause();

        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {

        super.onResume();

        startPolling();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        handler.removeCallbacks(runnable);
    }
}

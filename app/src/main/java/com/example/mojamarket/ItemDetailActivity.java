package com.example.mojamarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.mojamarket.models.Post;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.network.ChatRepository;
import com.google.android.material.button.MaterialButton;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ViewPager2 itemImageSlider;
    private TextView imageCounter, itemName, itemPrice, itemStatus, itemDescription, itemLocation, sellerName, sellerUsername;
    private MaterialButton contactSellerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        initializeViews();

        Post post = SessionManager.getCurrentClickedItem(this);
        if (post != null) {
            renderPostDetails(post);
        } else {
            finish();
        }
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        itemImageSlider = findViewById(R.id.itemImageSlider);
        imageCounter = findViewById(R.id.imageCounter);
        itemName = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        itemStatus = findViewById(R.id.itemStatus);
        itemDescription = findViewById(R.id.itemDescription);
        itemLocation = findViewById(R.id.itemLocation);
        sellerName = findViewById(R.id.sellerName);
        sellerUsername = findViewById(R.id.sellerUsername);
        contactSellerButton = findViewById(R.id.contactSellerButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void renderPostDetails(Post post) {
        itemName.setText(post.getItemName());
        itemDescription.setText(post.getItemDescription());
        itemLocation.setText(post.getSellerLocation());
        itemPrice.setText("R" + NumberFormat.getNumberInstance(Locale.US).format(post.getPrice()));

        if (post.getSeller() != null) {
            sellerName.setText(post.getSeller().getName() + " " + post.getSeller().getSurname());
            sellerUsername.setText("@" + post.getSeller().getUsername());
        }

        ArrayList<String> images = new ArrayList<>();
        itemImageSlider.setAdapter(new ImageSliderAdapter(this, images));
        imageCounter.setText("0 / 0");

        setupChatAction(post);
    }

    private void setupChatAction(Post post) {
        String currentUserID = SessionManager.getLoggedInUser(this) != null ? SessionManager.getLoggedInUser(this).getUserID() : "";
        boolean isOwner = post.getSeller() != null && post.getSeller().getUserID().equals(currentUserID);

        contactSellerButton.setVisibility(isOwner ? View.GONE : View.VISIBLE);
        contactSellerButton.setOnClickListener(v -> {
            contactSellerButton.setEnabled(false);
            ChatRepository.createChat(currentUserID, post.getSeller().getUserID(), post.getItemID(), null, new ChatRepository.ActionCallback() {
                @Override
                public void onSuccess(String chatID) {
                    runOnUiThread(() -> {
                        contactSellerButton.setEnabled(true);
                        Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                        intent.putExtra("chatID", chatID);
                        intent.putExtra("currentUserID", currentUserID);
                        intent.putExtra("name", post.getSeller().getName());
                        intent.putExtra("username", post.getSeller().getUsername());
                        startActivity(intent);
                    });
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> {
                        contactSellerButton.setEnabled(true);
                        Toast.makeText(ItemDetailActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }
}
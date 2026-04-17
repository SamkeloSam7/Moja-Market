package com.example.mojamarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.models.Post;
import com.example.mojamarket.models.User;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView itemImage;
    private TextView itemName;
    private TextView itemPrice;
    private TextView itemStatus;
    private TextView itemCondition;
    private TextView itemDatePosted;
    private TextView sellerName;
    private TextView sellerUsername;
    private TextView sellerRating;
    private TextView itemDescription;
    private TextView itemLocation;
    private TextView itemConditionDetail;
    private MaterialButton contactSellerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        backButton = findViewById(R.id.backButton);
        itemImage = findViewById(R.id.itemImage);
        itemName = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        itemStatus = findViewById(R.id.itemStatus);
        itemCondition = findViewById(R.id.itemCondition);
        itemDatePosted = findViewById(R.id.itemDatePosted);
        sellerName = findViewById(R.id.sellerName);
        sellerUsername = findViewById(R.id.sellerUsername);
        sellerRating = findViewById(R.id.sellerRating);
        itemDescription = findViewById(R.id.itemDescription);
        itemLocation = findViewById(R.id.itemLocation);
        itemConditionDetail = findViewById(R.id.itemConditionDetail);
        contactSellerButton = findViewById(R.id.contactSellerButton);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        Post currentPost = SessionManager.getCurrentClickedItem(this);

        if (currentPost == null) {
            Toast.makeText(this, "Error loading item details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        itemName.setText(currentPost.getItemName());
        itemDescription.setText(currentPost.getItemDescription());
        itemLocation.setText(currentPost.getSellerLocation());
        itemCondition.setText(currentPost.getCondition());
        itemConditionDetail.setText(currentPost.getCondition());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        itemDatePosted.setText("Posted on " + sdf.format(currentPost.getDatePosted()));

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        itemPrice.setText("R" + numberFormat.format(currentPost.getPrice()));

        if (currentPost.getQuantity() > 0) {
            itemStatus.setText(currentPost.getStockStatus() + " (" + currentPost.getQuantity() + ")");
        } else {
            itemStatus.setText("Sold Out");
        }

        sellerRating.setText(String.format(Locale.getDefault(), "%.1f", currentPost.getAverageRating()));

        User seller = currentPost.getSeller();
        if (seller != null) {
            String fullName = seller.getName() + " " + seller.getSurname();

            sellerName.setText(fullName);
            sellerUsername.setText("@" + seller.getUsername());

            contactSellerButton.setOnClickListener(v -> {
                Intent chatIntent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                chatIntent.putExtra("user_id", seller.getUserID().toString());
                chatIntent.putExtra("name", fullName);
                chatIntent.putExtra("username", seller.getUsername());
                startActivity(chatIntent);
            });
        }
    }
}
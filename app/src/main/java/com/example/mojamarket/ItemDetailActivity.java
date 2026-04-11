package com.example.mojamarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
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

        Intent intent = getIntent();

        String name = intent.getStringExtra("item_name");
        String description = intent.getStringExtra("item_description");
        double price = intent.getDoubleExtra("item_price", 0);
        String location = intent.getStringExtra("item_location");
        String condition = intent.getStringExtra("item_condition");
        int stock = intent.getIntExtra("item_stock", 0);
        int imageRes = intent.getIntExtra("item_image", android.R.drawable.ic_menu_gallery);
        double rating = intent.getDoubleExtra("item_rating", 0);
        int ratingCount = intent.getIntExtra("item_rating_count", 0);
        String datePosted = intent.getStringExtra("item_date_posted");
        String seller = intent.getStringExtra("item_seller");

        if (name == null) name = "Unknown Item";
        if (description == null) description = "No description available.";
        if (location == null) location = "Unknown location";
        if (condition == null) condition = "Unknown condition";
        if (datePosted == null) datePosted = "Unknown date";
        if (seller == null) seller = "unknown_user";

        itemName.setText(name);
        itemDescription.setText(description);
        itemLocation.setText(location);
        itemCondition.setText(condition);
        itemConditionDetail.setText(condition);
        itemDatePosted.setText("Posted on " + datePosted);
        itemImage.setImageResource(imageRes);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        itemPrice.setText("R" + numberFormat.format((int) price));

        if (stock > 0) {
            itemStatus.setText("In Stock (" + stock + ")");
        } else {
            itemStatus.setText("Sold Out");
        }

        sellerName.setText("Samkelo Mthembu");
        sellerUsername.setText("@" + seller);

        if (ratingCount > 0) {
            sellerRating.setText(String.format(Locale.getDefault(), "%.1f (%d)", rating, ratingCount));
        } else {
            sellerRating.setText(String.format(Locale.getDefault(), "%.1f", rating));
        }

        String finalSeller = seller;
        contactSellerButton.setOnClickListener(v -> {
            Intent chatIntent = new Intent(ItemDetailActivity.this, ChatActivity.class);
            chatIntent.putExtra("user_id", 1);
            chatIntent.putExtra("name", "Samkelo Mthembu");
            chatIntent.putExtra("username", finalSeller);
            startActivity(chatIntent);
        });
    }
}
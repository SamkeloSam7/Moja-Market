package com.example.mojamarket;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView itemImage;
    private TextView itemName;
    private TextView itemPrice;
    private TextView itemStatus;
    private TextView itemCondition;
    private TextView sellerName;
    private TextView sellerUsername;
    private TextView sellerRating;
    private TextView itemDescription;
    private TextView itemLocation;
    private TextView itemConditionDetail;
    private MaterialButton contactSellerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        backButton = findViewById(R.id.backButton);
        itemImage = findViewById(R.id.itemImage);
        itemName = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        itemStatus = findViewById(R.id.itemStatus);
        itemCondition = findViewById(R.id.itemCondition);
        sellerName = findViewById(R.id.sellerName);
        sellerUsername = findViewById(R.id.sellerUsername);
        sellerRating = findViewById(R.id.sellerRating);
        itemDescription = findViewById(R.id.itemDescription);
        itemLocation = findViewById(R.id.itemLocation);
        itemConditionDetail = findViewById(R.id.itemConditionDetail);
        contactSellerButton = findViewById(R.id.contactSellerButton);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        int itemId = getIntent().getIntExtra("item_id", -1);

        Item item = getSampleItemById(itemId);

        if (item != null) {
            itemImage.setImageResource(item.getImageResId());
            itemName.setText(item.getName());
            itemPrice.setText("R" + (int) item.getPrice());
            itemDescription.setText(item.getDescription());
            itemLocation.setText(item.getLocation());
            itemCondition.setText(item.getCondition());
            itemConditionDetail.setText(item.getCondition());

            if (item.getStock() > 0) {
                itemStatus.setText("In Stock (" + item.getStock() + ")");
            } else {
                itemStatus.setText("Sold Out");
            }

            sellerName.setText("Samkelo Mthembu");
            sellerUsername.setText("@samkelo");
            sellerRating.setText("4.8 (12)");
        }

        contactSellerButton.setOnClickListener(v -> {
            // Chat screen will go here later
        });
    }

    private Item getSampleItemById(int itemId) {
        if (itemId == 1) {
            return new Item(
                    1,
                    "iPhone 13 Pro",
                    "Excellent condition, barely used. Comes with original box and charger.",
                    12000,
                    "Johannesburg",
                    "Used - Excellent",
                    1,
                    android.R.drawable.ic_menu_gallery,
                    4.8,
                    5,
                    "28/03/2026",
                    "yamkela_j"
            );
        } else if (itemId == 2) {
            return new Item(
                    2,
                    "Gaming Laptop",
                    "High performance laptop suitable for gaming, design, and coding.",
                    15000,
                    "Johannesburg",
                    "Used - Good",
                    1,
                    android.R.drawable.ic_menu_gallery,
                    4.7,
                    8,
                    "29/03/2026",
                    "blessings_k"
            );
        } else if (itemId == 3) {
            return new Item(
                    3,
                    "Sony Headphones",
                    "Clean audio, noise cancelling, and still in very good condition.",
                    2500,
                    "Wits Campus",
                    "Used - Very Good",
                    2,
                    android.R.drawable.ic_menu_gallery,
                    4.6,
                    4,
                    "30/03/2026",
                    "samkelo_m"
            );
        }
        return null;
    }
}
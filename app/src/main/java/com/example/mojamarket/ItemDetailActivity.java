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
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ViewPager2 itemImageSlider;
    private TextView imageCounter;
    private TextView itemName, itemPrice, itemStatus, itemCondition;
    private TextView sellerName, sellerUsername, sellerRating;
    private TextView itemDescription, itemLocation, itemConditionDetail, itemDatePosted;
    private MaterialButton contactSellerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        backButton = findViewById(R.id.backButton);
        itemImageSlider = findViewById(R.id.itemImageSlider);
        imageCounter = findViewById(R.id.imageCounter);
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
        itemDatePosted = findViewById(R.id.itemDatePosted);
        contactSellerButton = findViewById(R.id.contactSellerButton);

        backButton.setOnClickListener(v -> finish());

        Post post = SessionManager.getCurrentClickedItem(this);

        if (post == null) {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        itemName.setText(post.getItemName());
        itemDescription.setText(post.getItemDescription());
        itemLocation.setText(post.getSellerLocation());
        itemCondition.setText(post.getCondition());
        itemConditionDetail.setText(post.getCondition());
        itemDatePosted.setText("Posted on " + post.getDatePosted().toString());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        itemPrice.setText("R" + numberFormat.format(post.getPrice()));

        if (post.getQuantity() > 0) {
            itemStatus.setText("In Stock (" + post.getQuantity() + ")");
        } else {
            itemStatus.setText("Sold Out");
        }

        if (post.getSeller() != null) {
            sellerName.setText(post.getSeller().getName() + " " + post.getSeller().getSurname());
            sellerUsername.setText("@" + post.getSeller().getUsername());
        } else {
            sellerName.setText("Unknown Seller");
            sellerUsername.setText("@unknown");
        }

        sellerRating.setText(String.format(Locale.getDefault(), "%.1f", post.getAverageRating()));

        ArrayList<String> imageUris = post.getImageUris();
        if (imageUris == null) {
            imageUris = new ArrayList<>();
        }

        ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageUris);
        itemImageSlider.setAdapter(adapter);

        if (imageUris.size() == 0) {
            imageCounter.setText("0 / 0");
        } else {
            imageCounter.setText("1 / " + imageUris.size());
        }

        ArrayList<String> finalImageUris = imageUris;
        itemImageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (finalImageUris.size() == 0) {
                    imageCounter.setText("0 / 0");
                } else {
                    imageCounter.setText((position + 1) + " / " + finalImageUris.size());
                }
            }
        });

        boolean isOwnPost = false;
        if (post.getSeller() != null && SessionManager.getLoggedInUser(this) != null) {
            isOwnPost = post.getSeller().getUserID()
                    .equals(SessionManager.getLoggedInUser(this).getUserID());
        }

        if (isOwnPost) {
            contactSellerButton.setVisibility(View.GONE);
        } else {
            contactSellerButton.setVisibility(View.VISIBLE);
            contactSellerButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, ChatActivity.class);
                if (post.getSeller() != null) {
                    intent.putExtra("name", post.getSeller().getName() + " " + post.getSeller().getSurname());
                    intent.putExtra("username", post.getSeller().getUsername());
                }
                startActivity(intent);
            });
        }
    }
}
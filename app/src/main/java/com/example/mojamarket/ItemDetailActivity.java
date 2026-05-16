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
    private TextView itemName, itemPrice, itemStatus, itemDescription, itemLocation, sellerName, sellerUsername;
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
        itemImageSlider.setAdapter(new ImageSliderAdapter(this, images));;
    }
}
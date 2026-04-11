package com.example.mojamarket;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MyListingAdapter extends RecyclerView.Adapter<MyListingAdapter.MyListingViewHolder> {

    private final List<Item> itemList;

    public MyListingAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_listing, parent, false);
        return new MyListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListingViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.profileListingName.setText(item.getName());
        holder.profileListingDescription.setText(item.getDescription());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        holder.profileListingPrice.setText("R" + numberFormat.format((int) item.getPrice()));
        holder.profileListingRating.setText(String.valueOf(item.getRating()));

        if (item.getStock() > 0) {
            holder.profileListingStatus.setText("Available");
            holder.profileListingStatus.setBackgroundResource(R.drawable.bg_available_badge);
            holder.profileListingStatus.setTextColor(0xFFFFFFFF);
        } else {
            holder.profileListingStatus.setText("Sold Out");
            holder.profileListingStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            holder.profileListingStatus.setTextColor(0xFF6B7280);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
            intent.putExtra("item_id", item.getItemId());
            intent.putExtra("item_name", item.getName());
            intent.putExtra("item_description", item.getDescription());
            intent.putExtra("item_price", item.getPrice());
            intent.putExtra("item_location", item.getLocation());
            intent.putExtra("item_condition", item.getCondition());
            intent.putExtra("item_stock", item.getStock());
            intent.putExtra("item_image", item.getImageResId());
            intent.putExtra("item_rating", item.getRating());
            intent.putExtra("item_seller", item.getSellerUsername());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class MyListingViewHolder extends RecyclerView.ViewHolder {
        TextView profileListingName, profileListingStatus, profileListingDescription, profileListingPrice, profileListingRating;

        public MyListingViewHolder(@NonNull View itemView) {
            super(itemView);
            profileListingName = itemView.findViewById(R.id.profileListingName);
            profileListingStatus = itemView.findViewById(R.id.profileListingStatus);
            profileListingDescription = itemView.findViewById(R.id.profileListingDescription);
            profileListingPrice = itemView.findViewById(R.id.profileListingPrice);
            profileListingRating = itemView.findViewById(R.id.profileListingRating);
        }
    }
}
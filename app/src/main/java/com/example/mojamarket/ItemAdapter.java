package com.example.mojamarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final Context context;
    private final List<Item> itemList;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        holder.itemName.setText(item.getName());
        holder.itemDescription.setText(item.getDescription());
        holder.itemPrice.setText(String.format(Locale.getDefault(), "R%,.0f", item.getPrice()));
        holder.itemLocation.setText(item.getLocation());
        holder.itemDate.setText("Posted " + item.getDatePosted());
        holder.itemSeller.setText("by " + item.getSellerUsername());

        if (item.getRating() > 0) {
            holder.itemRating.setText(String.format(Locale.getDefault(), "%.1f (%d)", item.getRating(), item.getRatingCount()));
        } else {
            holder.itemRating.setText("0.0");
        }

        if (item.getStock() > 0) {
            holder.itemStatus.setText("Available");
            holder.itemStatus.setBackgroundResource(R.drawable.bg_available_badge);
            holder.itemStatus.setTextColor(0xFFFFFFFF);
        } else {
            holder.itemStatus.setText("Sold Out");
            holder.itemStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            holder.itemStatus.setTextColor(0xFF6B7280);
        }

        holder.itemImage.setImageResource(item.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("item_id", item.getItemId());
            intent.putExtra("item_name", item.getName());
            intent.putExtra("item_description", item.getDescription());
            intent.putExtra("item_price", item.getPrice());
            intent.putExtra("item_location", item.getLocation());
            intent.putExtra("item_condition", item.getCondition());
            intent.putExtra("item_stock", item.getStock());
            intent.putExtra("item_image", item.getImageResId());
            intent.putExtra("item_rating", item.getRating());
            intent.putExtra("item_rating_count", item.getRatingCount());
            intent.putExtra("item_date_posted", item.getDatePosted());
            intent.putExtra("item_seller", item.getSellerUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemDescription, itemPrice, itemLocation, itemRating, itemStatus, itemDate, itemSeller;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemLocation = itemView.findViewById(R.id.itemLocation);
            itemRating = itemView.findViewById(R.id.itemRating);
            itemStatus = itemView.findViewById(R.id.itemStatus);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemSeller = itemView.findViewById(R.id.itemSeller);
        }
    }
}
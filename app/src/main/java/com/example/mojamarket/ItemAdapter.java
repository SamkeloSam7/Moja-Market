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

import com.example.mojamarket.models.Post;
import com.example.mojamarket.session.SessionManager;
import com.example.mojamarket.utility.Helper;

import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final Context context;
    private final List<Post> posts;

    public ItemAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.itemName.setText(post.getItemName());
        holder.itemDescription.setText(post.getItemDescription());
        holder.itemPrice.setText(String.format(Locale.getDefault(), "R%,.0f", post.getPrice()));
        holder.itemLocation.setText(post.getSellerLocation());
        holder.itemDate.setText("Posted " + Helper.formatDate(post.getDatePosted()));
        holder.itemSeller.setText("by " + (post.getSeller() != null ? post.getSeller().getUsername() : "unknown"));

        if (post.getAverageRating() > 0) {
            holder.itemRating.setText(String.format(Locale.getDefault(), "%.1f", post.getAverageRating()));
        } else {
            holder.itemRating.setText("0.0");
        }

        if (post.getQuantity() > 0) {
            holder.itemStatus.setText("Available");
            holder.itemStatus.setBackgroundResource(R.drawable.bg_available_badge);
            holder.itemStatus.setTextColor(0xFFFFFFFF);
        } else {
            holder.itemStatus.setText("Sold Out");
            holder.itemStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            holder.itemStatus.setTextColor(0xFF6B7280);
        }

        holder.itemImage.setImageResource(android.R.drawable.ic_menu_gallery);

        holder.itemView.setOnClickListener(v -> {
            SessionManager.setCurrentClickedItem(post);
            Intent intent = new Intent(context, ItemDetailActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
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
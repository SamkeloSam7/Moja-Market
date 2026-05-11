package com.example.mojamarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojamarket.models.Post;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MyListingAdapter extends RecyclerView.Adapter<MyListingAdapter.MyListingViewHolder> {

    public interface OnEditPostClickListener {
        void onEditPostClick(Post post);
    }

    private final List<Post> postList;
    private final OnEditPostClickListener editListener;

    public MyListingAdapter(List<Post> postList, OnEditPostClickListener editListener) {
        this.postList = postList;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public MyListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_listing, parent, false);
        return new MyListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListingViewHolder holder, int position) {
        Post post = postList.get(position);
        Context ctx = holder.itemView.getContext();

        holder.profileListingName.setText(post.getItemName());
        holder.profileListingDescription.setText(post.getItemDescription());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        holder.profileListingPrice.setText("R" + numberFormat.format(post.getPrice()));
        holder.profileListingRating.setText(String.format(Locale.getDefault(), "%.1f", post.getAverageRating()));

        if ("available".equalsIgnoreCase(post.getStockStatus()) || post.getQuantity() > 0) {
            holder.profileListingStatus.setText("Available");
            holder.profileListingStatus.setBackgroundResource(R.drawable.bg_available_badge);
            holder.profileListingStatus.setTextColor(0xFFFFFFFF);
        } else {
            holder.profileListingStatus.setText("Sold Out");
            holder.profileListingStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            holder.profileListingStatus.setTextColor(0xFF6B7280);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, ItemDetailActivity.class);
            // use the same key that ItemDetailActivity reads ("ITEM_ID" not "post_id")
            intent.putExtra("ITEM_ID", post.getItemID().toString());
            ctx.startActivity(intent);
        });

        holder.editListingBtn.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEditPostClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyListingViewHolder extends RecyclerView.ViewHolder {
        TextView profileListingName, profileListingStatus, profileListingDescription, profileListingPrice, profileListingRating;
        ImageButton editListingBtn;

        public MyListingViewHolder(@NonNull View itemView) {
            super(itemView);
            profileListingName = itemView.findViewById(R.id.profileListingName);
            profileListingStatus = itemView.findViewById(R.id.profileListingStatus);
            profileListingDescription = itemView.findViewById(R.id.profileListingDescription);
            profileListingPrice = itemView.findViewById(R.id.profileListingPrice);
            profileListingRating = itemView.findViewById(R.id.profileListingRating);
            editListingBtn = itemView.findViewById(R.id.editListingBtn);
        }
    }
}
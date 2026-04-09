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

import java.text.NumberFormat;
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

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        holder.itemPrice.setText("R" + numberFormat.format((int) item.getPrice()));

        holder.itemLocation.setText(item.getLocation());
        holder.itemRating.setText(item.getRating() + " (" + item.getRatingCount() + ")");
        holder.itemDate.setText("Posted " + item.getPostedDate());
        holder.itemSeller.setText("by " + item.getSellerUsername());

        if (item.getStock() > 0) {
            holder.itemStatus.setText("Available");
        } else {
            holder.itemStatus.setText("Sold Out");
        }

        holder.itemImage.setImageResource(item.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra("item_id", item.getItemId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemDescription, itemPrice, itemLocation, itemRating, itemDate, itemSeller, itemStatus;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemLocation = itemView.findViewById(R.id.itemLocation);
            itemRating = itemView.findViewById(R.id.itemRating);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemSeller = itemView.findViewById(R.id.itemSeller);
            itemStatus = itemView.findViewById(R.id.itemStatus);
        }
    }
}
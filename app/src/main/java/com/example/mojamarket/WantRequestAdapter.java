package com.example.mojamarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class WantRequestAdapter extends RecyclerView.Adapter<WantRequestAdapter.WantRequestViewHolder> {

    private final Context context;
    private final List<WantRequest> requestList;

    public WantRequestAdapter(Context context, List<WantRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public WantRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_want_request, parent, false);
        return new WantRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WantRequestViewHolder holder, int position) {
        WantRequest request = requestList.get(position);

        holder.requestItemName.setText(request.getItemName());
        holder.requestDescription.setText(request.getDescription());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        holder.requestBudget.setText("Budget: R" + numberFormat.format((int) request.getBudget()));

        holder.requestDate.setText(request.getDatePosted());
        holder.requestUser.setText("by " + request.getUsername());

        if (request.isFulfilled()) {
            holder.requestStatus.setText("Fulfilled");
            holder.requestStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            holder.requestStatus.setTextColor(0xFF64748B);
            holder.respondButton.setVisibility(View.GONE);
        } else {
            holder.requestStatus.setText("Looking");
            holder.requestStatus.setBackgroundResource(R.drawable.bg_looking_badge);
            holder.requestStatus.setTextColor(0xFFFFFFFF);

            if (request.isOwnRequest()) {
                holder.respondButton.setVisibility(View.GONE);
            } else {
                holder.respondButton.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WantDetailActivity.class);
            intent.putExtra("request_id", request.getRequestId());
            intent.putExtra("item_name", request.getItemName());
            intent.putExtra("description", request.getDescription());
            intent.putExtra("budget", request.getBudget());
            intent.putExtra("date_posted", request.getDatePosted());
            intent.putExtra("username", request.getUsername());
            intent.putExtra("fulfilled", request.isFulfilled());
            intent.putExtra("own_request", request.isOwnRequest());
            context.startActivity(intent);
        });

        holder.respondButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("user_id", request.getRequestId());
            intent.putExtra("name", "Samkelo Mthembu");
            intent.putExtra("username", request.getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class WantRequestViewHolder extends RecyclerView.ViewHolder {
        TextView requestItemName, requestStatus, requestDescription, requestBudget, requestDate, requestUser;
        MaterialButton respondButton;

        public WantRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            requestItemName = itemView.findViewById(R.id.requestItemName);
            requestStatus = itemView.findViewById(R.id.requestStatus);
            requestDescription = itemView.findViewById(R.id.requestDescription);
            requestBudget = itemView.findViewById(R.id.requestBudget);
            requestDate = itemView.findViewById(R.id.requestDate);
            requestUser = itemView.findViewById(R.id.requestUser);
            respondButton = itemView.findViewById(R.id.respondButton);
        }
    }
}
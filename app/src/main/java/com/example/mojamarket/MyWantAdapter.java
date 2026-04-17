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
import com.example.mojamarket.models.Want;
import com.example.mojamarket.session.SessionManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyWantAdapter extends RecyclerView.Adapter<MyWantAdapter.MyWantViewHolder> {

    private final Context context;
    private final List<Want> requestList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public MyWantAdapter(Context context, List<Want> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public MyWantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_want_request, parent, false);
        return new MyWantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWantViewHolder holder, int position) {
        Want request = requestList.get(position);

        holder.requestItemName.setText(request.getItem());
        holder.requestDescription.setText(request.getDescription());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        holder.requestBudget.setText("Budget: R" + numberFormat.format((int) request.getBudget()));

        holder.requestDate.setText(dateFormat.format(request.getDatePosted()));
        holder.requestUser.setText("by " + request.getBuyer().getUsername());

        boolean isActive = request.isWantStatus();
        boolean isOwn = request.getBuyer().getUserID().equals(
                SessionManager.getLoggedInUser(context).getUserID()
        );

        if (!isActive) {
            holder.requestStatus.setText("Fulfilled");
            holder.requestStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            holder.requestStatus.setTextColor(0xFF64748B);
            holder.respondButton.setVisibility(View.GONE);
        } else {
            holder.requestStatus.setText("Looking");
            holder.requestStatus.setBackgroundResource(R.drawable.bg_looking_badge);
            holder.requestStatus.setTextColor(0xFFFFFFFF);
            holder.respondButton.setVisibility(isOwn ? View.GONE : View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WantDetailActivity.class);
            intent.putExtra("request_id", request.getId().toString());
            intent.putExtra("item_name", request.getItem());
            intent.putExtra("description", request.getDescription());
            intent.putExtra("budget", request.getBudget());
            intent.putExtra("date_posted", dateFormat.format(request.getDatePosted()));
            intent.putExtra("username", request.getBuyer().getUsername());
            intent.putExtra("fulfilled", !request.isWantStatus());
            intent.putExtra("own_request", isOwn);
            context.startActivity(intent);
        });

        holder.respondButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("user_id", request.getBuyer().getUserID().toString());
            intent.putExtra("name", request.getBuyer().getName());
            intent.putExtra("username", request.getBuyer().getUsername());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class MyWantViewHolder extends RecyclerView.ViewHolder {
        TextView requestItemName, requestStatus, requestDescription, requestBudget, requestDate, requestUser;
        MaterialButton respondButton;

        public MyWantViewHolder(@NonNull View itemView) {
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
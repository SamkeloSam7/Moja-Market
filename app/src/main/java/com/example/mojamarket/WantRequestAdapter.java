package com.example.mojamarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojamarket.models.Want;
import com.example.mojamarket.network.ApiClient;
import com.example.mojamarket.network.ApiConstants;
import com.example.mojamarket.session.SessionManager;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class WantRequestAdapter extends RecyclerView.Adapter<WantRequestAdapter.WantRequestViewHolder> {

    private final Context context;
    private final List<Want> requestList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public WantRequestAdapter(Context context, List<Want> requestList) {
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
        } else {
            holder.requestStatus.setText("Looking");
            holder.requestStatus.setBackgroundResource(R.drawable.bg_looking_badge);
            holder.requestStatus.setTextColor(0xFFFFFFFF);
        }

        // Clicking the card always just opens the detail screen
        holder.itemView.setOnClickListener(v -> {
            SessionManager.setCurrentClickedWantRequest(request);
            Intent intent = new Intent(context, WantDetailActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class WantRequestViewHolder extends RecyclerView.ViewHolder {
        TextView requestItemName, requestStatus, requestDescription, requestBudget, requestDate, requestUser;

        public WantRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestItemName = itemView.findViewById(R.id.requestItemName);
            requestStatus = itemView.findViewById(R.id.requestStatus);
            requestDescription = itemView.findViewById(R.id.requestDescription);
            requestBudget = itemView.findViewById(R.id.requestBudget);
            requestDate = itemView.findViewById(R.id.requestDate);
            requestUser = itemView.findViewById(R.id.requestUser);
        }
    }
}
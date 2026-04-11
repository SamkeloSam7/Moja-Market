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

public class MyWantAdapter extends RecyclerView.Adapter<MyWantAdapter.MyWantViewHolder> {

    private final List<WantRequest> wantList;

    public MyWantAdapter(List<WantRequest> wantList) {
        this.wantList = wantList;
    }

    @NonNull
    @Override
    public MyWantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_want, parent, false);
        return new MyWantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWantViewHolder holder, int position) {
        WantRequest want = wantList.get(position);

        holder.profileWantName.setText(want.getItemName());
        holder.profileWantDescription.setText(want.getDescription());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        holder.profileWantBudget.setText("Budget: R" + numberFormat.format((int) want.getBudget()));

        if (want.isFulfilled()) {
            holder.profileWantStatus.setText("Fulfilled");
            holder.profileWantStatus.setBackgroundResource(R.drawable.bg_fulfilled_badge);
            holder.profileWantStatus.setTextColor(0xFF6B7280);
        } else {
            holder.profileWantStatus.setText("Looking");
            holder.profileWantStatus.setBackgroundResource(R.drawable.bg_looking_badge);
            holder.profileWantStatus.setTextColor(0xFFFFFFFF);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WantDetailActivity.class);
            intent.putExtra("request_id", want.getRequestId());
            intent.putExtra("item_name", want.getItemName());
            intent.putExtra("description", want.getDescription());
            intent.putExtra("budget", want.getBudget());
            intent.putExtra("date_posted", want.getDatePosted());
            intent.putExtra("username", want.getUsername());
            intent.putExtra("fulfilled", want.isFulfilled());
            intent.putExtra("own_request", want.isOwnRequest());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return wantList.size();
    }

    static class MyWantViewHolder extends RecyclerView.ViewHolder {
        TextView profileWantName, profileWantStatus, profileWantDescription, profileWantBudget;

        public MyWantViewHolder(@NonNull View itemView) {
            super(itemView);
            profileWantName = itemView.findViewById(R.id.profileWantName);
            profileWantStatus = itemView.findViewById(R.id.profileWantStatus);
            profileWantDescription = itemView.findViewById(R.id.profileWantDescription);
            profileWantBudget = itemView.findViewById(R.id.profileWantBudget);
        }
    }
}